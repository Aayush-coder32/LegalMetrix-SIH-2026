package in.legalmetrix.identity;

import in.legalmetrix.audit.AuditService;
import in.legalmetrix.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock UserRepository users;
    @Mock RoleRepository roles;
    @Mock PasswordEncoder passwords;
    @Mock JwtService jwt;
    @Mock AuditService audit;
    @Mock HttpServletRequest request;
    AuthController controller;
    Role ownerRole;

    @BeforeEach void setUp() {
        controller = new AuthController(users, roles, passwords, jwt, audit);
        ownerRole = mock(Role.class);
        when(ownerRole.getCode()).thenReturn("BUSINESS_OWNER");
        when(ownerRole.getDisplayName()).thenReturn("Business / Instrument Owner");
    }

    @Test void registrationCreatesBusinessOwnerWithEncodedPassword() {
        when(users.existsByEmailIgnoreCase("owner@example.in")).thenReturn(false);
        when(roles.findByCode("BUSINESS_OWNER")).thenReturn(Optional.of(ownerRole));
        when(passwords.encode("A-Strong-Password-9")).thenReturn("bcrypt-hash");
        when(users.save(any(UserAccount.class))).thenAnswer(call -> call.getArgument(0));
        when(jwt.issue(any(UserAccount.class))).thenReturn("signed-access-token");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        var response = controller.register(new AuthController.RegisterRequest(
            " Owner@Example.in ", "A-Strong-Password-9", "  Sample Owner ", "+91-9000000000"), request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("signed-access-token", response.getBody().accessToken());
        assertEquals("owner@example.in", response.getBody().user().email());
        assertEquals("Sample Owner", response.getBody().user().fullName());
        assertEquals("bcrypt-hash", capturedUser().getPasswordHash());
        assertEquals("BUSINESS_OWNER", response.getBody().user().roles().getFirst());
        verify(audit).record(anyString(), eq("AUTH_REGISTER"), eq("USER"), anyString(), eq("SUCCESS"), eq("127.0.0.1"));
    }

    @Test void duplicateEmailIsRejected() {
        when(users.existsByEmailIgnoreCase("owner@example.in")).thenReturn(true);
        var error = assertThrows(ResponseStatusException.class, () -> controller.register(
            new AuthController.RegisterRequest("owner@example.in", "A-Strong-Password-9", "Owner", null), request));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(users, never()).save(any());
    }

    @Test void invalidCredentialsReturnGenericUnauthorizedAndAreAudited() {
        when(users.findByEmailIgnoreCase("owner@example.in")).thenReturn(Optional.empty());
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        var error = assertThrows(ResponseStatusException.class, () -> controller.login(
            new AuthController.LoginRequest("owner@example.in", "wrong-password"), request));
        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
        assertEquals("Invalid email or password", error.getReason());
        verify(audit).record(null, "AUTH_LOGIN", "USER", null, "FAILURE", "127.0.0.1");
    }

    private UserAccount capturedUser() {
        var captor = org.mockito.ArgumentCaptor.forClass(UserAccount.class);
        verify(users).save(captor.capture());
        return captor.getValue();
    }
}
