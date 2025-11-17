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

## 🎲 Realtime Poisson Counting Process Simulation

<div class="poi-panel">
  <div class="poi-controls">
    <div class="control">
      <label for="TInput">Time horizon \(T\)</label>
      <input id="TInput" type="number" value="1" step="0.1" min="0.1">
    </div>
    <div class="control">
      <label for="lambdaInput">Rate \(\lambda\)</label>
      <input id="lambdaInput" type="number" value="5" step="0.5" min="0.1">
    </div>
    <div class="control">
      <label for="nInput">Subintervals \(n\)</label>
      <input id="nInput" type="number" value="1000" min="50" step="50">
    </div>
    <div class="control">
      <label for="pathsInput">Visible trajectories</label>
      <input id="pathsInput" type="number" value="5" min="1" max="40">
    </div>
    <div class="control">
      <label for="runsInput">Paths for histogram</label>
      <input id="runsInput" type="number" value="2000" min="100">
    </div>
  </div>

  <div class="poi-actions">
    <button id="poiStartBtn">▶ Start</button>
    <button id="poiToggleBtn">⏸ Pause</button>
    <button id="poiResetBtn">🧹 Reset</button>
  </div>
</div>

<pre id="poissonInfo" class="poi-info"></pre>

<h3>Sample trajectories of the counting process \(N(t)\)</h3>
<canvas id="poissonTrajCanvas" class="poi-canvas"></canvas>

<h3 style="margin-top:1.2rem;">Distribution of \(N(T)\)</h3>
<canvas id="poissonHistCanvas" class="poi-canvas"></canvas>

<h3>🧠 Theoretical interpretation</h3>

<p>
  We divide the time interval \([0, T]\) into \(n\) small subintervals of length
</p>

<p>
  $$
  \Delta t = \frac{T}{n}
  $$
</p>

<p>
  and in each subinterval we generate at most one event with probability
</p>

<p>
  $$
  p = \lambda \,\Delta t.
  $$
</p>

<p>
  This is a <strong>Bernoulli approximation</strong> of a counting process.
  Let \(N(t)\) be the cumulative number of events up to time \(t\).
  For each fixed \(T\):
</p>

<ul>
  <li>The total number of events \(N(T)\) is approximately the sum of \(n\) independent Bernoulli\((p)\) variables.</li>
  <li>
    As \(n \to \infty\) and \(\Delta t \to 0\) with \(\lambda\) fixed,
    the law of \(N(T)\) converges to a <strong>Poisson distribution</strong> with mean \(\lambda T\):
    $$
    N(T) \;\overset{d}{\longrightarrow}\; \mathrm{Poisson}(\lambda T).
    $$
  </li>
</ul>

<p>
  The limiting process \(\{N(t), t \ge 0\}\) is called a
  <strong>Poisson counting process</strong> with rate \(\lambda\).
  It has the following key properties:
</p>

<ul>
  <li>\(N(0) = 0\).</li>
  <li><strong>Independent increments</strong>: for disjoint intervals, the increments of \(N(t)\) are independent.</li>
  <li>
    <strong>Stationary increments</strong>: for any \(t \ge 0\) and \(h &gt; 0\),
    $$
    N(t + h) - N(t) \sim \mathrm{Poisson}(\lambda h).
    $$
  </li>
</ul>

<p>
  In particular,
</p>

<p>
  $$
  \mathbb{E}[N(t)] = \lambda t,
  \qquad
  \mathrm{Var}(N(t)) = \lambda t.
  $$
</p>

<p>
  The parameter \(\lambda\) is the <strong>intensity</strong> or <strong>rate</strong> of the process:
</p>

<ul>
  <li>\(\lambda\) is the <strong>expected number of events per unit time</strong>.</li>
  <li>\(\lambda T\) is the expected number of events in the whole interval \([0, T]\).</li>
  <li>A larger \(\lambda\) produces trajectories that climb faster and histograms centered around higher values of \(N(T)\).</li>
</ul>

<p>
  In our simulation:
</p>

<ul>
  <li>The <strong>step–like trajectories</strong> illustrate the random evolution of \(N(t)\) over time.</li>
  <li>
    The <strong>empirical histogram</strong> of \(N(T)\) gradually approaches the
    <strong>Poisson\((\lambda T)\)</strong> probability mass function (orange curve)
    as the number of simulated paths increases.
  </li>
</ul>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="module">
  import { initPoissonUI } from "{{ 'Statistics/HW10/assets/js/hw10_poisson_rt.js' | relative_url }}";

  initPoissonUI({
    ids: {
      TInput: 'TInput',
      lambdaInput: 'lambdaInput',
      nInput: 'nInput',
      pathsInput: 'pathsInput',
      runsInput: 'runsInput',
      startBtn: 'poiStartBtn',
      toggleBtn: 'poiToggleBtn',
      resetBtn: 'poiResetBtn',
      infoBoxId: 'poissonInfo',
      trajCanvasId: 'poissonTrajCanvas',
      histCanvasId: 'poissonHistCanvas'
    },
    defaults: {
      T: 1,
      lambda: 5,
      n: 1000,
      visiblePaths: 5,
      runs: 2000,
      tickMs: 5
    }
  });
</script>

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
