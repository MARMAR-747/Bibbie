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

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🔎 Types of distributions (by number of variables)

<div class="dist-list" markdown="1">

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

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🧭 What distributions help us understand

- **Shape of the data:** symmetric, skewed, etc.  
- **Central tendency:** mean, median, mode.  
- **Spread:** variance, standard deviation.  
- **Relationships between variables** (bivariate and multivariate cases).

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

# 💾 Practical Example — Creating a Dataset using PostgreSQL

To better understand how datasets are created and structured before statistical analysis,  
we can use a simple SQL example. The following query defines a table named `players`,  
which could represent data collected from a multiplayer game:

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-sql">
CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    player_name VARCHAR(50),
    age INT,
    country VARCHAR(50),
    matches_played INT,
    win_rate FLOAT,
    favorite_champion VARCHAR(50)
);
  </code></pre>
</div>

This table structure allows us to store data in a **tabular format**,  
where each row corresponds to a player and each column represents one of their attributes.  
It is from such datasets that we can later compute **distributions**, visualize data,  
and perform **statistical analysis**.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🧩 Populating the Dataset

Once the structure of the `players` table has been defined,  
we can insert data to build our dataset.  
Each row represents an individual player, while each column corresponds to one of their characteristics —  
for example, their name, country, or win rate.

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-sql">
INSERT INTO players (player_name, age, country, matches_played, win_rate, favorite_champion)
VALUES
('Alice', 22, 'Italy', 150, 0.65, 'Lux'),
('Bob', 27, 'USA', 300, 0.72, 'Ezreal'),
('Carol', 19, 'France', 80, 0.58, 'Ahri'),
('David', 24, 'Italy', 200, 0.60, 'Favij'),
('Eve', 30, 'Germany', 400, 0.77, 'Garen'),
('Franz', 28, 'Germany', 250, 0.69, 'Ashe'),
('Giulia', 21, 'Italy', 180, 0.63, 'Power'),
('Henry', 23, 'UK', 120, 0.55, 'Lux'),
('Isabelle', 26, 'France', 220, 0.68, 'Garen'),
('Jack', 32, 'USA', 450, 0.73, 'Ninja'),
('Karen', 29, 'Spain', 300, 0.70, 'Ashe'),
('Luca', 25, 'Italy', 260, 0.66, 'Garen'),
('Maria', 20, 'Portugal', 90, 0.59, 'Ahri'),
('Noah', 24, 'USA', 310, 0.71, 'Ezreal'),
('Olivia', 22, 'UK', 150, 0.61, 'Lux'),
('Paolo', 28, 'Italy', 330, 0.74, 'Velox'),
('Quentin', 31, 'France', 410, 0.76, 'Garen'),
('Rita', 23, 'Germany', 230, 0.65, 'Ahri'),
('Sara', 27, 'Italy', 280, 0.69, 'Cicciogamer'),
('Tom', 21, 'Spain', 110, 0.57, 'Ashe');
  </code></pre>
</div>

The resulting table is the following:  
<div class="figure-container">
  <img src="/Bibbie/assets/images/Players_Table.png" alt="Example dataset table" class="figure-img">
</div>

This query fills the dataset with sample data representing different players  
from multiple countries. Such a dataset can later be used to compute **descriptive statistics**  
(e.g., average win rate per country or most frequent champion)  
and to explore **bivariate distributions** between variables like *age* and *win rate*.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 📈 Univariate Distributions — Exploring Single Variables

Once the dataset has been created, we can begin analyzing the **frequency distributions**  
of individual variables. These analyses allow us to observe how often certain values occur  
and identify the most frequent characteristics in our dataset.

Below are three examples of **univariate distributions** obtained from the `players` table.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

### 🧙‍♂️ Favorite Champion Distribution
The following query counts how many players prefer each champion.  
It gives us insight into which champions are the most popular in the dataset.

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-sql">
SELECT favorite_champion, COUNT(*) AS frequency
FROM players
GROUP BY favorite_champion
ORDER BY frequency DESC;
  </code></pre>
</div>

<div class="figure-container">
  <img src="/Bibbie/assets/images/Univariate_favorite_champion_table.png" alt="Example dataset table" class="figure-img">
</div>
<div class="figure-container">
  <img src="/Bibbie/assets/images/Bar_chart_favorite_champion.png" alt="Example dataset table" class="figure-img">
</div>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

### 👥 Age Distribution
This query groups the players by age, counting how many share the same value.  
It helps identify the **most common age groups** among the players.

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-sql">
SELECT age, COUNT(*) AS frequency
FROM players
GROUP BY age
ORDER BY age;
  </code></pre>
</div>

<div class="figure-container">
  <img src="/Bibbie/assets/images/Univariate_age_table.png" alt="Example dataset table" class="figure-img">
</div>
<div class="figure-container">
  <img src="/Bibbie/assets/images/Bar_chart_age.png" alt="Example dataset table" class="figure-img">
</div>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

### 🌍 Country Distribution
This last query shows how many players come from each country,  
helping us understand the **geographical composition** of the dataset.

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-sql">
SELECT country, COUNT(*) AS frequency
FROM players
GROUP BY country
ORDER BY frequency DESC;
  </code></pre>
</div>

<div class="figure-container">
  <img src="/Bibbie/assets/images/Univariate_country_table.png" alt="Example dataset table" class="figure-img">
</div>
<div class="figure-container">
  <img src="/Bibbie/assets/images/Bar_chart_country.png" alt="Example dataset table" class="figure-img">
</div>

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

Together, these queries describe three **univariate distributions** —  
each one focusing on a single variable (*favorite_champion*, *age*, and *country*).  
They form the first step in understanding the overall structure and diversity of our data.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 🔄 Bivariate Distribution — Relationship Between Two Variables

After analyzing the single-variable (univariate) distributions,  
we can move to a **bivariate distribution**, which explores the relationship between two variables.  
In this case, we study how the **preferred champion** varies depending on the **player’s country**.

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-sql">
SELECT 
    country,
    favorite_champion,
    COUNT(*) AS frequency
FROM players
GROUP BY country, favorite_champion
ORDER BY country, frequency DESC;
  </code></pre>
</div>

<div class="figure-container">
  <img src="/Bibbie/assets/images/Bivariate_country_fav_table.png" alt="Example dataset table" class="figure-img">
</div>

This query produces a **two-dimensional frequency table**,  
where each row represents a pair *(country, favorite_champion)* and shows  
how many players fall into that combination.
By comparing these joint frequencies, we can detect potential **correlations** or **patterns** —  
for example, whether players from the same country tend to favor the same champions,  
or if regional preferences differ significantly.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

# 🧮 Letter distribution and Caesar Cipher
Let's choose a text and calculate its letter distribution. The text in question is "Freedom" by Italo Calvino.

<p><strong>📜 Original text:</strong></p>
<pre class="typewriter">
La libertà non è un punto di partenza, ma un punto d’arrivo. È una conquista lenta, faticosa, che si costruisce ogni giorno attraverso le scelte, i gesti, i pensieri. Non esiste un momento in cui possiamo dire: “Ecco, ora sono libero.” Perché in ogni momento, qualcosa torna a legarci: la paura, il giudizio, l’abitudine, il ricordo.
La libertà non è assenza di vincoli, ma capacità di muoversi dentro i vincoli senza esserne schiacciati. È come camminare in un labirinto sapendo che ogni muro può diventare una guida, non una prigione.

Molti confondono la libertà con l’arbitrio, con il diritto di fare ciò che si vuole. Ma essere liberi non significa poter fare tutto; significa sapere ciò che vale la pena di fare, e farlo. È scegliere il proprio cammino, anche quando è difficile, anche quando gli altri prendono la strada più comoda.
La libertà è silenziosa, non ha bisogno di gridare. Vive nei gesti minimi: nel dire la verità quando conviene mentire, nel restare fedeli a un’idea anche quando tutti l’hanno dimenticata, nel guardare il mondo con i propri occhi e non con quelli che ci prestano gli altri.

Ogni libertà autentica nasce dalla conoscenza. Non si può essere liberi se non si sa da cosa si vuole essere liberi. Chi non conosce le proprie catene, non potrà mai spezzarle.
Per questo la cultura è la prima forma di libertà. Leggere, capire, interrogarsi: sono atti che liberano più di qualunque rivoluzione. La libertà dell’uomo comincia quando egli impara a pensare con la propria testa.
Eppure, la libertà non è mai solitudine. Nessuno è libero davvero se non lo sono anche gli altri. La libertà è un bene che cresce solo se condiviso. Come l’aria, come la luce, come la speranza. Non può essere tenuta per sé: chi tenta di farlo, la perde.

Viviamo in tempi in cui la libertà viene spesso ridotta a una parola, a uno slogan, a un diritto astratto. Ma la libertà vera non ha bisogno di dichiarazioni solenni: ha bisogno di coscienza, di impegno, di coraggio. È un cammino che si misura nel quotidiano.
Essere liberi è accettare la responsabilità di ciò che si è. È non cercare scuse, non delegare, non voltarsi dall’altra parte. È comprendere che ogni nostra scelta disegna una parte del mondo, e che ogni atto libero è anche un atto giusto.

Forse la libertà assoluta non esiste. Ma ciò non deve scoraggiarci. La libertà è fatta di gradi, di tentativi, di cadute e riprese. È una fiamma fragile, che bisogna difendere anche quando sembra inutile.
E se un giorno la perderemo, non accadrà per colpa di chi ce l’ha tolta, ma di chi non l’ha difesa.

Perché la libertà non è un dono. È una responsabilità. È la prova più alta della nostra umanità.
</pre>

<p><strong>Letter Distribution:</strong></p>
<pre id="caesar-distribution" class="caesar-output"></pre>

<p><strong>Encrypted Text:</strong></p>
<pre id="caesar-encrypted" class="caesar-output"></pre>

<p><strong>Decrypted Text:</strong></p>
<pre id="caesar-decrypted" class="caesar-output"></pre>

<p><strong>Analysis:</strong></p>
<pre id="caesar-info" class="caesar-output"></pre>

<script src="{{ 'Statistics/HW2/assets/js/Caesar.js' | relative_url }}" defer></script>
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
