---
layout: default
title: HW11
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

<h2>🌀 HW11 — Realtime Brownian Motion Simulation</h2>

<div class="bm-panel">
  <div class="bm-controls">
    <div class="control">
      <label for="B_TInput">Time horizon \(T\)</label>
      <input id="B_TInput" type="number" value="1" step="0.1" min="0.1">
    </div>

    <div class="control">
      <label for="B_nInput">Time steps \(n\)</label>
      <input id="B_nInput" type="number" value="1000" min="200">
    </div>

    <div class="control">
      <label for="B_pathsInput">Visible paths</label>
      <input id="B_pathsInput" type="number" value="5" min="1" max="40">
    </div>
  </div>

  <div class="bm-buttons">
    <button id="B_startBtn">▶ Start</button>
    <button id="B_toggleBtn">⏸ Pause</button>
    <button id="B_resetBtn">🧹 Reset</button>
  </div>
</div>

<pre id="B_info" class="bm-info"></pre>

<h3>Sample trajectories of Brownian motion</h3>
<canvas id="B_canvas" class="bm-canvas"></canvas>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="module">
  import { initBrownianUI } from "{{ 'Statistics/HW11/assets/js/hw11_brownian.js' | relative_url }}";

  initBrownianUI({
    ids: {
      TInput: "B_TInput",
      nInput: "B_nInput",
      pathsInput: "B_pathsInput",
      startBtn: "B_startBtn",
      toggleBtn: "B_toggleBtn",
      resetBtn: "B_resetBtn",
      infoId: "B_info",
      canvasId: "B_canvas"
    },
    defaults: {
      T: 1,
      n: 1000,
      visiblePaths: 5,
      tickMs: 30
    }
  });
</script>

<div class="hw11-expl">

  <div class="expl-header">
    <span class="expl-badge">Theory</span>
    <h3>🧠 From Random Walks to Brownian Motion</h3>
    <p>
      We now build a <strong>continuous-time, continuous-state</strong> stochastic process:
      the <strong>Brownian motion</strong>, also called the <em>Wiener process</em>.
    </p>
  </div>

  <div class="expl-section">
    <h4>📉 Discrete approximation</h4>
    <p>
      We split time into \(n\) intervals of size
    </p>
    <p class="expl-formula">
      $$
      \Delta t = \frac{T}{n}.
      $$
    </p>
    <p>
      At each step we add a <strong>normal increment</strong>:
    </p>
    <p class="expl-formula">
      $$
      \Delta B_k \sim \mathcal{N}(0, \Delta t).
      $$
    </p>
    <p>
      The process evolves as:
      $$
      B_{k} = B_{k-1} + \Delta B_k.
      $$
    </p>
  </div>

  <div class="expl-section expl-highlight">
    <h4>🎲 Box–Muller: Generating Normal(0,1) from Uniform(0,1)</h4>
    <p>
      Since computers generate uniforms, we use the Box–Muller transform:
    </p>
    <p class="expl-formula">
      $$
      Z = \sqrt{-2\ln U_1}\,\cos(2\pi U_2),
      $$
    </p>
    <p>
      which produces \(Z \sim \mathcal{N}(0,1)\).  
      We then scale it by \(\sqrt{\Delta t}\) to obtain the Brownian increments.
    </p>
  </div>

  <div class="expl-section">
    <h4>📈 Limit: The Brownian motion</h4>
    <p>
      As \(n \to \infty\), the discrete approximation converges to true Brownian motion,
      characterized by:
    </p>
    <ul>
      <li>
        \(B(0) = 0\)
      </li>
      <li>
        <strong>Independent increments</strong>
      </li>
      <li>
        <strong>Stationary increments</strong>:
        $$
        B(t+h) - B(t) \sim \mathcal{N}(0, h)
        $$
      </li>
      <li>
        Continuous paths, but nowhere differentiable
      </li>
    </ul>
    <p class="expl-formula">
      $$
      \mathbb{E}[B(t)] = 0,
      \qquad
      \mathrm{Var}(B(t)) = t.
      $$
    </p>
  </div>

  <div class="expl-section">
    <h4>👀 Understanding the simulation</h4>
    <ul>
      <li>Each trajectory shows a possible realization of \(B(t)\).</li>
      <li>Fluctuations grow like \(\sqrt{t}\).</li>
      <li>Paths are continuous but have jagged, irregular structure.</li>
      <li>This is the mathematical foundation of stochastic calculus (Ito integrals, SDEs, etc.)</li>
    </ul>
  </div>

</div>

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
