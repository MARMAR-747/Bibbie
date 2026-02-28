---
layout: default
title: Network Infrastructures
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/<NEXT>/" class="nav-button right">➡️ <NEXT_LABEL></a>
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

<div class="course-header">
  <h1 class="course-title">🌐 Network Infrastructures</h1>
  <div class="title-divider-soft"></div>
</div>

<div class="section-title">Overview</div>

<table class="table-elegant kv-table">
  <tr>
    <td><strong>Professors</strong></td>
    <td>Francesca Cuomo (Access Networks), Marco Polverini (Transport Networks)</td>
  </tr>
  <tr>
    <td><strong>Description</strong></td>
    <td><DESCRIPTION></td>
  </tr>
  <tr>
    <td><strong>Exam type</strong></td>
    <td>Homework using kathara + two intermediate tests + written exam</td>
  </tr>
  <tr>
    <td><strong>Difficulty</strong></td>
    <td><DIFFICULTY></td>
  </tr>
  <tr>
    <td><strong>Tips</strong></td>
    <td><TIPS></td>
  </tr>
</table>

<div class="section-title">Available Material</div>
<div class="material-table-wrap">
  <table class="table-elegant">
    <thead>
      <tr>
        <th style="width: 69%; text-align: center;">Title</th>
        <th style="width: 2%; text-align: center;">Type</th>
        <th style="width: 2%; text-align: center;">Pages</th>
        <th style="width: 2%; text-align: center;">Status</th>
        <th style="width: 25%; text-align: center;">Score</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><a class="file-link" href="<PDF_LINK>" target="_blank"><PDF_TITLE></a></td>
        <td style="text-align: center;"><MAT_TYPE></td>
        <td style="text-align: center;"><PAGES></td>
        <td style="text-align: center;"><STATUS></td>
        <td style="text-align: center;"><SCORE></td>
      </tr>
    </tbody>
  </table>
</div>

<div class="action-row">
  <a class="btn-soft" href="<RATE_FORM_LINK>" target="_blank" rel="noopener noreferrer">
    ⭐ Rate the material of <COURSE_NAME>
  </a>

  <a class="btn-soft-secondary" href="mailto:marcomarino.ci@gmail.com">
    📬 Report errors
  </a>
</div>

<div class="page-footer-meta">
  <div class="meta-item">
    🔒 <span>All material is released under license</span>
    <a href="https://creativecommons.org/licenses/by-nc-nd/4.0/" target="_blank" rel="noopener noreferrer">CC BY-NC-ND 4.0</a>.
  </div>

  <div class="meta-item">
    🔗 <span>Last update:</span> <strong>{{ site.time | date: "%d/%m/%Y" }}</strong>
  </div>
</div>

{% assign ordine = site.materie_order %}
{% assign idx = page.order_index | plus: 0 %}

<div class="nav-prev-next">
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
