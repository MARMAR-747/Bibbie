---
layout: default
title: HW2
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

# 📚 Datasets and Distributions

A **dataset** is a structured collection of data, usually organized in **rows** and **columns** (like a table in a database). Each **row** represents one observation/record, while each **column** represents one variable/feature. A dataset is the foundation for any kind of data analysis or statistical computation: it provides the raw information from which we can extract patterns, compute distributions, and make conclusions.

A **distribution** describes how the values of a variable are spread or arranged within a dataset — which values occur and **how often** they appear.

---

## 🔎 Types of distributions (by number of variables)

<div class="dist-list">

**Univariate distribution** → focuses on **one** variable.  
<div class="dist-example">
<b>Example:</b> how many players come from each country, or how many have a given age.
</div>

**Bivariate distribution** → focuses on **two** variables simultaneously.  
<div class="dist-example">
<b>Example:</b> how win rate changes with age, or how champion preference varies by country.
</div>

<div class="dots-separator">
  <span>⋮</span><br>
  <span>⋮</span>
</div>

**Multivariate distribution** → analyzes **three or more** variables simultaneously.

</div>

---

## 🧭 What distributions help us understand

- **Shape of the data:** symmetric, skewed, etc.  
- **Central tendency:** mean, median, mode.  
- **Spread:** variance, standard deviation.  
- **Relationships between variables** (bivariate and multivariate cases).

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
