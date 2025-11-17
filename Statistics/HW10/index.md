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

<div class="hw10-expl">

  <div class="expl-header">
    <span class="expl-badge">Theory</span>
    <h3>🧠 What are we simulating here?</h3>
    <p>
      We consider a counting process on the time interval \([0, T]\), where events occur
      at a constant average rate \(\lambda\) per unit time. This situation naturally leads
      to a <strong>Poisson counting process</strong>.
    </p>
  </div>

  <div class="expl-section">
    <h4>⏱ Discretizing time</h4>
    <p>
      To simulate the process on a computer, we <strong>discretize time</strong>:
    </p>
    <p class="expl-formula">
      $$
      \Delta t = \frac{T}{n}, \qquad n \text{ large (e.g. } n = 1000 \text{ or more)}
      $$
    </p>
    <p>
      and in each small subinterval we allow at most one event, with probability
    </p>
    <p class="expl-formula">
      $$
      p = \lambda\,\Delta t.
      $$
    </p>
    <p>
      Each subinterval behaves like a <strong>Bernoulli trial</strong>:
      either “event” (\(1\)) with probability \(p\), or “no event” (\(0\)) with probability \(1-p\).
      The counting process \(N(t)\) is then the running sum of these Bernoulli outcomes.
    </p>
  </div>

  <div class="expl-section expl-highlight">
    <h4>📈 From Bernoulli sums to a Poisson process</h4>
    <p>
      Fix a final time \(T\). If we sum the events over all \(n\) subintervals, we get
      an approximate total number of events:
    </p>
    <p class="expl-formula">
      $$
      N(T) \approx \sum_{k=1}^{n} X_k,
      $$
    </p>
    <p>
      where \(X_k \sim \text{Bernoulli}(p)\) and the \(X_k\)'s are independent.
      For large \(n\), with \(\Delta t = T/n\) and \(p = \lambda \Delta t\), the distribution of
      \(N(T)\) converges to a <strong>Poisson distribution</strong> with mean \(\lambda T\):
    </p>
    <p class="expl-formula">
      $$
      N(T) \;\overset{d}{\longrightarrow}\; \mathrm{Poisson}(\lambda T).
      $$
    </p>
    <p>
      When we repeat the whole simulation many times, the empirical histogram of \(N(T)\)
      (blue bars) gets closer and closer to the <strong>Poisson\((\lambda T)\)</strong> curve
      (orange line).
    </p>
  </div>

  <div class="expl-section">
    <h4>🔍 Properties of the Poisson counting process</h4>
    <div class="expl-grid">
      <div>
        <ul>
          <li>\(N(0) = 0\).</li>
          <li>
            <strong>Independent increments</strong>:
            the numbers of events in disjoint time intervals are independent.
          </li>
          <li>
            <strong>Stationary increments</strong>:
            only the length of the interval matters.
            For any \(t \ge 0\) and \(h &gt; 0\),
            $$
            N(t+h) - N(t) \sim \mathrm{Poisson}(\lambda h).
            $$
          </li>
        </ul>
      </div>
      <div class="expl-box">
        <p class="expl-box-title">Mean and variance</p>
        <p class="expl-formula">
          $$
          \mathbb{E}[N(t)] = \lambda t,
          \qquad
          \mathrm{Var}(N(t)) = \lambda t.
          $$
        </p>
        <p>
          This “mean = variance” identity is a distinctive fingerprint
          of the Poisson family.
        </p>
      </div>
    </div>
  </div>

  <div class="expl-section">
    <h4>⚙️ What does the rate \(\lambda\) mean?</h4>
    <div class="expl-grid">
      <div>
        <ul>
          <li>\(\lambda\) is the <strong>average number of events per unit time</strong>.</li>
          <li>\(\lambda T\) is the expected number of events in the whole interval \([0, T]\).</li>
        </ul>
      </div>
      <div class="expl-box">
        <p class="expl-box-title">Visual effect of \(\lambda\)</p>
        <ul>
          <li>
            <strong>Large \(\lambda\)</strong>: trajectories of \(N(t)\) climb faster and look steeper;
            the histogram of \(N(T)\) is centered at larger values.
          </li>
          <li>
            <strong>Small \(\lambda\)</strong>: events are rare, many paths stay flat for long periods;
            the histogram is concentrated on small counts (0, 1, 2…).
          </li>
        </ul>
      </div>
    </div>
  </div>

  <div class="expl-section">
    <h4>👀 How to read the simulation</h4>
    <ul>
      <li>
        The <strong>top chart</strong> shows several sample trajectories of \(N(t)\).
        Each one is a step function that increases by 1 whenever an event occurs.
      </li>
      <li>
        The <strong>bottom chart</strong> collects the final values \(N(T)\)
        from many simulated paths and builds an empirical histogram.
      </li>
      <li>
        The <strong>orange line</strong> is the theoretical Poisson\((\lambda T)\)
        probability mass function. As more paths finish, the blue bars align better
        with this curve.
      </li>
    </ul>
    <p>
      Together, these plots show how a simple discrete Bernoulli construction in
      small time steps converges to a <strong>continuous-time Poisson counting process</strong>
      with rate \(\lambda\).
    </p>
  </div>

</div>

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
