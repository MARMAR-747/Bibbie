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

## 🔢 HW10 – Bernoulli Approximation of a Poisson Counting Process

<p>
Simulate a counting process on <strong>[0, T]</strong> by splitting the interval into
<code>n</code> small subintervals and generating an event in each one with probability
<code>λ · T / n</code>. The resulting cumulative counts approximate a
<strong>Poisson process</strong> with rate λ, and the number of events in [0, T]
approaches a <strong>Poisson(λT)</strong> distribution.
</p>

<div style="margin:12px 0; display:grid; grid-template-columns: repeat(auto-fit,minmax(190px,1fr)); gap:10px; max-width:820px;">
  <label>Time horizon T:
    <input id="TInput" type="number" value="1" step="0.1" min="0.1">
  </label>
  <label>Rate λ:
    <input id="lambdaInput" type="number" value="5" step="0.5" min="0.1">
  </label>
  <label>Subintervals n:
    <input id="nInput" type="number" value="5000" min="10" step="100">
  </label>
  <label>Visible trajectories:
    <input id="pathsInput" type="number" value="5" min="1" max="20">
  </label>
  <label>Paths for histogram:
    <input id="runsInput" type="number" value="2000" min="100">
  </label>
</div>

<button id="runPoissonBtn">▶ Run simulation</button>

<pre id="poissonInfo" style="
  margin-top:10px;
  background:#111;
  color:#0f0;
  padding:10px;
  border-radius:8px;
  max-width:820px;
  font-family:ui-monospace, SFMono-Regular, Menlo, Consolas, monospace;
  white-space:pre-wrap;
"></pre>

<h3>Sample trajectories of the counting process N(t)</h3>
<canvas id="poissonTrajCanvas" style="max-width:820px;"></canvas>

<h3 style="margin-top:18px;">Distribution of N(T)</h3>
<canvas id="poissonHistCanvas" style="max-width:820px;"></canvas>

<!-- Chart.js + your HW10 JS -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="module">
  import { initPoissonUI } from "{{ 'Statistics/HW10/assets/js/hw10_poisson.js' | relative_url }}";

  initPoissonUI({
    ids: {
      TInput: 'TInput',
      lambdaInput: 'lambdaInput',
      nInput: 'nInput',
      pathsInput: 'pathsInput',
      runsInput: 'runsInput',
      runBtn: 'runPoissonBtn',
      infoBoxId: 'poissonInfo',
      trajCanvasId: 'poissonTrajCanvas',
      histCanvasId: 'poissonHistCanvas'
    },
    defaults: {
      T: 1,
      lambda: 5,
      n: 5000,
      visiblePaths: 5,
      runs: 2000
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
