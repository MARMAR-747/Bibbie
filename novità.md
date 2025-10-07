---
layout: default
title: Novità
nav_order: 3
---

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

# 🆕 Aggiornamenti del materiale
---

Benvenuto nella sezione **Novità**, dove puoi trovare tutti gli aggiornamenti relativi a questo sito web.  
Ogni aggiornamento riporta la **data di pubblicazione**, il **contenuto aggiunto o migliorato**, e – se disponibile – un link diretto al materiale aggiornato.

## 📅 Ultimi aggiornamenti

### 🗓️ 6 Ottobre 2025
- Aggiunta la **modalità notte** in tutto il sito 🌙  
- Implementato il **cambio lingua (IT/EN)** nella home page 🇮🇹🇬🇧  

---

### 🗓️ 7 Ottobre 2025
- Aggiornato il PDF di **Teoria dei Segnali** (capitolo sui sistemi LTI)  
- Corretto un errore nella formula di convoluzione  
- Aggiunte nuove spiegazioni sulle **risposte impulsive**  

---

### 🗓️ 28 Settembre 2025
- Creata la nuova sezione **Elettronica** con 9 CFU completi  
- Migliorato il layout mobile per una migliore leggibilità 📱  

---
