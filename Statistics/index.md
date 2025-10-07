---
layout: default
title: Statistics
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Algebra/" class="nav-button left">⬅️ Algebra</a>
  <a href="/Bibbie/Elettrotecnica/" class="nav-button right">➡️ Elettrotecnica</a>
</div>

<br>

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

<h1 class="stats-header">
  <span id="typed-stats"></span>
  <span class="number-stream"></span>
</h1>

<p>📊 Benvenuto nella sezione dedicata agli <strong>homework</strong> e agli esercizi del corso di <em>Statistics</em> presso UniSapienza.</p>
<p>Qui troverai esercitazioni, soluzioni e approfondimenti progressivamente aggiornati durante il semestre.</p>

<!-- Carico Typed con defer così è pronto prima del nostro init -->
<script src="https://cdn.jsdelivr.net/npm/typed.js@2.0.12" defer></script>

<!-- Init robusto: attende DOM + presenza di window.Typed -->
<script defer>
  (function () {
    function start() {
      if (!document.querySelector('#typed-stats')) return;           // l'H1 esiste?
      if (typeof window.Typed === 'undefined') {                     // typed non ancora pronto? riprova
        return setTimeout(start, 50);
      }

      // Effetto digit typing
      new Typed('#typed-stats', {
        strings: ['Statistics', 'Data Analysis', 'Homework & Insights 📊'],
        typeSpeed: 60,
        backSpeed: 30,
        loop: true,
        smartBackspace: true
      });

      // Effetto numeri dinamici
      const el = document.querySelector('.number-stream');
      if (el) {
        const randomNumbers = (len = 10) =>
          Array.from({ length: len }, () => Math.floor(Math.random() * 10)).join('');
        const updateStream = () => { el.textContent = randomNumbers(10); };
        updateStream();
        setInterval(updateStream, 250);
      }
    }

    // Parto quando il DOM è pronto
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', start);
    } else {
      start();
    }
  })();
</script>

---
🔒 Questo materiale è rilasciato sotto licenza [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Ultimo aggiornamento: {{ site.time | date: "%d/%m/%Y" }}

{% assign ordine = site.materie_order %}
{% assign idx = page.order_index | plus: 0 %}

<div style="margin-top: 3rem; display: flex; justify-content: space-between; font-weight: bold;">
  {% if idx > 0 %}
    {% assign precedente = ordine[idx | minus: 1] %}
    <a href="/{{ precedente | replace: ' ', '%20' }}/">⟵ {{ precedente }}</a>
  {% else %}
    <span></span>
  {% endif %}

  {% if idx < ordine.size | minus: 1 %}
    {% assign successiva = ordine[idx | plus: 1] %}
    <a href="/{{ successiva | replace: ' ', '%20' }}/">{{ successiva }} ⟶</a>
  {% else %}
    <span></span>
  {% endif %}
</div>
