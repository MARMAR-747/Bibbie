---
layout: default
title: Elettrotecnica
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Teoria Dei Segnali/" class="nav-button left">⬅️ Teoria Dei Segnali</a>
  <a href="/Bibbie/Elettronica/" class="nav-button right">➡️ Elettronica</a>
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

# 📘 Elettrotecnica
---

## Overview:

<table>
  <tr>
    <td><strong>Prof.</strong></td>
    <td>
      Antonino Imburgia
    </td>
  </tr>
  <tr>
    <td><strong>Descrizione</strong></td>
    <td>
      Elettrotecnica, nonostante valga solo 6 CFU, presenta un programma tanto corposo quanto accessibile. Fortunatamente, il professor Imburgia si distingue per        la sua chiarezza espositiva ed è uno dei pochi docenti che propone esami realmente coerenti con quanto svolto in aula. Per lo studio della teoria, è               consigliato il file “LA BIBBIA DI ELETTROTECNICA”, che prende spunto direttamente dagli appunti scritti in classe dal professore offrendo una panoramica           accurata di tutti gli argomenti trattati (l’unico difetto di quest'ultimo è infatti la grafia, a volte poco leggibile). Per quanto riguarda gli esercizi, il       riferimento principale è il file “LE RIVELAZIONI DI ELETTROTECNICA”, che contiene: <br>
      - Le soluzioni dettagliate degli esercizi dell’eserciziario ufficiale del professore. <br>
      - Una raccolta di esercizi svolti in aula. <br>
      - Vecchi appelli d’esame.
    </td>
  </tr>
  <tr>
    <td><strong>Modalità d'esame</strong></td>
    <td>
      
    </td>
  </tr>
  <tr>
    <td><strong>Difficoltà</strong></td>
    <td>🔸 Media 💀💀💀⚪⚪</td>
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
      <td>LA BIBBIA DI ELETTROTECNICA</td>
      <td style="text-align: center;">Teoria</td>
      <td style="text-align: center;">91</td>
      <td style="text-align: center;">🔄</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
    <tr>
      <td>
        <a href="../Elettrotecnica/LE RIVELAZIONI DI ELETTROTECNICA.pdf" target="_blank">LE RIVELAZIONI DI ELETTROTECNICA</a>
      </td>
      <td style="text-align: center;">Esercizi</td>
      <td style="text-align: center;">245</td>
      <td style="text-align: center;">✅</td>
      <td style="text-align: center;">☆☆☆☆☆ (0)</td>
    </tr>
  </tbody>
</table>

<a href="https://forms.gle/FdiNix35fyMHXay76" target="_blank" rel="noopener noreferrer">
  Valuta il materiale di Elettrotecnica ⭐
</a> <br><br>

📬 Per segnalazione di eventuali errori:  
&emsp;&nbsp;&nbsp;[marcomarino.ci@gmail.com](mailto:marcomarino.ci@gmail.com)

---
🔒 Questo materiale è rilasciato sotto licenza [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Ultimo aggiornamento: {{ site.time | date: "%d/%m/%Y" }}
