---
layout: default
title: Home
nav_order: 1
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

<script>
  // Se l'utente cambia lingua, salvala
  document.querySelectorAll('.lang-button').forEach(btn => {
    btn.addEventListener('click', () => {
      localStorage.setItem('lang', btn.getAttribute('aria-label').includes('English') ? 'en' : 'it');
    });
  });

  // Se clicca su "Home", usa la lingua salvata
  document.querySelectorAll('a[href="/Bibbie/index.html"]').forEach(link => {
    link.addEventListener('click', e => {
      const lang = localStorage.getItem('lang');
      if (lang === 'en') {
        e.preventDefault();
        window.location.href = '/Bibbie/index_en.html';
      }
    });
  });
</script>

<script>
document.addEventListener('DOMContentLoaded', () => {
  const counter = document.getElementById('pdf-count');
  if (!counter) return;
  
  const target = parseInt(counter.dataset.target);
  if (isNaN(target)) return;

  let count = 0;
  const step = 1;
  const delay = 400;

  const update = () => {
    if (count >= target) {
      counter.textContent = target;
      return;
    }
    count += step;
    counter.textContent = count;
    setTimeout(update, delay);
  };

  update();
});
</script>

<h2>📘 <span id="typed"></span></h2>

<script src="https://cdn.jsdelivr.net/npm/typed.js@2.0.12"></script>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    new Typed('#typed', {
      strings: [
        'Le Bibbie di Ingegneria Informatica',
        'Appunti universitari in versione digitale',
        'Università degli Studi di Palermo e di Roma'
      ],
      typeSpeed: 50,
      backSpeed: 25,
      loop: true
    });
  });
</script>
---
Quella di **Ingegneria Informatica** è universalmente riconosciuta come una delle **facoltà più complesse** da affrontare.
Ogni giorno ci si confronta con problemi di **notevole difficoltà** — uno tra tutti: l’esame di **Teoria dei Segnali**.

Ma cosa succede se non si ha a disposizione del **buon materiale per studiare**?

Ho iniziato a scrivere appunti **per me stesso**, come esercizio di memoria e per allenarmi a scrivere al PC (soprattutto su Word).
Con il tempo, però, mi sono reso conto che **ore e ore di lavoro** non potevano essere lasciate in un cassetto: avevo trovato un nuovo scopo, ovvero
**aiutare chi, come me, trovava difficoltà a orientarsi tra le dispense dei docenti**.

**Chiariamoci**: non considero i miei appunti "migliori" rispetto a quelli ufficiali.
Credo però che, nell’eccessivo formalismo accademico, si perda talvolta il contatto concreto con gli studenti.

### 📖 Perché “Le Bibbie”?
Perché anche le materie da **6 CFU**, all’apparenza innocue, possono nascondere **programmi vastissimi**. Il nome è ironico, ma anche 
simbolico: questi appunti sono diventati per me — e spero anche per voi — **una guida completa e affidabile**.

Di seguito troverete infatti **tutto il materiale** da me prodotto durante la **triennale in Ingegneria Informatica** all’**Università degli Studi di Palermo**, 
raccolto **per anno e per materia**.

<div class="counter-container">
  <div class="counter-box">
    <span id="pdf-count" class="counter" data-target="14">0</span>
    <p data-i18n="counter"></p>
    <p>📚 PDF disponibili</p>
  </div>
</div>

<style>
.counter-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
  margin-top: 2rem;
}
.counter-box {
  text-align: center;
  background-color: var(--card-background-color, var(--body-background-color));
  padding: 20px 30px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  transition: background-color 0.3s ease;
}
.counter-box p {
  margin: 0.5rem 0 0;
  color: var(--body-text-color);
}
.counter {
  font-size: 2.5rem;
  font-weight: bold;
  color: var(--link-color);
  display: block;
}
</style>

<!-- ===== CARD GRID: TRIENNALE ===== -->
<h2>📂<img src="{{ '/assets/images/IT_flag.png' | relative_url }}" width="30" style="vertical-align: middle;"> Materiale (Triennale in Ingegneria Informatica - UniPa)</h2>

<div class="year-card">
  <span class="year-number">1°</span>
  <div>
    <div class="year-main">Primo anno</div>
    <div class="year-sub">Triennale</div>
  </div>
</div>
<div class="cards-grid">
  <a class="course-card" href="{{ '/Algebra/' | relative_url }}">
    <div class="card-title">Algebra</div>
    <div class="card-meta">6 CFU</div>
  </a>
</div>

<div class="year-card">
  <span class="year-number">2°</span>
  <div>
    <div class="year-main">Secondo anno</div>
    <div class="year-sub">Triennale</div>
  </div>
</div>
<div class="cards-grid">
  <a class="course-card" href="{{ '/Teoria%20Dei%20Segnali/' | relative_url }}">
    <div class="card-title">Teoria dei Segnali</div>
    <div class="card-meta">9 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Elettrotecnica/' | relative_url }}">
    <div class="card-title">Elettrotecnica</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Metodi%20matematici%20e%20numerici/' | relative_url }}">
    <div class="card-title">Metodi matematici e numerici</div>
    <div class="card-meta">9 CFU</div>
  </a>
</div>

<div class="year-card">
  <span class="year-number">3°</span>
  <div>
    <div class="year-main">Terzo anno</div>
    <div class="year-sub">Triennale</div>
  </div>
</div>
<div class="cards-grid">
  <a class="course-card" href="{{ '/Reti%20di%20calcolatori/' | relative_url }}">
    <div class="card-title">Reti di calcolatori</div>
    <div class="card-meta">9 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Elettronica/' | relative_url }}">
    <div class="card-title">Elettronica</div>
    <div class="card-meta">9 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Controlli%20Automatici/' | relative_url }}">
    <div class="card-title">Controlli Automatici</div>
    <div class="card-meta">9 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Ingegneria%20Del%20Software/' | relative_url }}">
    <div class="card-title">Ingegneria del Software</div>
    <div class="card-meta">9 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Programmazione%20Web%20e%20Mobile/' | relative_url }}">
    <div class="card-title">Programmazione Web e Mobile</div>
    <div class="card-meta">9 CFU</div>
  </a>
</div>

<!-- ===== CARD GRID: MAGISTRALE ===== -->
<h2>📂<img src="{{ '/assets/images/UK_flag.png' | relative_url }}" width="30" style="vertical-align: middle;"> Materiale (Magistrale in Cybersecurity - UniSapienza)</h2>

<div class="year-card">
  <span class="year-number">1°</span>
  <div>
    <div class="year-main">Primo anno</div>
    <div class="year-sub">Magistrale</div>
  </div>
</div>
<div class="cards-grid">
  <a class="course-card" href="{{ '/Statistics/' | relative_url }}">
    <div class="card-title">Statistics</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Cryptography/' | relative_url }}">
    <div class="card-title">Cryptography</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Distributed%20Systems/' | relative_url }}">
    <div class="card-title">Distributed Systems</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Network%20Infrastructures/' | relative_url }}">
    <div class="card-title">Network Infrastructures</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Ethical%20Hacking/' | relative_url }}">
    <div class="card-title">Ethical Hacking</div>
    <div class="card-meta">9 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Practical%20Network%20Defense/' | relative_url }}">
    <div class="card-title">Practical Network Defense</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Economics%20of%20Technology%20and%20Management/' | relative_url }}">
    <div class="card-title">Economics of Technology and Management</div>
    <div class="card-meta">6 CFU</div>
  </a>

  <a class="course-card" href="{{ '/Cyber%20and%20Computer%20Law/' | relative_url }}">
    <div class="card-title">Cyber and Computer Law</div>
    <div class="card-meta">6 CFU</div>
  </a>
</div>

---
🔒 Tutto il materiale è rilasciato sotto licenza [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Ultimo aggiornamento: {{ site.time | date: "%d/%m/%Y" }}
