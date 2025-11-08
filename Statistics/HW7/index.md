---
layout: default
title: HW7
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

# 🛡️ Random Security Updates and Attack Model

A server receives weekly security updates across \( n \) weeks.  
Each week, there are \( m \) independent attackers.  
Every attacker can successfully breach the system with probability \( p \).

We assume:
- If **at least one attacker succeeds**, the server is breached → score = **-1**
- If **all attackers fail**, the server stays secure → score = **+1**

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🎲 Probability of Security or Breach

Since attackers act independently:

- Probability that one attacker fails: $$(1-p)$$
- Probability that all \( m \) attackers fail: $$P(\text{secure}) = (1 - p)^m$$  
- Therefore:
  $$
  P(\text{breach}) = 1 - (1 - p)^m
  $$

Let
$$
X_i = \begin{cases}
+1 & \text{if week } i \text{ is secure} \\
-1 & \text{if week } i \text{ is breached}
\end{cases}
$$

Then the **cumulative security score** after \( n \) weeks is:

$$
S_n = \sum_{i=1}^{n} X_i
$$

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🪜 Connection to Random Walks

This process defines a **biased random walk**:
- Step **+1** with probability $$(1 - p)^m$$
- Step **−1** with probability $$1 - (1 - p)^m$$

If we denote:

$$
q = (1 - p)^m
$$

then:

$$
P(X_i = +1) = q, \quad P(X_i = -1) = 1 - q
$$

This is equivalent to a **Binomial distribution** under a sign transformation.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 📊 Distribution of Total Scores

Let $$K$$ = number of secure weeks, then:

$$
K \sim \mathrm{Binomial}(n, q)
$$

Since $$S_n = (+1)\cdot K + (-1)\cdot(n - K) = 2K - n$$, we have:

$$
S_n = 2K - n \quad\Longleftrightarrow\quad K = \frac{S_n + n}{2}
$$

Thus, the theoretical distribution of total scores is determined by the Binomial distribution of $$K$$.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

<h2>🛡️ Security vs Attackers — Random Walk Simulation</h2>

<label>Weeks (n):</label>
<input id="nInput" type="number" value="20"><br>

<label>Attackers (m):</label>
<input id="mInput" type="number" value="5"><br>

<label>Attack probability (p):</label>
<input id="pInput" type="number" value="0.2" step="0.01"><br>

<label>Simulations:</label>
<input id="runsInput" type="number" value="2000"><br><br>

<button id="runSim">Run Simulation</button>

<canvas id="securityChart" style="max-width:700px; margin-top:20px;"></canvas>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script type="module">
  import { simulateSecurityWalk, plotResults } from "{{ 'Statistics/HW_RandomWalkSecurity/assets/js/hw_security_walk.js' | relative_url }}";

  document.getElementById("runSim").onclick = () => {
    const n = +nInput.value;
    const m = +mInput.value;
    const p = +pInput.value;
    const runs = +runsInput.value;

    const { scoreCounts, q } = simulateSecurityWalk(n, m, p, runs);
    plotResults(scoreCounts, q, n, runs, "securityChart");
  };
</script>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 📈 Convergence to the Binomial Distribution

As the number of simulations runs → ∞  
the empirical distribution of final scores approaches the **binomial distribution** of the number of secure weeks.

As the number of weeks \( n \) grows, the random walk becomes smoother, and by the **Central Limit Theorem**, the distribution of scores approaches a **Gaussian shape** centered around:
$$
\mathbb{E}[S_n] = n(2q - 1)
$$
and variance
$$
\mathrm{Var}(S_n) = 4nq(1 - q)
$$

This demonstrates how:
- **Random walks**
- **Independent security risks**
- **Binomial distributions**

are mathematically and computationally linked.

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
