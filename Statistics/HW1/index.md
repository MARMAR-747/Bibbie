---
layout: default
title: HW1
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

# 📊 What is Statistics?

<div class="quote-box">
  <p><em>“Statistics is the grammar of science.”</em></p>
  <p class="quote-author">— Karl Pearson</p>
</div>

Statistics (*status rerum* — "state of affairs") is a tool of the scientific method that uses mathematics to study a collective phenomenon.  
This study can be both **qualitative** and **quantitative** and occurs under conditions of **uncertainty** or **non-determinism**, with a partial knowledge of the phenomenon.

The need to quantify the phenomena under study — that is, to analyze and describe them in mathematical terms — has its origins in **ancient Egypt**,  
where successive dynasties introduced the recording of data such as **population size**, **material goods**, **number of soldiers**, and so on.  

Obviously, statistics has undergone significant evolution over the ages, up to the present day where two main branches are recognized:

---

## 📈 Main Branches of Statistics

### 1️⃣ Descriptive Statistics
Its purpose is to **summarize data** through graphical tools  
(such as bar charts, pie charts, histograms, box plots) and indices  
(statistical indicators, position indicators, dispersion indicators, correlation indicators, shape indicators, etc.)  
that describe the salient aspects of the observed data, thus forming the statistical content.

<div class="figure-container">
  <img src="/Bibbie/assets/images/FIG1.png" alt="Example histogram by AGCOM" class="figure-img">
  <p class="figure-caption">
    <strong>Fig. 1.</strong> Example of histogram provided by AGCOM, depicting FTTH coverage in Italy based on the number of municipalities reached (regardless of how many households are served).
  </p>
</div>

---

### 2️⃣ Inferential Statistics
Its purpose is to **establish the characteristics** of the data and the behavior of the measurements collected  
(statistical variables) with a predetermined **probability of error**.  

It is strongly connected to **probability theory** and allows us to make predictions based on models derived from inferential techniques.

<div class="figure-container">
  <img src="/Bibbie/assets/images/FIG2.png" alt="Example of linear regression on Nasdaq" class="figure-img">
  <p class="figure-caption">
    <strong>Fig. 2.</strong> Example of linear regression applied to Nasdaq price movements (hourly chart, 131-period calculation).  
    The regression line acts as an equilibrium price and can be used for both trend-following and reversal strategies.
  </p>
</div>

---

## 📚 Summary
- **Descriptive statistics** → represents and summarizes data.  
- **Inferential statistics** → draws conclusions and predictions about a population from sample data.  

Both are fundamental in transforming **data into knowledge**, providing the mathematical foundation of decision-making under uncertainty.

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
