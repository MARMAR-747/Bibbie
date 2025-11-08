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

<style>
  /* --- Responsive form layout --- */
  .rw-panel { margin: 12px 0 8px; }
  .rw-controls {
    display: grid;
    grid-template-columns: repeat(3, minmax(180px, 1fr));
    gap: 12px 16px;
    align-items: end;
  }
  .rw-controls .control label {
    display: block;
    font-weight: 600;
    font-size: .92rem;
    margin-bottom: 6px;
  }
  .rw-controls .control input[type="number"] {
    width: 100%;
    padding: 8px 10px;
    border: 1px solid var(--border, #d0d0d0);
    border-radius: 8px;
    background: var(--bg, #fff);
  }

  /* second row spans 2+1 columns nicely */
  .rw-controls .span-2 { grid-column: span 2; }

  /* Buttons row */
  .rw-actions {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-top: 10px;
  }
  .rw-actions button {
    padding: 8px 14px;
    border-radius: 8px;
    border: 1px solid var(--border, #d0d0d0);
    background: var(--bg, #fafafa);
    cursor: pointer;
    font-weight: 600;
  }
  .rw-actions button:hover { background: #f2f2f2; }

  /* Info box and canvases */
  .rw-info {
    background: #111; color: #0f0;
    padding: 10px; border-radius: 8px; max-width: 520px;
    margin: 10px 0 14px;
    font-family: ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
    white-space: pre-wrap;
  }
  .rw-canvas { width: 100%; max-width: 900px; }

  /* Dark theme compatibility (Just the Docs) */
  @media (prefers-color-scheme: dark) {
    :root { --border:#2c2c2c; --bg:#151515; }
  }

  /* Mobile: stack to one column */
  @media (max-width: 820px) {
    .rw-controls { grid-template-columns: 1fr; }
    .rw-controls .span-2 { grid-column: auto; }
  }
</style>

<h2>🛡️ Server Security — Random Walk Simulation (Animated)</h2>

<div class="rw-panel">
  <div class="rw-controls">
    <!-- Row 1 -->
    <div class="control">
      <label for="nInput">Weeks (n)</label>
      <input id="nInput" type="number" min="1" step="1" value="25">
    </div>
    <div class="control">
      <label for="mInput">Attackers (m)</label>
      <input id="mInput" type="number" min="1" step="1" value="20">
    </div>
    <div class="control">
      <label for="pInput">Attack probability (p)</label>
      <input id="pInput" type="number" min="0" max="1" step="0.01" value="0.01">
    </div>

    <!-- Row 2 -->
    <div class="control span-2">
      <label for="runsInput">Simulations (total runs)</label>
      <input id="runsInput" type="number" min="1" step="1" value="2000">
    </div>
    <div class="control">
      <label for="visiblesInput">Visible trajectories (animated)</label>
      <input id="visiblesInput" type="number" min="1" max="60" step="1" value="25">
    </div>
  </div>

  <div class="rw-actions">
    <button id="startBtn">▶ Start</button>
    <button id="toggleBtn">⏸ Pause</button>
    <button id="resetBtn">🧹 Reset</button>
  </div>
</div>

<pre id="infoBox" class="rw-info"></pre>

<h3>Animated trajectories</h3>
<canvas id="pathCanvas" class="rw-canvas"></canvas>

<h3 style="margin-top:16px;">Distribution of final scores (empirical vs theoretical)</h3>
<canvas id="distCanvas" class="rw-canvas"></canvas>

<!-- Chart.js + your existing module init stay as-is -->
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
    defaults: { n: 25, m: 20, p: 0.01, runs: 2000, visibles: 25, tickMs: 50 }
  });
</script>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 📈 Convergence to the Binomial Distribution

As the number of simulations runs → ∞ the empirical distribution of final scores approaches the **binomial distribution** of the number of secure weeks.

As the number of weeks \( n \) grows, the random walk becomes smoother, and by the **Central Limit Theorem**, the distribution of scores approaches a **Gaussian shape** centered around:

$$
\mathbb{E}[S_n] = n(2q - 1)
$$

and variance:

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
