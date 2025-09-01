---
layout: default
title: Il mio curriculum
nav_order: 2
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

# 👱🏻💻 Il mio curriculum
---

<iframe 
  src="/assets/pdf/CV Marco Marino.pdf" 
  width="100%" 
  height="900px" 
  style="border:none; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
</iframe>
