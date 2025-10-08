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

<h1 class="stats-header">
  <span class="stats-icon">📊</span>
  <span class="stats-title" data-text="Statistics">Statistics</span>
  <span class="number-stream" id="stats-numbers"></span>
</h1>

<p> Welcome to the <em>Statistics</em> course <strong>assignments</strong> section.</p>

<h2>📋 Homework</h2>

<table>
  <thead>
    <tr>
      <th style="width: 2%; text-align: center;">#</th>
      <th style="width: 69%; text-align: center;">Title</th>
      <th style="width: 2%; text-align: center;">Link</th>
      <th style="width: 2%; text-align: center;">Deadline</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align: center;">1</td>
      <td style="text-align: left;">What is statistics and why can it be useful for cybersecurity?</td>
      <td style="text-align: center;"><a href="Bibbie/Statistics/HW1">📄 HW</a></td>
      <td style="text-align: center;">09/10/25</td>
    </tr>
    <tr>
      <td style="text-align: center;">2</td>
      <td style="text-align: left;">TBD</td>
      <td style="text-align: center;">TBD</td>
      <td style="text-align: center;">TBD</td>
    </tr>
    <tr>
      <td style="text-align: center;">3</td>
      <td style="text-align: left;">TBD</td>
      <td style="text-align: center;">TBD</td>
      <td style="text-align: center;">TBD</td>
    </tr>
  </tbody>
</table>
<hr style="margin-top: 2rem; margin-bottom: 1rem;">

🔒 All material is released under license [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Last update: {{ site.time | date: "%d/%m/%Y" }}

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

<script src="{{ 'Statistics/assets/js/stats.js' | relative_url }}"></script>
