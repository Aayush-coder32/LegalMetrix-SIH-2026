import { useState, type ReactNode } from 'react'
import { Link } from 'react-router-dom'
import {
  ArrowRight, BadgeCheck, Bell, CalendarDays, Check, ClipboardList,
  Clock3, Globe, Play, QrCode, Scale, ShieldCheck, X, FileCheck2, Activity, Mail,
} from 'lucide-react'

const steps = [
  { number: '01', title: 'Apply Online', text: 'Register your business and submit your instrument details with required documents.', icon: ClipboardList, tone: 'blue' },
  { number: '02', title: 'Book Inspection', text: 'An authorized officer schedules an inspection and records the verification details.', icon: CalendarDays, tone: 'violet' },
  { number: '03', title: 'Get a Digital Certificate', text: 'After successful verification, access a digital certificate with a QR check.', icon: BadgeCheck, tone: 'green' },
  { number: '04', title: 'Track & Stay Updated', text: 'Follow application progress and see renewal reminders from your dashboard.', icon: Bell, tone: 'orange' },
]

export default function LandingPage() {
  const [menuOpen, setMenuOpen] = useState(false)
  return <div className="lm-landing">
    <header className="lm-header">
      <Link to="/" className="lm-brand" aria-label="LegalMetrix home">
        <span className="lm-brand-mark"><Scale size={30}/><i><Check size={11}/></i></span>
        <span className="lm-brand-copy"><strong>LegalMetrix</strong><small>Digital Legal Metrology Services</small></span>
      </Link>
      <button className="lm-menu-toggle" aria-label={menuOpen ? 'Close navigation' : 'Open navigation'} onClick={() => setMenuOpen(!menuOpen)}>{menuOpen ? <X/> : <span>☰</span>}</button>
      <nav className={`lm-nav ${menuOpen ? 'is-open' : ''}`}>
        <a className="is-active" href="#home" onClick={() => setMenuOpen(false)}>Home</a>
        <a href="#about" onClick={() => setMenuOpen(false)}>About</a>
        <a href="#services" onClick={() => setMenuOpen(false)}>Services</a>
        <a href="#process" onClick={() => setMenuOpen(false)}>How It Works</a>
        <a href="#contact" onClick={() => setMenuOpen(false)}>Contact</a>
      </nav>
      <div className="lm-header-actions"><span className="lm-language"><Globe size={17}/> English <span>⌄</span></span><Link className="lm-login" to="/login">Login</Link><Link className="lm-register" to="/register">Register</Link></div>
    </header>

    <main>
      <section className="lm-hero" id="home">
        <div className="lm-hero-copy">
          <div className="lm-prototype-chip"><Scale size={16}/> SIH 2026 · DIGITAL SERVICE PROTOTYPE</div>
          <h1>Online Verification System for <span>Weighing and Measuring Instruments</span></h1>
          <p className="lm-hero-lead">A unified digital platform concept for businesses and Legal Metrology Officers to manage instrument verification with clarity and ease.</p>
          <div className="lm-hero-actions"><Link to="/register" className="lm-primary-cta">Get Started <ArrowRight size={18}/></Link><a href="#process" className="lm-secondary-cta"><Play size={15} fill="currentColor"/> Watch Demo</a></div>
          <div className="lm-highlights" aria-label="Prototype design principles">
            <Highlight icon={<BadgeCheck/>} title="Clear" detail="Transparent process"/>
            <Highlight icon={<ShieldCheck/>} title="Secure" detail="Role-based access"/>
            <Highlight icon={<Clock3/>} title="Guided" detail="Step-by-step workflow"/>
            <Highlight icon={<QrCode/>} title="Digital" detail="Certificate-ready design"/>
          </div>
        </div>
        <div className="lm-hero-visual">
          <img className="lm-hero-art-image" src="/images/metrology-hero.png" alt="Illustration of a weighing scale with a certificate preview and phone" />
        </div>
      </section>

      <section className="lm-about" id="about">
        <div className="lm-about-copy"><span className="lm-kicker">ABOUT LEGALMETRIX</span><h2>Clearer metrology services, designed around trust.</h2><p>LegalMetrix is an SIH 2026 prototype for bringing instrument registration, inspection requests and application updates into one simple digital workspace.</p><div className="lm-about-points"><span><ShieldCheck size={17}/> Secure, role-based access</span><span><Activity size={17}/> Transparent request tracking</span><span><BadgeCheck size={17}/> Officer-led verification</span></div></div>
        <div className="lm-about-note"><span className="lm-about-note-icon"><Scale size={23}/></span><b>Built for a fair measurement ecosystem</b><p>Business users can submit details and evidence online. Authorized officers review requests and record decisions.</p><small>Prototype demonstration · Not an official government service</small></div>
      </section>

      <section className="lm-services" id="services">
        <div className="lm-section-intro"><span className="lm-kicker">PORTAL SERVICES</span><h2>One workspace for every step</h2><p>Keep instrument applications and verification activity organized in one place.</p></div>
        <div className="lm-service-grid">
          <article className="lm-service-card"><span className="lm-service-icon blue"><Scale size={22}/></span><h3>Instrument registration</h3><p>Submit instrument details and business location to start a verification application.</p><Link to="/instruments/new">Register an instrument <ArrowRight size={14}/></Link></article>
          <article className="lm-service-card"><span className="lm-service-icon green"><CalendarDays size={22}/></span><h3>Inspection requests</h3><p>Choose a preferred date, share the inspection location and attach an instrument photo.</p><Link to="/inspections/book">Request an inspection <ArrowRight size={14}/></Link></article>
          <article className="lm-service-card"><span className="lm-service-icon violet"><ClipboardList size={22}/></span><h3>Application tracking</h3><p>Review submitted applications and follow their recorded status from your workspace.</p><Link to="/login">Open your workspace <ArrowRight size={14}/></Link></article>
          <article className="lm-service-card"><span className="lm-service-icon orange"><FileCheck2 size={22}/></span><h3>Certificates &amp; QR checks</h3><p>Certificate records and public verification are planned for a later prototype phase.</p><Link to="/verify">Explore certificate checks <ArrowRight size={14}/></Link></article>
        </div>
      </section>

      <section className="lm-process" id="process">
        <div className="lm-section-intro"><span className="lm-kicker">SIMPLE · 4-STEP PROCESS</span><h2>How It Works</h2><p>From registration to verification, a clear online journey for instrument owners.</p></div>
        <div className="lm-step-grid">{steps.map(({ number, title, text, icon: Icon, tone }, index) => <article className={`lm-step-card ${tone}`} key={number}><span className="lm-step-number">{number}</span><span className="lm-step-icon"><Icon size={31}/></span><h3>{title}</h3><p>{text}</p>{index < steps.length - 1 && <ArrowRight className="lm-step-arrow" size={19}/>}</article>)}</div>
      </section>

      <section className="lm-contact" id="contact"><div><span className="lm-kicker">CONTACT &amp; SUPPORT</span><h2>Need help with the prototype?</h2><p>For account or demonstration support, contact the LegalMetrix team. Do not send confidential business or instrument records by email.</p></div><a className="lm-contact-link" href="mailto:support@legalmetrix.in"><Mail size={18}/> support@legalmetrix.in <ArrowRight size={15}/></a></section>

    </main>
    <footer className="lm-footer"><div className="lm-footer-left"><ShieldCheck size={27}/><span>LegalMetrix SIH prototype</span><i/><span>For demonstration only</span><i/><span>Not an official government service</span></div><a href="#home">Back to top <ArrowRight size={15}/></a></footer>
  </div>
}

function Highlight({ icon, title, detail }: { icon: ReactNode; title: string; detail: string }) {
  return <div className="lm-highlight"><span>{icon}</span><div><b>{title}</b><small>{detail}</small></div></div>
}
