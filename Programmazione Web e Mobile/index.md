---
layout: default
title: Programmazione Web e Mobile
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Ingegneria Del Software/" class="nav-button left">⬅️ Ingegneria del Software</a>
  <a class="nav-button right">➡️ IN PROGRESS</a>
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

# 📘 Programmazione Web e Mobile
---

## Overview:

<table>
  <tr>
    <td><strong>Prof.</strong></td>
    <td>
      Federico Concone
    </td>
  </tr>
  <tr>
    <td><strong>Menzioni speciali</strong></td>
    <td>Si ringrazia in modo speciale un eroe senza il quale il progetto non avrebbe mai visto luce in tempo:<br>
    - <strong>Simone Girgenti</strong> (anche detto <strong>Simeone</strong>), colui che non ha vacillato di fronte al temibile Kotlin
      e si è fatto carico di portare avanti il lavoro quando non sono stato disponibile.<br>
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
      Il <strong>progetto</strong> consta
      <br><br>
      Lo <strong>scritto</strong>
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

<img src="{{ '/assets/images/CopertinaRAD.png' | relative_url }}" width="80">
<img src="{{ '/assets/images/CopertinaODD.png' | relative_url }}" width="80">
<img src="{{ '/assets/images/CopertinaSDD.png' | relative_url }}" width="80">

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
        <a href="../Ingegneria Del Software/RAD.pdf" target="_blank">RAD</a>
      </td>
      <td style="text-align: center;">Progetto</td>
      <td style="text-align: center;">193</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
    <tr>
      <td>
        <a href="../Ingegneria Del Software/ODD.pdf" target="_blank">ODD</a>
      </td>
      <td style="text-align: center;">Progetto</td>
      <td style="text-align: center;">22</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
    <tr>
      <td>
        <a href="../Ingegneria Del Software/SDD.pdf" target="_blank">SDD</a>
      </td>
      <td style="text-align: center;">Progetto</td>
      <td style="text-align: center;">33</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
  </tbody>
</table>

<a href="https://docs.google.com/forms/d/e/1FAIpQLSdtodu3VPHwG825FNluwVazuPSc_mzX1lgQC1v22RndIOVhaQ/viewform" target="_blank" rel="noopener noreferrer">
  Valuta il materiale di Programmazione Web e Mobile ⭐
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
