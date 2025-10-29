---
layout: default
title: HW4
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

## 🔢 The Law of Large Numbers (LLN)

The **Law of Large Numbers (LLN)** is a cornerstone of probability theory stating that the **average of a large number of independent and identically distributed (i.i.d.) random variables** tends to approach the **expected value** of the underlying distribution as the number of trials increases.

In simpler terms:  
> When an experiment is repeated many times, the **observed (empirical) frequency** of an event approaches its **true probability**.

---

### 📘 Formal Definition

Let $X_1, X_2, \dots, X_n$ be i.i.d. random variables with expected value $E[X_i] = \mu$.  
The Law of Large Numbers states that:

$$
\frac{1}{n} \sum_{i=1}^{n} X_i \xrightarrow[n \to \infty]{} \mu
$$

This convergence can be:

- **Weak LLN** → convergence *in probability*  
- **Strong LLN** → convergence *almost surely* (with probability 1)

---

### 🎯 Example: Bernoulli Trials

Consider a sequence of Bernoulli trials with success probability $p$.  
Each trial is defined as:

$$
X_i =
\begin{cases}
1, & \text{if success} \\
0, & \text{if failure}
\end{cases}
$$

The **relative frequency of success** after $n$ trials is:

$$
f(n) = \frac{1}{n}\sum_{i=1}^{n} X_i
$$

According to the Law of Large Numbers:

$$
f(n) \to p \quad \text{as } n \to \infty
$$

---

### 💡 Intuitive Meaning

At the beginning (small $n$), the relative frequency $f(n)$ fluctuates heavily — randomness dominates.  
As $n$ increases, these fluctuations shrink, and $f(n)$ stabilizes near the true probability $p$.  
This explains why empirical averages become more reliable with many observations.

---

<!-- === LLN Interactive Simulator === -->
<link rel="preconnect" href="https://cdn.jsdelivr.net" />
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
  /* Layout principale */
  .lln-wrap {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 20px;
    align-items: start;
    margin-top: 1rem;
  }

  /* Pannello dei controlli */
  .lln-card {
    padding: 14px;
    border: 1px solid #e0e0e0;
    border-radius: 10px;
    background: #fff;
    box-shadow: 0 2px 6px rgba(0,0,0,0.04);
  }

  .lln-controls {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px;
    margin: 16px 0;
    align-items: end;
  }

  .lln-controls label {
    font-weight: 600;
    font-size: 0.9rem;
    display: block;
    margin-bottom: 4px;
  }

  .lln-controls input[type="range"] {
    width: 100%;
  }

  .lln-small {
    font-size: 0.8rem;
    color: #666;
    margin-top: 2px;
  }

  /* Pulsanti */
  .lln-buttons {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
  }

  .lln-buttons button {
    flex: 1;
    min-width: 70px;
    padding: 8px 10px;
    border-radius: 8px;
    border: 1px solid #d0d0d0;
    background: #fafafa;
    cursor: pointer;
    font-weight: 500;
    transition: 0.2s;
  }

  .lln-buttons button:hover {
    background: #f3f3f3;
  }

  canvas {
    width: 100% !important;
    height: 420px !important;
  }

  @media (max-width: 900px) {
    .lln-wrap { grid-template-columns: 1fr; }
    .lln-controls { grid-template-columns: 1fr 1fr; }
    canvas { height: 360px !important; }
  }
</style>

<h2>🎲 LLN Simulation — trajectories & histogram (VERSION 1)</h2>

<div class="lln-card lln-controls">
  <div>
    <label>Trials \( n \): <span id="nVal">200</span></label>
    <input id="nSlider" type="range" min="1" max="2000" step="1" value="200"/>
    <div class="lln-small">Move or press ▶ Play</div>
  </div>
  <div>
    <label>Max trials \( n_{\max} \): <span id="nMaxVal">1000</span></label>
    <input id="nMaxSlider" type="range" min="50" max="10000" step="50" value="1000"/>
    <div class="lln-small">Upper bound for the x-axis</div>
  </div>
  <div>
    <label>Trajectories \( m \): <span id="mVal">50</span></label>
    <input id="mSlider" type="range" min="1" max="150" step="1" value="50"/>
    <div class="lln-small">More lines → heavier rendering</div>
  </div>
  <div>
    <label>Success prob \( p \): <span id="pVal">0.50</span></label>
    <input id="pSlider" type="range" min="0" max="1" step="0.01" value="0.50"/>
    <div class="lln-small">Bernoulli\(p\) per trial</div>
  </div>
  <div>
    <label>Bins (hist): <span id="binsVal">20</span></label>
    <input id="binsSlider" type="range" min="5" max="40" step="1" value="20"/>
    <div class="lln-small">Histogram resolution</div>
  </div>
  <div class="lln-buttons">
    <button id="playBtn">▶ Play</button>
    <button id="pauseBtn">⏸ Pause</button>
    <button id="regenBtn">🔁 Regenerate</button>
    <button id="resetBtn">🧹 Reset</button>
  </div>
</div>

<div class="lln-wrap">
  <div class="lln-card">
    <h3 style="margin:6px 0 8px 0;">Trajectories of \( f(n) \) (relative frequency)</h3>
    <canvas id="trajChart"></canvas>
  </div>
  <div class="lln-card">
    <h3 style="margin:6px 0 8px 0;">Histogram of \( f(n) \) across \( m \) trajectories</h3>
    <canvas id="histChart"></canvas>
    <p class="lln-small">As \( n \) grows, the empirical distribution concentrates near \( p \).</p>
  </div>
</div>

<script src="{{ 'Statistics/HW4/assets/js/hw4_lln.js' | relative_url }}" defer></script>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

<!-- === LLN Interactive Simulator === -->
<link rel="preconnect" href="https://cdn.jsdelivr.net" />
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
  /* Layout principale */
  .lln-wrap {
    display: grid;
    grid-template-columns: 2fr 1fr;
    gap: 20px;
    align-items: start;
    margin-top: 1rem;
  }

  /* Pannello dei controlli */
  .lln-card {
    padding: 14px;
    border: 1px solid #e0e0e0;
    border-radius: 10px;
    background: #fff;
    box-shadow: 0 2px 6px rgba(0,0,0,0.04);
  }

  .lln-controls {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px;
    margin: 16px 0;
    align-items: end;
  }

  .lln-controls label {
    font-weight: 600;
    font-size: 0.9rem;
    display: block;
    margin-bottom: 4px;
  }

  .lln-controls input[type="range"] {
    width: 100%;
  }

  .lln-small {
    font-size: 0.8rem;
    color: #666;
    margin-top: 2px;
  }

  /* Pulsanti */
  .lln-buttons {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
  }

  .lln-buttons button {
    flex: 1;
    min-width: 70px;
    padding: 8px 10px;
    border-radius: 8px;
    border: 1px solid #d0d0d0;
    background: #fafafa;
    cursor: pointer;
    font-weight: 500;
    transition: 0.2s;
  }

  .lln-buttons button:hover {
    background: #f3f3f3;
  }

  canvas {
    width: 100% !important;
    height: 420px !important;
  }

  @media (max-width: 900px) {
    .lln-wrap { grid-template-columns: 1fr; }
    .lln-controls { grid-template-columns: 1fr 1fr; }
    canvas { height: 360px !important; }
  }
</style>

<h2>🎲 LLN Simulation — trajectories & histogram (VERSION 2)</h2>

<div class="lln-card lln-controls">
  <div>
    <label>Trials \( n \): <span id="nVal">200</span></label>
    <input id="nSlider" type="range" min="1" max="2000" step="1" value="200"/>
    <div class="lln-small">Move or press ▶ Play</div>
  </div>
  <div>
    <label>Max trials \( n_{\max} \): <span id="nMaxVal">1000</span></label>
    <input id="nMaxSlider" type="range" min="50" max="10000" step="50" value="1000"/>
    <div class="lln-small">Upper bound for the x-axis</div>
  </div>
  <div>
    <label>Trajectories \( m \): <span id="mVal">50</span></label>
    <input id="mSlider" type="range" min="1" max="150" step="1" value="50"/>
    <div class="lln-small">More lines → heavier rendering</div>
  </div>
  <div>
    <label>Success prob \( p \): <span id="pVal">0.50</span></label>
    <input id="pSlider" type="range" min="0" max="1" step="0.01" value="0.50"/>
    <div class="lln-small">Bernoulli\(p\) per trial</div>
  </div>
  <div>
    <label>Bins (hist): <span id="binsVal">20</span></label>
    <input id="binsSlider" type="range" min="5" max="40" step="1" value="20"/>
    <div class="lln-small">Histogram resolution</div>
  </div>
  <div class="lln-buttons">
    <button id="playBtn">▶ Play</button>
    <button id="pauseBtn">⏸ Pause</button>
    <button id="regenBtn">🔁 Regenerate</button>
    <button id="resetBtn">🧹 Reset</button>
  </div>
</div>

<div class="lln-wrap">
  <div class="lln-card">
    <h3 style="margin:6px 0 8px 0;">Trajectories of \( f(n) \) with right-side histogram</h3>
    <canvas id="trajChart"></canvas>
    <p class="lln-small">The yellow bars on the right show the empirical distribution of \(f(n)\) over the \(m\) trajectories at the current \(n\). The red dashed line marks \(p\).</p>
  </div>
</div>

<script src="{{ 'Statistics/HW4/assets/js/hw4_llnv2.js' | relative_url }}" defer></script>

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
