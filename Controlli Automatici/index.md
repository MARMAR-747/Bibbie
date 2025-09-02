---
layout: default
title: Controlli Automatici
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Elettronica/" class="nav-button left">⬅️ Elettronica</a>
  <a href="/Bibbie/Ingegneria Del Software/" class="nav-button right">➡️ Ingegneria Del Software</a>
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

# 📘 Controlli Automatici
---

## Overview:

<table>
  <tr>
    <td><strong>Prof.</strong></td>
    <td>
      Adriano Fagiolini
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
      Il <strong>progetto</strong> consta di
      <br><br>
      L'<strong>orale</strong> consiste
    </td>
  </tr>
  <tr>
    <td><strong>Difficoltà</strong></td>
    <td>🔺 Medio-Alta 💀💀💀💀⚪</td>
  </tr>
  <tr>
    <td><strong>Domande frequenti</strong></td>
    <td>
      
    </td>
  </tr>
  <tr>
    <td><strong>Tips</strong></td>
    <td>
      
    </td>
  </tr>
</table>

## Materiale disponibile:

<img src="{{ '/assets/images/CopertinaCAUTO.png' | relative_url }}" width="80">

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
        <a href="../Controlli Automatici/Progetto_CA2315.pdf" target="_blank">SUBMARINE: ROV</a>
      </td>
      <td style="text-align: center;">Progetto</td>
      <td style="text-align: center;">36</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
  </tbody>
</table>

<a href="https://forms.gle/iqwHpBBBcairaDzy7" target="_blank" rel="noopener noreferrer">
  Valuta il materiale di Controlli Automatici ⭐
</a> <br><br>

📬 Per segnalazione di eventuali errori:  
&emsp;&nbsp;&nbsp;[marcomarino.ci@gmail.com](mailto:marcomarino.ci@gmail.com)

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
