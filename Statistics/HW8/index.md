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

# 🔗 Connections between HW4 and HW7

HW7 is closely related to the **Bernoulli process simulation** used in the Law of Large Numbers (LLN) assignment. In both cases, the system evolves through **independent repeated trials**, each resulting in either *success* or *failure*.

## 🎯 Core Analogy

| LLN Homework | Security Random Walk Homework |
|-------------|------------------------------|
| Each trial returns 1 (success) or 0 (failure) | Each week returns +1 (secure) or −1 (breached) |
| We studied the **relative frequency** of successes | We study the **cumulative score** over time |
| Success probability = $$p$$ | Security probability per week = $$q = (1 - p)^m$$ |
| LLN shows $$f(n) \to p$$ | Score distribution converges to a **Binomial-derived random walk** |

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🚶 Random Walk Interpretation

Define:

- $$X_i = +1$$ if the server remains secure in week \( i \)
- $$X_i = -1$$ if the server is breached in week \( i \)

Then the cumulative score after \( n \) weeks is:

$$
S_n = \sum_{i=1}^{n} X_i
$$

Let $$K$$ be the number of secure weeks. Then:

$$
S_n = (+1) \cdot K + (-1) \cdot (n-K) = 2K - n.
$$

So:

$$
K = \frac{S_n + n}{2}.
$$

Thus, the random walk is fully determined by the distribution of $$K$$, which follows a **Binomial** law:

$$
K \sim \text{Binomial}(n, q), \quad q = (1 - p)^m
$$

Therefore:

$$
S_n \sim 2 \cdot \text{Binomial}(n, q) - n.
$$

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

# 🔗 Mathematical relationships

## 🔢 Binomial Coefficients

The **Binomial coefficients** $$\binom{n}{K}$$ play a central role in both the LLN simulation and the random walk model. They represent the number of different ways to obtain exactly $$K$$ successful outcomes (or secure weeks) out of $$n$$ independent trials.

Mathematically, each term is defined as:

$$
\binom{n}{K} = \frac{n!}{K!(n-K)!}.
$$

In our security simulation, the term $$\binom{n}{K} q^K (1-q)^{n-K}$$ expresses the **probability** that the server remains secure for exactly $$K$$ weeks, given $$n$$ total updates and weekly security probability $$q = (1 - p)^m$$.

Each possible outcome of the random walk — each final score $$S_n = 2K - n$$ — therefore corresponds to a **combination** of $$K$$ upward steps (+1) and $$n - K$$ downward steps (−1). The binomial coefficient counts how many distinct trajectories lead to that final position. Thus, the entire random walk is **combinatorially governed** by the structure of the binomial coefficients.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🧩 Pascal’s Triangle

**Pascal’s Triangle** provides a recursive representation of the binomial coefficients. Each row $$n$$ corresponds to all coefficients $$\binom{n}{K}$$ for $$\( K = 0, 1, 2, \dots, n \)$$, and satisfies the recurrence relation:

$$
\binom{n}{K} = \binom{n-1}{K} + \binom{n-1}{K-1}.
$$

Graphically, each element is the **sum of the two entries above it**, forming the triangular pattern. This structure reflects exactly how **random walks evolve**:
- Each trajectory at step $$n$$ can be reached either from the “up” branch (a previous secure week) or from the “down” branch (a breach).  
- The total number of trajectories reaching a given score after $$n$$ steps equals the sum of the counts of trajectories leading to the two possible prior states.

Therefore, Pascal’s Triangle not only encodes binomial coefficients but also captures the **branching logic of stochastic processes** like Bernoulli trials and random walks.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## ✨ Binomial Expansion

The **Binomial Theorem** unifies these concepts by expressing the expansion of a power of a sum:

$$
(a + b)^n = \sum_{K=0}^{n} \binom{n}{K} a^K b^{n-K}.
$$

If we substitute $$a = q$$ and $$b = (1 - q)$$, we obtain:

$$
(q + (1 - q))^n = \sum_{K=0}^{n} \binom{n}{K} q^K (1 - q)^{n-K}.
$$

The right-hand side represents the **probability distribution** of the number of secure weeks $$K$$ — that is, the **Binomial distribution**. This direct connection shows that:
- The **random walk histogram** is a discrete representation of the binomial expansion.
- The **empirical frequencies** observed in the simulation approximate the theoretical binomial probabilities.

In large samples, these empirical frequencies converge to the theoretical values predicted by the binomial theorem, illustrating once again the **Law of Large Numbers** in action.

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
