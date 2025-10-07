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

## 📂 Materiale (Triennale in Ingegneria Informatica - UniPa) <img src="{{ '/assets/images/IT_flag.png' | relative_url }}" width="30" style="vertical-align: middle;">

<h2 style="display: inline-flex; align-items: center; gap: 8px;">
  📂 Materiale (Triennale in Ingegneria Informatica - UniPa)
  <img src="{{ '/assets/images/IT_flag.png' | relative_url }}" width="30" alt="IT">
</h2>

### ➀ Primo anno

- [Algebra](Algebra/) (6 CFU)

### ➁ Secondo anno

- [Teoria dei segnali](Teoria Dei Segnali/) (9 CFU)
- [Elettrotecnica](Elettrotecnica/) (6 CFU)
- [Metodi matematici e numerici] (9 CFU)

### ➂ Terzo anno

- [Reti di calcolatori] (9 CFU)
- [Elettronica](Elettronica/) (9 CFU)
- [Controlli Automatici](Controlli Automatici/) (9 CFU)
- [Ingegneria Del Software](Ingegneria Del Software/) (9 CFU)
- [Programmazione Web e Mobile](Programmazione Web e Mobile/) (9 CFU)

<img src="{{ '/assets/images/UK_flag.png' | relative_url }}" width="40">
## 📂 Materiale (Magistrale in Cybersecurity - UniSapienza)
---
### ➀ Primo anno

- [Statistics] (6 CFU)
- [Cryptography] (6 CFU)
- [Distributed Systems] (6 CFU)
- [Network Infrastructures] (6 CFU)

---
🔒 Tutto il materiale è rilasciato sotto licenza [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Ultimo aggiornamento: {{ site.time | date: "%d/%m/%Y" }}
