---
layout: default
title: La mia build
nav_order: 7
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

# 💻 La mia build
---

<img src="{{ '/assets/images/CV1.png' | relative_url }}" width="100%">  
<img src="{{ '/assets/images/CV2.png' | relative_url }}" width="100%">  
<img src="{{ '/assets/images/CV3.png' | relative_url }}" width="100%">
