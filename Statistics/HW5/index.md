---
layout: default
title: HW5
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

---
layout: default
title: HW5 — Measures of Location and Dispersion
nav_exclude: true
---

<div class="nav-buttons">
  <a href="/Bibbie/Statistics/" class="nav-button left">⬅️ Statistics</a>
</div>

# 📊 Measures of Location and Dispersion

In statistics, **measures of location (or central tendency)** and **measures of dispersion (or variability)** are used to describe and summarize data.  
- **Location measures** identify a **representative value** around which observations cluster.  
- **Dispersion measures** describe **how spread out** the data are.

Together, they provide a more complete understanding of a dataset.

---

## 🎯 Measures of Location (Central Tendency)

These measures describe the **center** or the most typical value in a dataset.

| Measure | Definition | Formula / Description | Advantages | Limitations |
|--------|------------|----------------------|------------|-------------|
| **Mean (Arithmetic Average)** | The sum of all values divided by the number of values. | $$\displaystyle \bar{x} = \frac{1}{n}\sum_{i=1}^n x_i$$ | Uses all data; good for symmetric distributions. | Sensitive to outliers and skewed data. |
| **Median** | The middle value when data are ordered. | If \(n\) is odd → middle value; If even → average of two middle values. | Robust to outliers; good for skewed distributions. | Ignores magnitude of values beyond order. |
| **Mode** | The most frequently occurring value. | Count-based measure. | Useful for categorical data. | May not be unique or may not exist (no repetition). |
| **Midrange** | Average of the minimum and maximum values. | $$\displaystyle \frac{\min(x) + \max(x)}{2}$$ | Easy to compute. | Extremely sensitive to outliers. |
| **Trimmed Mean** | Mean after removing extreme values (e.g., 5% highest and lowest). | Remove extremes → compute mean. | Reduces effect of outliers while still using most data. | Requires choosing trimming percentage.

---

### ✅ Choosing a Location Measure

| Data Characteristics | Best Measure |
|----------------------|-------------|
| Symmetric, no outliers | **Mean** |
| Skewed distributions / heavy outliers | **Median** |
| Categorical or discrete repeats | **Mode** |

---

## 📐 Measures of Dispersion (Variability)

These measures indicate **how spread out** values are around the center.

| Measure | Definition | Formula / Description | Advantages | Limitations |
|--------|------------|----------------------|------------|-------------|
| **Range** | Difference between max and min. | $$\text{Range} = \max(x) - \min(x)$$ | Simple to compute. | Extremely sensitive to outliers. |
| **Variance** | Average squared deviation from the mean. | $$\displaystyle s^2 = \frac{1}{n-1}\sum_{i=1}^n (x_i - \bar{x})^2$$ | Fundamental in statistical theory. | Units are squared → less intuitive. |
| **Standard Deviation (SD)** | Square root of variance. | $$\displaystyle s = \sqrt{s^2}$$ | Same units as data; widely used. | Still influenced by outliers. |
| **Interquartile Range (IQR)** | Range between the 25th and 75th percentiles. | $$\text{IQR} = Q_3 - Q_1$$ | Robust to outliers; measures spread of central mass. | Ignores tails of distribution. |
| **Coefficient of Variation (CV)** | Relative dispersion: SD normalized by mean. | $$\displaystyle \text{CV} = \frac{s}{\bar{x}}$$ | Useful to compare datasets with different scales. | Undefined if mean = 0.

---

### ✅ Choosing a Dispersion Measure

| Situation | Recommended Measure |
|----------|---------------------|
| Data are roughly normal | **Standard Deviation** |
| Data contain outliers or heavy tails | **IQR** |
| Compare datasets with different units or scales | **Coefficient of Variation** |

---

## 🧮 Examples for Each Measure

Consider the dataset:

$$
X = \{2,\, 4,\, 4,\, 5,\, 7,\, 9\}
$$

There are $n = 6$ observations.

---

## 🎯 Measures of Location (Central Tendency)

### 1) **Mean (Arithmetic Average)**

$$
\bar{x} = \frac{2 + 4 + 4 + 5 + 7 + 9}{6} = \frac{31}{6} \approx 5.17
$$

### 2) **Median**

Ordered data: $2, 4, 4, 5, 7, 9$  
Middle values (since $n = 6$): $4$ and $5$

$$
\text{Median} = \frac{4 + 5}{2} = 4.5
$$

### 3) **Mode**

The value appearing most often is:

$$
\text{Mode} = 4
$$

### 4) **Midrange**

$$
\text{Midrange} = \frac{\min(X) + \max(X)}{2} = \frac{2 + 9}{2} = 5.5
$$

### 5) **Trimmed Mean** (10% trimming example)

Remove smallest and largest values → remaining data: $4, 4, 5, 7$

$$
\text{Trimmed Mean} = \frac{4 + 4 + 5 + 7}{4} = \frac{20}{4} = 5
$$

---

## 📐 Measures of Dispersion (Variability)

### 1) **Range**

$$
\text{Range} = 9 - 2 = 7
$$

### 2) **Variance**

First compute the mean $ \bar{x} \approx 5.17 $

Squared deviations:

| $x_i$ | $x_i - \bar{x}$ | $(x_i - \bar{x})^2$ |
|---|---|---|
| 2 | -3.17 | 10.05 |
| 4 | -1.17 | 1.37 |
| 4 | -1.17 | 1.37 |
| 5 | -0.17 | 0.03 |
| 7 | 1.83 | 3.35 |
| 9 | 3.83 | 14.67 |

Sum:

$$
\sum (x_i - \bar{x})^2 = 30.84
$$

Sample variance:

$$
s^2 = \frac{30.84}{6 - 1} = 6.17
$$

### 3) **Standard Deviation**

$$
s = \sqrt{6.17} \approx 2.48
$$

### 4) **Interquartile Range (IQR)**

Quartiles:

$$
Q_1 = 4, \qquad Q_3 = 7
$$

$$
\text{IQR} = Q_3 - Q_1 = 7 - 4 = 3
$$

### 5) **Coefficient of Variation (CV)**

$$
\text{CV} = \frac{s}{\bar{x}} = \frac{2.48}{5.17} \approx 0.48
$$

→ The relative dispersion is **48%** of the mean.

---

## ✅ Key Insight

| Type | Interpretation | Example Result |
|---|---|---|
| **Location** | Where values cluster | Around $\approx 5$ (mean $\approx 5.17$, median $= 4.5$) |
| **Dispersion** | How spread values are | Moderate spread (SD $\approx 2.48$, IQR $= 3$) |

Together, these measures give a **complete statistical summary** of the dataset.

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
