---
layout: default
title: Contatti
nav_order: 4
---

<script>
  document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('theme-toggle');
    const saved = localStorage.getItem('theme');
    if (saved) {
      jtd.setTheme(saved);
      if (btn) btn.textContent = saved === 'dark' ? '☀️' : '🌙';
    }
    if (btn) {
      btn.addEventListener('click', () => {
        const curr = jtd.getTheme();
        const next = curr === 'dark' ? 'light' : 'dark';
        jtd.setTheme(next);
        localStorage.setItem('theme', next);
        btn.textContent = next === 'dark' ? '☀️' : '🌙';
      });
    }
  });
</script>

<div class="contacts-wrap">
  <h1 class="contacts-title">📬 Contatti</h1>
  <div class="contacts-divider"></div>

  <p class="contacts-lead">
    Se trovi un errore negli appunti o vuoi segnalare qualcosa, scrivimi qui:
  </p>

  <div class="contacts-actions">
    <a class="contact-btn"
       href="https://mail.google.com/mail/?view=cm&fs=1&to=marcomarino.ci@gmail.com&su=Segnalazione%20errore%20-%20Bibbie&body=Ciao%20Marco,%0D%0A%0D%0AHo%20trovato%20un%20errore%20in:%20%0D%0A%0D%0ADescrizione:%20"
       target="_blank" rel="noopener noreferrer">
      ✉️ Segnala un errore (Gmail)
    </a>

    <a class="contact-btn"
       href="https://instagram.com/marco.marino747"
       target="_blank" rel="noopener noreferrer">
      📸 Instagram: @marco.marino747
    </a>
  </div>

  <div class="contact-hint">
    Tip: se possibile, indica materia, pagina e un breve contesto — così lo correggo più velocemente.
  </div>
</div>
