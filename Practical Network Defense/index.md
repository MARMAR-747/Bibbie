---
layout: default
title: Practical Network Defense
nav_exclude: true
---

<meta http-equiv="refresh" content="10">

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
  <h1 class="course-title">💻 Practical Network Defense</h1>
  <div class="title-divider-soft"></div>
</div>

<div class="section-title">Overview</div>

<table class="table-elegant kv-table">
  <tr>
    <td><strong>Professor</strong></td>
    <td>
      Angelo Spognardi
    </td>
  </tr>
  <tr>
    <td><strong>Description</strong></td>
    <td>
      The course introduces the fundamental methodologies and tools for protecting computer networks, with strong emphasis on practical application. It covers network attack mechanisms, intrusion detection, traffic monitoring, vulnerability assessment, firewall configuration, and the design of defense-in-depth strategies.
    </td>
  </tr>
  <tr>
    <td><strong>Exam type</strong></td>
    <td>
      Homeworks (up to 4 people in a group) + Written exam
    </td>
  </tr>
  <tr>
    <td><strong>Difficulty</strong></td>
    <td>🔸 Medium 💀💀💀⚪⚪</td>
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
        <a class="file-link" href="../Practical Network Defense/Bible of Practical Network Defense.pdf" target="_blank">BIBLE OF PND</a>
      </td>
      <td style="text-align: center;">Theory/Exercises</td>
      <td style="text-align: center;">86</td>
      <td style="text-align: center;">Updated 08/03/26</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
  </tbody>
</table>
</div>

<div class="action-row">
  <a class="btn-soft" href="https://forms.gle/J7j6xVKH1AD9NF4x9" target="_blank" rel="noopener noreferrer">
    ⭐ Rate the material of Practical Network Defense
  </a>

  <a class="btn-soft-secondary"
   href="https://mail.google.com/mail/?view=cm&fs=1&to=marcomarino.ci@gmail.com&su=Report%20Error%20-%20Practical%20Network%20Defense&body=Please%20describe%20the%20issue%20here:"
   target="_blank">
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
