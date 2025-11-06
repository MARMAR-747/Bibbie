---
layout: default
title: HW6
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

# 📐 Online Mean & Variance — recurrence, proofs and implementation

This page derives the **simplest recurrence relationships** for the **arithmetic mean** and the **variance**, and implements **online algorithms** that update these statistics incrementally as new data arrive. Traditional "batch" algorithms (recomputing from scratch) are slower, require storing the full dataset and are **numerically unstable** (catastrophic cancellation, overflow).

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🧠 1) Arithmetic Mean — Recurrence & Proof

Given observations:

$$
x_1, x_2, \dots, x_n
$$

The arithmetic mean after $n$ samples is:

$$
\bar{x}_n = \frac{1}{n}\sum_{i=1}^{n} x_i
$$

### ✅ Recurrence Form (as derived on the whiteboard in class)

$$
\bar{x}_n = \frac{1}{n}\left((n-1)\bar{x}_{n-1} + x_n\right)
$$

### ✅ Online Update Form (best form for implementation)

$$
\boxed{\bar{x}_n = \bar{x}_{n-1} + \frac{x_n - \bar{x}_{n-1}}{n}}
$$

### ✍️ Proof

We start from the definition:

$$
\bar{x}_n = \frac{1}{n}\sum_{i=1}^{n} x_i
$$

I can see the sum up to $n$ as the sum up to $n-1$ plus the last value $x_n$:

$$
\sum_{i=1}^{n} x_i = \Big(\sum_{i=1}^{n-1}x_i\Big) + x_n
$$

So:

$$
\bar{x}_n = \frac{1}{n}\Big(\sum_{i=1}^{n-1}x_i + x_n\Big)
$$

Now, if the previous mean is:

$$
\bar{x}_{n-1} = \frac{1}{n-1}\sum_{i=1}^{n-1}x_i
$$

The sum up to $n-1$ is:

$$
\sum_{i=1}^{n-1}x_i = (n-1)\bar{x}_{n-1}
$$

Consequentially:

$$
\bar{x}_n = \frac{1}{n}\Big(\sum_{i=1}^{n-1}x_i + x_n\Big) = \frac{1}{n}\Big((n-1)\bar{x}_{n-1}+x_n\Big)
$$

Now we derive the online update formula. First, we divide the terms:

$$
\bar{x}_n = \frac{1}{n}\Big((n-1)\bar{x}_{n-1}+x_n\Big) = \frac{n-1}{n}\bar{x}_{n-1}+\frac{x_n}{n}
$$

Then, we rewrite $\frac{n-1}{n}=1-\frac{1}{n}$:

$$
\bar{x}_n = \frac{n-1}{n}\bar{x}_{n-1}+\frac{x_n}{n} = \bar{x}_{n-1}-\frac{1}{n}\bar{x}_{n-1}+\frac{x_n}{n}
$$

Finally, we factor and derive the online update formula:

$$
\bar{x}_n = \bar{x}_{n-1}-\frac{1}{n}\bar{x}_{n-1}+\frac{x_n}{n} = \bar{x}_{n-1}+\frac{x_n-\bar{x}_{n-1}}{n}
$$

### 💡 Interpretation

The new mean is the old mean plus a small adjustment, proportional to the difference between the new value and the old mean. If $x_n$ is greater than
$$
\bar{x}_{n-1}
$$
then
$$
\bar{x}_{n}
$$ 
grows, otherwise it decreases.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 📊 2) Variance — Recurrence & Proof

We want to update the variance **incrementally**, without recomputing all previous values.

Let the mean at step $n$ be $\bar{x}_n$, and define the **sum of squared deviations**:

$$
M2_n = \sum_{i=1}^{n}(x_i - \bar{x}_n)^2
$$

From this:

- **Population variance** is:

$$
\sigma_n^2 = \frac{M2_n}{n}
$$

- **Sample variance** (unbiased) is:

$$
s_n^2 = \frac{M2_n}{n-1} \quad \text{for } n \geq 2
$$

So our real goal is to find a recurrence for $M2_n$.

### ✅ Key Observation

Define the difference between the new value and the *previous* mean:

$$
\delta = x_n - \bar{x}_{n-1}
$$

The updated mean is (from the previous section):

$$
\bar{x}_n = \bar{x}_{n-1} + \frac{\delta}{n}
$$

Now, the deviation of the new point from the *new* mean is:

$$
x_n - \bar{x}_n
$$

### ✍️ Proof of the Recurrence for $M2_n$

Start from the definition:

$$
M2_n = \sum_{i=1}^n (x_i - \bar{x}_n)^2
$$

Split the sum:

$$
M2_n = \sum_{i=1}^{n-1}(x_i - \bar{x}_n)^2 + (x_n - \bar{x}_n)^2
$$

Now rewrite each term inside the sum by **adding and subtracting** $\bar{x}_{n-1}$:

$$
x_i - \bar{x}_n = (x_i - \bar{x}_{n-1}) + (\bar{x}_{n-1} - \bar{x}_n)
$$

Expand the square:

$$
(x_i - \bar{x}_n)^2 = (x_i - \bar{x}_{n-1})^2 + 2(x_i - \bar{x}_{n-1})(\bar{x}_{n-1} - \bar{x}_n) + (\bar{x}_{n-1} - \bar{x}_n)^2
$$

Sum over $i = 1, \dots, n-1$.  
The middle term disappears because:

$$
\sum_{i=1}^{n-1}(x_i - \bar{x}_{n-1}) = 0
$$

(This is always true: deviations around the mean sum to zero.)

So we are left with:

$$
M2_n = M2_{n-1} + (n-1)(\bar{x}_{n-1} - \bar{x}_n)^2 + (x_n - \bar{x}_n)^2
$$

Now substitute the mean update relation:

$$
\bar{x}_{n-1} - \bar{x}_n = -\frac{\delta}{n}, \qquad x_n - \bar{x}_n = \delta\frac{n-1}{n}
$$

Substitute into the equation:

$$
M2_n 
= M2_{n-1} + (n-1)\left(\frac{\delta}{n}\right)^2 + \left(\delta\frac{n-1}{n}\right)^2
$$

Factor the terms:

$$
M2_n = M2_{n-1} + \frac{n-1}{n}\delta^2
$$

This is the **recurrence form**.

### ✅ Final Online Update Formula (Welford’s Algorithm)

$$
\boxed{
\begin{aligned}
\delta &= x_n - \bar{x}_{n-1} \\
\bar{x}_n &= \bar{x}_{n-1} + \frac{\delta}{n} \\
M2_n &= M2_{n-1} + \frac{n-1}{n}\delta^2
\end{aligned}
}
$$

Then:

$$
\boxed{s_n^2 = \frac{M2_n}{n-1}} \quad \text{(sample variance)}
$$

### 💡 Interpretation

- If $x_n$ is **far** from the previous mean, $\delta$ is large → variance increases.
- If $x_n$ is **close** to the mean, $\delta$ is small → variance barely changes.
- The update is **local**, does **not** require past data, and is **numerically stable**.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

<h2>📈 Online Mean & Variance — Interactive Demo</h2>
<p>This demo shows incremental statistics updated in real time.</p>

<button id="pause-btn" style="padding:6px 12px; border-radius:6px; cursor:pointer;">⏸ Pause</button>
<button id="reset-btn" style="padding:6px 12px; border-radius:6px; cursor:pointer;">🧹 Reset</button>

<pre id="stats-output" style="background:#111; color:#0f0; padding:12px; border-radius:6px; width:max-content; margin-top:12px;"></pre>

<canvas id="stats-chart" style="max-width:700px; margin-top:20px;"></canvas>

<h3 style="margin-top:20px;">Values Streamed (latest updates):</h3>
<pre id="values-list" style="
  background:#222;
  color:#0ff;
  padding:10px;
  border-radius:6px;
  max-width:700px;
  max-height:120px;
  overflow-y:auto;
"></pre>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script type="module">
  import { startOnlineDemo } from "{{ 'Statistics/HW6/assets/js/hw_online_stats.js' | relative_url }}";
  startOnlineDemo("stats-output", "stats-chart", "pause-btn", "reset-btn", "values-list");
</script>

The script used to achieve this is the following:  

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-javascript">
// ============================================================
// Online Mean & Variance (Welford's Algorithm)
// ============================================================
// This class updates the mean and variance incrementally,
// without storing past data and without recomputing everything.
// It is O(1) in memory and computation and numerically stable.

export class OnlineStats {
  constructor() {
    this.n = 0;      // Number of observed samples
    this.mean = 0;   // Running mean
    this.M2 = 0;     // Running sum of squared deviations
  }

  // Add one new data point x
  push(x) {
    this.n += 1;
    const delta = x - this.mean;   // Deviation from previous mean
    this.mean += delta / this.n;   // Update mean
    const delta2 = x - this.mean;  // Deviation from updated mean
    this.M2 += delta * delta2;     // Update M2 for variance tracking
  }

  // Sample variance (unbiased estimator)
  get variance() {
    return this.n > 1 ? this.M2 / (this.n - 1) : 0;
  }

  // Sample standard deviation
  get std() {
    return Math.sqrt(this.variance);
  }
}


// ============================================================
// Streaming Demo Controller
// ============================================================
// This function attaches chart + live updates to the page.

export function startOnlineDemo(outputElementId, canvasElementId, pauseButtonId, resetButtonId, listElementId) {
  const stats = new OnlineStats();
  const output = document.getElementById(outputElementId);
  const canvas = document.getElementById(canvasElementId).getContext("2d");
  const pauseBtn = document.getElementById(pauseButtonId);
  const resetBtn = document.getElementById(resetButtonId);
  const listBox = document.getElementById(listElementId);

  let isRunning = true;
  let values = []; // store recent values for display

  const xValues = [];
  const meanValues = [];

  const chart = new Chart(canvas, {
    type: "line",
    data: {
      labels: xValues,
      datasets: [{
        label: "Running Mean",
        data: meanValues,
        borderColor: "orange",
        borderWidth: 2,
        fill: false,
        tension: 0.15
      }]
    },
    options: { responsive: true, animation: false }
  });

  function refreshOutput() {
    output.textContent =
      `Count:    ${stats.n}\n` +
      `Mean:     ${stats.mean.toFixed(4)}\n` +
      `Variance: ${stats.variance.toFixed(4)}\n` +
      `Std Dev:  ${stats.std.toFixed(4)}`;
  }

  function refreshList() {
    listBox.textContent = values.join(", ");
  }

  pauseBtn.addEventListener("click", () => {
    isRunning = !isRunning;
    pauseBtn.textContent = isRunning ? "⏸ Pause" : "▶ Play";
  });

  resetBtn.addEventListener("click", () => {
    stats.n = 0;
    stats.mean = 0;
    stats.M2 = 0;
    values = [];
    xValues.length = 0;
    meanValues.length = 0;
    chart.update();
    refreshOutput();
    refreshList();
  });

  setInterval(() => {
    if (!isRunning) return;

    const x = Math.random() * 10; // example: new value in [0,10]
    stats.push(x);

    values.push(x.toFixed(2));
    if (values.length > 200) values.shift(); // keep list manageable

    xValues.push(stats.n);
    meanValues.push(stats.mean);

    if (xValues.length > 200) {
      xValues.shift();
      meanValues.shift();
    }

    chart.update();
    refreshOutput();
    refreshList();

  }, 1000);
}
  </code></pre>
</div>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🧮 Numerical Stability and Computational Advantages of Online Algorithms

When computing the mean and variance, there are two general approaches:

1. **Batch (recompute-all) methods** — use full data each time:
   $$
   \bar{x} = \frac{1}{n}\sum_{i=1}^{n} x_i,
   \qquad
   s^2 = \frac{1}{n-1}\sum_{i=1}^{n}(x_i - \bar{x})^2
   $$

   These methods require **access to all past data** and involve repeated summation over growing sets.

3. **Online (incremental) methods** — update statistics as new data arrives using recurrence formulas:
   $$
   \bar{x}_n = \bar{x}_{n-1} + \frac{x_n - \bar{x}_{n-1}}{n}
   $$
   $$
   M2_n = M2_{n-1} + \delta (x_n - \bar{x}_n), \qquad \delta = x_n - \bar{x}_{n-1}
   $$

   These methods store **only constant memory** and update in **$O(1)$ time per observation**.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

### 🔹 Numerical Stability and Error Propagation

Batch computation of variance often uses the formula:
$$
s^2 = \frac{1}{n-1}\left(\sum x_i^2 - n \bar{x}^2\right)
$$

This expression is well-known to be **numerically unstable**, because:
- $\sum x_i^2$ and $n\bar{x}^2$ may be **very large**
- their **difference may be very small**
And this would cause **catastrophic cancellation** (loss of significant digits).

Online algorithms **avoid this subtraction** entirely.  
The update form:
$$
M2_n = M2_{n-1} + \delta(x_n - \bar{x}_n)
$$
keeps intermediate values **small and centered**, minimizing floating-point roundoff.

| Aspect | Batch Formula | Online (Welford) |
|--------|--------------|----------------|
| Risk of catastrophic cancellation | **High** (difference of large numbers) | **Low**, no large subtraction |
| Sensitivity to floating-point precision | High | Low |
| Accumulated numerical error | Grows with $n$ | Bounded and stable |

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

### 🔹 Overflow and Range Control

Batch algorithms often compute:
- $\sum x_i$ (which can overflow for large $n$),
- $\sum x_i^2$ (which can overflow even faster).

Online methods avoid this because they:
- Track only the **mean** and **M2**, which stay **on the scale of the data**, not $n$ times bigger.
- Therefore reduce risk of:
  - Overflow,
  - Underflow,
  - Loss of significance.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

### 🔹 Computational Efficiency and Scalability

| Property | Batch Methods | Online Methods |
|---------|--------------|----------------|
| Time per new data point | $O(n)$ | **$O(1)$** |
| Memory usage | $O(n)$ (must store data) | **$O(1)$** |
| Can run on streaming data? | ❌ No | ✅ Yes |
| Suitable for large datasets? | ❌ Not scalable | ✅ Scales to millions/billions of samples |
| Can be used in real-time? | ❌ No | ✅ Yes |

This makes online algorithms ideal for:
- Streaming analytics
- IoT sensors
- Network monitoring
- Real-time dashboards
- Big data pipelines
- Machine learning with continuous updates

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
