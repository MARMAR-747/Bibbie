---
layout: default
title: HW10
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Statistics/" class="nav-button left">⬅️ Statistics</a>
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

# 🔗 Connections between HW4 and HW7

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🌀 Fibonacci Sequence Connection

When a random walk is **restricted** — for example, when the system cannot go below zero (an “absorbing boundary” scenario) — the number of valid trajectories becomes related to the **Fibonacci sequence**. To understand why, consider a process where the system can take one or two steps at a time but cannot cross the lower boundary. The number of distinct paths to reach a given point satisfies the recurrence:

$$
F_{n+1} = F_n + F_{n-1},
$$

with initial conditions $$F_1 = 1, F_2 = 1$$.

This is precisely the **Fibonacci recurrence**, and it emerges in constrained random walks, queueing theory, and network reliability models.  
Each new path can be constructed either by:
- Extending a path that ended one step earlier  
- Extending a path that ended two steps earlier

Hence, Fibonacci-like growth appears naturally when **combinatorial restrictions** are imposed on stochastic or recursive systems — a fascinating intersection between **combinatorics**, **recurrence relations**, and **probability theory**.

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
