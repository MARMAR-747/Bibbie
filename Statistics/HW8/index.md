---
layout: default
title: HW8
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

# 🔗 Connections between HW4 (LLN) and HW7

HW7 is closely related to the **Bernoulli process simulation** used in the Law of Large Numbers (LLN) assignment.  
In both cases, the system evolves through **independent repeated trials**, each resulting in either *success* or *failure*.

## 🎯 Core Analogy

| LLN Homework | Security Random Walk Homework |
|-------------|------------------------------|
| Each trial returns 1 (success) or 0 (failure) | Each week returns +1 (secure) or −1 (breached) |
| We studied the **relative frequency** of successes | We study the **cumulative score** over time |
| Success probability = \( p \) | Security probability per week = \( q = (1 - p)^m \) |
| LLN shows \( f(n) \to p \) | Score distribution converges to a **Binomial-derived random walk** |

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🚶 Random Walk Interpretation

Define:

- \( X_i = +1 \) if the server remains secure in week \( i \)
- \( X_i = -1 \) if the server is breached in week \( i \)

Then the cumulative score after \( n \) weeks is:

$$
S_n = \sum_{i=1}^{n} X_i
$$

Let \( K \) be the number of secure weeks. Then:

$$
S_n = (+1) \cdot K + (-1) \cdot (n-K) = 2K - n.
$$

So,

$$
K = \frac{S_n + n}{2}.
$$

Thus, the random walk is fully determined by the distribution of \( K \), which follows a **Binomial** law:

$$
K \sim \text{Binomial}(n, q), \quad q = (1 - p)^m.
$$

Therefore:

$$
S_n \sim 2 \cdot \text{Binomial}(n, q) - n.
$$

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🔢 Binomial Coefficients and Pascal’s Triangle

The probability that exactly \( K \) weeks are secure is:

$$
P(K) = \binom{n}{K} q^K (1-q)^{n-K}.
$$

The coefficients \( \binom{n}{K} \) are the entries in **Pascal’s Triangle**, which satisfies the recurrence:

$$
\binom{n}{K} = \binom{n-1}{K} + \binom{n-1}{K-1}.
$$

This mirrors the idea that **each step in the random walk has two possible continuations**, exactly like Bernoulli trials.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## ✨ Binomial Expansion

Pascal’s triangle describes the coefficients in:

$$
(q + (1-q))^n = \sum_{K=0}^n \binom{n}{K} q^K (1-q)^{n-K}.
$$

This expression is **exactly the probability distribution** of the number of secure weeks.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🌀 Fibonacci Sequence Connection

If the random walk is **restricted** (e.g., it cannot go below zero), the number of valid paths satisfies:

$$
F_{n+1} = F_n + F_{n-1}.
$$

This is the same recurrence that defines the **Fibonacci sequence**.  
Such models appear in **absorbing barriers**, e.g., *a system that fails permanently once breached*.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🧭 Summary of Similarities and Differences

| Feature | LLN (Bernoulli Simulation) | Security Random Walk |
|--------|----------------------------|----------------------|
| Model | Bernoulli trials | Bernoulli trials → mapped to ±1 walk |
| Quantity studied | Frequency \( f(n) \) | Cumulative score \( S_n \) |
| Distribution | Binomial | Binomial → shifted and scaled |
| Behavior as \( n \to \infty \) | \( f(n) \to p \) | Score distribution approaches Normal (CLT) |
| Visualization | Convergence to mean | Random walk spreading and histogram convergence |

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
