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

<h2>🛡️ Server Security — Random Walk Simulation (Animated)</h2>
<p>
  Weekly +1 if secure, −1 if breached (at least one attacker succeeds).<br>
  Security probability per week: <code>q = (1 - p)^m</code>.
</p>

<div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(220px,1fr)); gap:10px; max-width:820px;">
  <label>Weeks (n): <input id="nInput" type="number" value="20" min="1" step="1"></label>
  <label>Attackers (m): <input id="mInput" type="number" value="5" min="1" step="1"></label>
  <label>Attack probability (p): <input id="pInput" type="number" value="0.2" min="0" max="1" step="0.01"></label>
  <label>Simulations (total runs): <input id="runsInput" type="number" value="2000" min="1" step="1"></label>
  <label>Visible trajectories (animated): <input id="visiblesInput" type="number" value="10" min="1" max="60" step="1"></label>
</div>

<div style="margin:12px 0; display:flex; gap:8px; flex-wrap:wrap;">
  <button id="startBtn">▶ Start</button>
  <button id="toggleBtn">⏸ Pause</button>
  <button id="resetBtn">🧹 Reset</button>
</div>

<pre id="infoBox" style="background:#111;color:#0f0;padding:10px;border-radius:6px;max-width:420px;"></pre>

<h3 style="margin-top:10px;">Animated trajectories</h3>
<canvas id="pathCanvas" style="max-width:820px;"></canvas>

<h3 style="margin-top:16px;">Distribution of final scores (empirical vs theoretical)</h3>
<canvas id="distCanvas" style="max-width:820px;"></canvas>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="module">
  import { initSecurityUI } from "{{ 'Statistics/HW7/assets/js/hw_security_walk_animated.js' | relative_url }}";

  initSecurityUI({
    ids: {
      nInput: 'nInput',
      mInput: 'mInput',
      pInput: 'pInput',
      runsInput: 'runsInput',
      visiblesInput: 'visiblesInput',
      startBtn: 'startBtn',
      toggleBtn: 'toggleBtn',
      resetBtn: 'resetBtn',
      pathCanvasId: 'pathCanvas',
      distCanvasId: 'distCanvas',
      infoBoxId: 'infoBox'
    },
    defaults: {
      n: 20, m: 5, p: 0.2, runs: 2000, visibles: 10, tickMs: 50
    }
  });
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
