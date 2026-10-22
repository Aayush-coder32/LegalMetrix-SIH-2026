import { useState, type ReactNode } from 'react'
import { Link } from 'react-router-dom'
import {
  ArrowRight, BadgeCheck, Bell, CalendarDays, Check, ClipboardList,
  Clock3, Globe, Play, QrCode, Scale, ShieldCheck, X,
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
        <div className="lm-hero-visual" aria-label="Illustration of a weighing scale with a certificate preview and phone">
          <div className="lm-visual-wash"/><div className="lm-visual-orbit"/>
          <div className="lm-certificate-card"><div className="lm-certificate-top"><span className="lm-mini-seal"><Scale size={17}/></span><span><b>Certificate Preview</b><small>Illustrative interface only</small></span></div><span className="lm-preview-tag"><i/> PREVIEW</span><div className="lm-cert-rule"/><div className="lm-cert-line"><span>Instrument type</span><b>Example instrument</b></div><div className="lm-cert-line"><span>Reference</span><b>Sample data</b></div><div className="lm-cert-line"><span>Certificate status</span><b className="lm-cert-status"><Check size={11}/> Not a real record</b></div><div className="lm-cert-foot"><QrCode size={38}/><span>QR visual<br/>illustration only</span></div></div>
          <div className="lm-scale-art"><div className="lm-scale-pan"/><div className="lm-scale-neck"/><div className="lm-scale-body"><div className="lm-scale-screen"><strong>0.000</strong><small>kg</small></div><span className="lm-scale-button"/><span className="lm-scale-button"/><span className="lm-scale-button"/></div><div className="lm-scale-foot left"/><div className="lm-scale-foot right"/></div>
          <div className="lm-phone"><div className="lm-phone-notch"/><div className="lm-phone-screen"><span className="lm-scan-label">QR PREVIEW</span><QrCode size={75} strokeWidth={1.7}/><span className="lm-phone-check"><Check size={15}/></span><b>Preview only</b><small>Not connected to records</small></div></div>
          <div className="lm-art-caption"><ShieldCheck size={15}/> Prototype visual · no official certificate shown</div>
        </div>
      </section>

      <section className="lm-process" id="process">
        <div className="lm-section-intro"><span className="lm-kicker">SIMPLE · 4-STEP PROCESS</span><h2>How It Works</h2><p>From registration to verification, a clear online journey for instrument owners.</p></div>
        <div className="lm-step-grid">{steps.map(({ number, title, text, icon: Icon, tone }, index) => <article className={`lm-step-card ${tone}`} key={number}><span className="lm-step-number">{number}</span><span className="lm-step-icon"><Icon size={31}/></span><h3>{title}</h3><p>{text}</p>{index < steps.length - 1 && <ArrowRight className="lm-step-arrow" size={19}/>}</article>)}</div>
      </section>

      <section className="lm-platform-note" id="about"><div className="lm-note-icon"><ShieldCheck size={22}/></div><div><b>Designed for transparent compliance services</b><p>LegalMetrix is an SIH prototype. The workflows shown are illustrative and require formal authorization before real-world use.</p></div><Link to="/register">Explore the prototype <ArrowRight size={16}/></Link></section>
    </main>
    <footer className="lm-footer" id="contact"><div className="lm-footer-left"><ShieldCheck size={28}/><span>Prototype concept</span><i/><span>For businesses and officers</span><i/><span>Designed with consumer trust in mind</span></div><a href="mailto:help@legalmetrix.example">Contact the prototype team</a></footer>
  </div>
}

function Highlight({ icon, title, detail }: { icon: ReactNode; title: string; detail: string }) {
  return <div className="lm-highlight"><span>{icon}</span><div><b>{title}</b><small>{detail}</small></div></div>
}
