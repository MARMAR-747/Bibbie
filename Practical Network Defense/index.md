---
layout: default
title: Practical Network Defense
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Economics of Technology and Management/" class="nav-button right">➡️ ETM</a>
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
  <h1 class="course-title">💰 Economics of Technology and Management</h1>
  <div class="title-divider-soft"></div>
</div>

<div class="section-title">Overview</div>

<table class="table-elegant kv-table">
  <tr>
    <td><strong>Professor</strong></td>
    <td>
      Idiano D'Adamo
    </td>
  </tr>
  <tr>
    <td><strong>Description</strong></td>
    <td>
      The course provides the essential tools to analyze firms’ decision-making processes, focusing on microeconomic foundations, organizational structures, technological innovation strategies, investment evaluation, and financial statement analysis.
    </td>
  </tr>
  <tr>
    <td><strong>Exam type</strong></td>
    <td>
      Project (in solo or in pairs) + Oral
    </td>
  </tr>
  <tr>
    <td><strong>Difficulty</strong></td>
    <td>🟢 Medium-Low 💀💀⚪⚪⚪</td>
  </tr>
  <tr>
    <td><strong>Tips</strong></td>
    <td>
      
    </td>
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
      <td>
        <a class="file-link" href="../Economics of Technology and Management/Bible of Economics of Technology and Management.pdf" target="_blank">BIBLE OF ETM</a>
      </td>
      <td style="text-align: center;">Theory/Exercises</td>
      <td style="text-align: center;">31</td>
      <td style="text-align: center;">Updated 28/02/26</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
  </tbody>
</table>
</div>

<div class="action-row">
  <a class="btn-soft" href="https://forms.gle/sVfWQ4wJ9Fomwq1E9" target="_blank" rel="noopener noreferrer">
    ⭐ Rate the material of Economics of Technology and Management
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
