---
layout: default
title: Economics of Technology and Management
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

# 📘 Economics of Technology and Management

## Overview:

<table>
  <tr>
    <td><strong>Prof.</strong></td>
    <td>
      Idiano D'Adamo
    </td>
  </tr>
  <tr>
    <td><strong>Descrizione</strong></td>
    <td>
      
    </td>
  </tr>
  <tr>
    <td><strong>Modalità d'esame</strong></td>
    <td>
      
    </td>
  </tr>
  <tr>
    <td><strong>Difficoltà</strong></td>
    <td>🟢 Medio-Bassa 💀💀⚪⚪⚪</td>
  </tr>
  <tr>
    <td><strong>Tips</strong></td>
    <td>
      
    </td>
  </tr>
</table>

## Materiale disponibile:

<img src="{{ '/assets/images/CopertinaRAD.png' | relative_url }}" width="80">

<table>
  <thead>
    <tr>
      <th style="width: 69%; text-align: center;">Titolo</th>
      <th style="width: 2%; text-align: center;">Tipologia</th>
      <th style="width: 2%; text-align: center;">Pagine</th>
      <th style="width: 2%; text-align: center;">Status</th>
      <th style="width: 25%; text-align: center;">Score</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <a href="../Algebra/Il Vangelo di Algebra.pdf" target="_blank">IL VANGELO DI ALGEBRA</a>
      </td>
      <td style="text-align: center;">Teoria/Esercizi</td>
      <td style="text-align: center;">157</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
    <tr>
      <td>
        <a href="../Algebra/Breviario delle nozioni di Algebra.pdf" target="_blank">BREVIARIO DELLE NOZIONI DI ALGEBRA</a>
      </td>
      <td style="text-align: center;">Teoria</td>
      <td style="text-align: center;">4</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
    <tr>
      <td>
        <a href="../Algebra/Vecchi compiti di Algebra.pdf" target="_blank">VECCHI COMPITI DI ALGEBRA</a>
      </td>
      <td style="text-align: center;">Esercizi</td>
      <td style="text-align: center;">41</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
  </tbody>
</table>

<a href="https://forms.gle/SPKm8SfiY1dxVx2KA" target="_blank" rel="noopener noreferrer">
  Valuta il materiale di Economics of Technology and Management ⭐
</a> <br><br>

📬 Per segnalazione di eventuali errori:  
&emsp;&nbsp;&nbsp;[marcomarino.ci@gmail.com](mailto:marcomarino.ci@gmail.com)

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
