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

The new mean is the old mean plus a small adjustment, proportional to the difference between the new value and the old mean. If $x_n$ is greater than $\bar{x}_{n-1}$ then $\bar{x}_{n}$ grows, otherwise it decreases.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 📊 2) Variance — Stable Online Recurrence (Welford Algorithm)

Define:

$$
M2_n = \sum_{i=1}^{n}(x_i - \bar{x}_n)^2.
$$

Then:

- **Population variance:**
$$
\sigma_n^2 = \frac{M2_n}{n}
$$

- **Sample variance:**
$$
s_n^2 = \frac{M2_n}{n-1} \quad \text{for } n \ge 2
$$

### ✅ Online Update Recurrence

Let:

$$
\delta = x_n - \bar{x}_{n-1}
$$

Then:

$$
\boxed{\bar{x}_n = \bar{x}_{n-1} + \frac{\delta}{n}}
$$

$$
\boxed{M2_n = M2_{n-1} + \delta(x_n - \bar{x}_n)}
$$

This avoids computing $\sum x_i$ and $\sum x_i^2$, preventing **catastrophic cancellation**.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## ⚙️ 3) JavaScript Implementation (Stable & Online)

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
