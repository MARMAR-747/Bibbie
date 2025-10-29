---
layout: default
title: HW3
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

# The Law of Large Numbers (LLN)

The **Law of Large Numbers (LLN)** is a fundamental theorem in probability theory stating that the **average of a large number of independent and identically distributed (i.i.d.) random variables** tends to approach the **expected value** of the underlying distribution as the number of trials increases.

In simpler terms:  
> When an experiment is repeated many times, the **empirical (observed) frequency** of an event approaches its **theoretical probability**.

Formally, if \( X_1, X_2, \dots, X_n \) are i.i.d. random variables with expected value \( E[X_i] = \mu \), then:

\[
\frac{1}{n} \sum_{i=1}^{n} X_i \xrightarrow[n \to \infty]{} \mu
\]

This convergence can be:
- **Weak (WLLN)**: convergence in probability,
- **Strong (SLLN)**: convergence almost surely (with probability 1).

### Example
Consider a sequence of Bernoulli trials with success probability \( p \).  
Each trial returns:
\[
X_i = 
\begin{cases}
1, & \text{if success} \\
0, & \text{if failure}
\end{cases}
\]

The relative frequency after \( n \) trials is:
\[
f(n) = \frac{1}{n}\sum_{i=1}^{n} X_i
\]

According to the LLN:
\[
f(n) \to p \quad \text{as } n \to \infty
\]

### Intuitive meaning
At the beginning (small \( n \)), the relative frequency \( f(n) \) fluctuates significantly.  
As \( n \) grows, the fluctuations diminish, and \( f(n) \) stabilizes near the true probability \( p \).  
This is the mathematical foundation for why empirical averages and experimental data become reliable with enough observations.

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
