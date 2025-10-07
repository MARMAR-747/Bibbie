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
  <span class="stats-title"></span>
  <span class="number-stream"></span>
</h1>

<p>📊 Benvenuto nella sezione dedicata agli <strong>homework</strong> e agli esercizi del corso di <em>Statistics</em> presso UniSapienza.</p>
<p>Qui troverai esercitazioni, soluzioni e approfondimenti progressivamente aggiornati durante il semestre.</p>

<script>
document.addEventListener('DOMContentLoaded', () => {
  const title = document.querySelector('.stats-title');
  const numbers = document.querySelector('.number-stream');
  if (!title || !numbers) return;

  // Effetto "digit typing" artigianale
  const text = "Statistics";
  let i = 0;
  function type() {
    if (i < text.length) {
      title.textContent += text.charAt(i);
      i++;
      setTimeout(type, 100); // velocità di scrittura
    }
  }
  type();

  // Effetto numeri in tempo reale
  function randomNumbers(length = 10) {
    return Array.from({ length }, () => Math.floor(Math.random() * 10)).join('');
  }
  function updateStream() {
    numbers.textContent = randomNumbers(10);
  }
  updateStream();
  setInterval(updateStream, 250);
});
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
