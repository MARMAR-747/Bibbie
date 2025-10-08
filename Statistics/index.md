---
layout: default
title: Statistics
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Statistics/" class="nav-button right">➡️ Cryptography</a>
</div>

<br>

<script>
  document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('theme-toggle');
    const saved = localStorage.getItem('theme');
    if (saved && window.jtd) {
      jtd.setTheme(saved);
      if (btn) btn.textContent = saved === 'dark' ? '☀️' : '🌙';
    }
    if (btn) {
      btn.addEventListener('click', () => {
        if (!window.jtd) return;
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
document.addEventListener('DOMContentLoaded', () => {
  // piccolo ritardo per essere certi che il DOM sia completo
  setTimeout(() => {
    const el = document.getElementById('stats-numbers');
    if (!el) return;

    function randomNumbers(length = 10) {
      let nums = '';
      for (let i = 0; i < length; i++) {
        nums += Math.floor(Math.random() * 10);
      }
      return nums;
    }

    function updateStream() {
      el.textContent = randomNumbers(10);
    }

    updateStream();              // imposta la prima volta
    setInterval(updateStream, 250);  // aggiorna ogni 250 ms
  }, 200);
});
</script>

<h1 class="stats-header">
  <!-- Typing solo CSS: il testo è già nel DOM -->
  <span class="stats-title" data-text="Statistics">Statistics</span>
  <span class="number-stream" id="stats-numbers">0000000000</span>
</h1>

<p>📊 Benvenuto nella sezione dedicata agli <strong>homework</strong> e agli esercizi del corso di <em>Statistics</em> presso UniSapienza.</p>
<p>Qui troverai esercitazioni, soluzioni e approfondimenti progressivamente aggiornati durante il semestre.</p>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

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
