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

# 📚 Part I: Datasets and Distributions

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

## 🧭 1.1. What distributions help us understand

- **Shape of the data:** symmetric, skewed, etc.  
- **Central tendency:** mean, median, mode.  
- **Spread:** variance, standard deviation.  
- **Relationships between variables** (bivariate and multivariate cases).

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## 💾 1.2. Practical Example — Creating a Dataset using PostgreSQL

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

## 🧩 1.2.1. Populating the Dataset

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

## 📈 1.2.2. Univariate Distributions — Exploring Single Variables

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

## 🔄 1.2.3. Bivariate Distribution — Relationship Between Two Variables

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

# 🧮 Part II: Letter distribution and Caesar Cipher
Let's choose a text and:
- Calculate its letter distribution
- Encrypt it using the Caesar cipher with a given shift
- Decode it assuming we don't know the shift used.
The chosen text is "Freedom" by Italo Calvino.

<p><strong>📜 Original text:</strong></p>
<pre class="typewriter">
Freedom is not a starting point but a destination.
It is a slow, difficult conquest, built day by day through choices, actions, and thoughts. There is no moment in which we can truly say: “Now I am free.” Because at every moment, something tries to bind us again — fear, judgment, habit, memory.
Freedom is not the absence of constraints, but the ability to move within them without being crushed. It is like walking through a maze, knowing that each wall can be a guide rather than a prison.

Many confuse freedom with license — with the right to do whatever they please.
But being free does not mean being able to do everything; it means knowing what is worth doing, and doing it. It means choosing your own path, even when it is hard, even when others take the easier road.
Freedom is quiet; it does not need to shout. It lives in the smallest gestures: in telling the truth when lying would be easier; in staying faithful to an idea when everyone else has forgotten it; in looking at the world through your own eyes, not through the eyes others lend you.

Every true form of freedom is born from knowledge.
No one can be free without knowing from what they wish to be freed.
Those who do not recognize their own chains will never be able to break them.
That is why culture is the first form of freedom.
To read, to understand, to question — these acts liberate us more than any revolution. Human freedom begins the moment a person learns to think for themselves.

And yet, freedom is never solitude.
No one is truly free unless others are free as well.
Freedom is a good that grows only when shared — like air, like light, like hope.
It cannot be hoarded; whoever tries to keep it for themselves will lose it.

We live in times when freedom is often reduced to a word, a slogan, an abstract right. But true freedom does not need solemn declarations; it needs awareness, commitment, courage. It is a path measured in the everyday.
To be free is to accept responsibility for who we are.
It means not making excuses, not delegating, not looking away.
It means understanding that every choice we make shapes a part of the world, and that every free act is also a just one.

Perhaps absolute freedom does not exist. But that should not discourage us.
Freedom is made of degrees, of attempts, of falls and new beginnings.
It is a fragile flame that must be protected even when it seems useless.
And if one day we lose it, it will not be because someone took it from us, but because we failed to defend it.

Because freedom is not a gift.
It is a responsibility.
It is the highest test of our humanity.
</pre>

We will use the following script, appropriately commented:  

<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-javascript">
(function () {
  // ===== CONFIG =====
  // Testo originale da cifrare
  const TEXT = `Freedom is not a starting point but a destination.
It is a slow, difficult conquest, built day by day through choices, actions, and thoughts. There is no moment in which we can truly say: “Now I am free.” Because at every moment, something tries to bind us again — fear, judgment, habit, memory.
Freedom is not the absence of constraints, but the ability to move within them without being crushed. It is like walking through a maze, knowing that each wall can be a guide rather than a prison.

Many confuse freedom with license — with the right to do whatever they please.
But being free does not mean being able to do everything; it means knowing what is worth doing, and doing it. It means choosing your own path, even when it is hard, even when others take the easier road.
Freedom is quiet; it does not need to shout. It lives in the smallest gestures: in telling the truth when lying would be easier; in staying faithful to an idea when everyone else has forgotten it; in looking at the world through your own eyes, not through the eyes others lend you.

Every true form of freedom is born from knowledge.
No one can be free without knowing from what they wish to be freed.
Those who do not recognize their own chains will never be able to break them.
That is why culture is the first form of freedom.
To read, to understand, to question — these acts liberate us more than any revolution. Human freedom begins the moment a person learns to think for themselves.

And yet, freedom is never solitude.
No one is truly free unless others are free as well.
Freedom is a good that grows only when shared — like air, like light, like hope.
It cannot be hoarded; whoever tries to keep it for themselves will lose it.

We live in times when freedom is often reduced to a word, a slogan, an abstract right. But true freedom does not need solemn declarations; it needs awareness, commitment, courage. It is a path measured in the everyday.
To be free is to accept responsibility for who we are.
It means not making excuses, not delegating, not looking away.
It means understanding that every choice we make shapes a part of the world, and that every free act is also a just one.

Perhaps absolute freedom does not exist. But that should not discourage us.
Freedom is made of degrees, of attempts, of falls and new beginnings.
It is a fragile flame that must be protected even when it seems useless.
And if one day we lose it, it will not be because someone took it from us, but because we failed to defend it.

Because freedom is not a gift.
It is a responsibility.
It is the highest test of our humanity.`;

  // Valore di shift per la cifratura di Cesare
  const SHIFT = 5;

  // Frequenze tipiche delle lettere nella lingua inglese (in percentuale)
  const ENGLISH_FREQ = {
    a: 8.167, b: 1.492, c: 2.782, d: 4.253, e: 12.702, f: 2.228, g: 2.015,
    h: 6.094, i: 6.966, j: 0.153, k: 0.772, l: 4.025, m: 2.406, n: 6.749,
    o: 7.507, p: 1.929, q: 0.095, r: 5.987, s: 6.327, t: 9.056, u: 2.758,
    v: 0.978, w: 2.360, x: 0.150, y: 1.974, z: 0.074
  };

  // Bigrammi comuni in inglese, usati per affinare la decifratura
  const COMMON_BIGRAMS = ["th", "he", "in", "er", "an", "re", "on", "at", "en", "nd"];

  // Codice ASCII della lettera 'a'
  const A = "a".charCodeAt(0);

  // Funzione per tenere solo le lettere minuscole del testo
  const onlyLettersLower = s => s.toLowerCase().replace(/[^a-z]/g, "");

  // Converte un indice (0–25) nella lettera corrispondente
  const indexToLetter = i => String.fromCharCode(A + i);

  // === CIFRARIO DI CESARE ===
  function caesar(str, shift) {
    // Normalizza lo shift per mantenerlo nel range 0–25
    shift = ((shift % 26) + 26) % 26;
    // Sostituisce ogni lettera con quella spostata di "shift"
    return str.replace(/[A-Za-z]/g, ch => {
      const isUpper = ch >= "A" && ch <= "Z"; // Maiuscola?
      const base = isUpper ? 65 : 97; // Codice ASCII base per A/a
      const code = ch.charCodeAt(0) - base; // Indice della lettera (0–25)
      return String.fromCharCode(((code + shift) % 26) + base); // Nuova lettera
    });
  }

  // === CONTEGGIO LETTERE ===
  function freqCounts(s) {
    const counts = Array(26).fill(0); // Inizializza array di 26 zeri
    for (const c of s) counts[c.charCodeAt(0) - A]++; // Incrementa contatore per ogni lettera
    return counts; // Restituisce array di frequenze assolute
  }

  // Trasforma l'array dei conteggi in un oggetto { lettera: conteggio }
  function freqObjectFromCounts(counts) {
    const obj = {};
    for (let i = 0; i < 26; i++) obj[indexToLetter(i)] = counts[i];
    return obj;
  }

  // Ordina l'oggetto delle frequenze in base al valore (decrescente)
  function sortFreqObject(obj) {
    return Object.entries(obj).sort((a, b) => b[1] - a[1]);
  }

  // === ANALISI STATISTICA (Chi-quadrato) ===
  // Confronta la distribuzione osservata con quella attesa (inglese)
  function chiSquare(obsCounts, expPercents) {
    const N = obsCounts.reduce((a, b) => a + b, 0) || 1; // Numero totale di lettere
    let chi = 0;
    for (let i = 0; i < 26; i++) {
      const expected = (expPercents[i] / 100) * N; // Frequenza attesa
      if (expected > 0) {
        const diff = obsCounts[i] - expected;
        chi += (diff * diff) / expected; // Somma parziale per il test chi²
      }
    }
    return chi;
  }

  // === BIGRAM SCORE ===
  // Conta quanti bigrammi comuni appaiono nel testo
  function bigramScore(str) {
    const s = onlyLettersLower(str);
    let score = 0;
    for (const bg of COMMON_BIGRAMS) {
      const matches = s.match(new RegExp(bg, "g"));
      score += matches ? matches.length : 0;
    }
    return score;
  }

  // === RILEVAMENTO AUTOMATICO DELLO SHIFT ===
  function guessShiftByLanguage(cipher) {
    let best = { shift: 0, chi: Infinity, tie: -Infinity, plaintext: "" };
    for (let s = 0; s < 26; s++) { // Prova tutti i possibili shift
      const candidate = caesar(cipher, 26 - s); // Decifra con shift inverso
      const L = onlyLettersLower(candidate); // Pulisce il testo
      const obs = freqCounts(L); // Frequenze osservate
      const expPerc = Array(26).fill(0).map((_, i) => ENGLISH_FREQ[indexToLetter(i)]); // Frequenze attese
      const chi = chiSquare(obs, expPerc); // Calcola chi²
      const tie = bigramScore(candidate); // Calcola punteggio dei bigrammi
      // Se chi² è più piccolo (o uguale ma con più bigrammi comuni), è una soluzione migliore
      const better = (chi < best.chi) || (chi === best.chi && tie > best.tie);
      if (better) best = { shift: s, chi, tie, plaintext: candidate };
    }
    return best;
  }

  // Scrive del testo in un elemento HTML tramite id
  function write(id, text) {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
  }

  // ===== MAIN =====
  // Pulisce il testo tenendo solo le lettere
  const letters = onlyLettersLower(TEXT);
  // Calcola la frequenza assoluta delle lettere
  const counts = freqCounts(letters);
  // Ordina la distribuzione per frequenza decrescente
  const sortedCounts = sortFreqObject(freqObjectFromCounts(counts));

  // Cifra il testo con il Cifrario di Cesare (shift definito sopra)
  const encrypted = caesar(TEXT, SHIFT);
  // Tenta di indovinare lo shift e decifrare automaticamente
  const guess = guessShiftByLanguage(encrypted);

  // ===== SCRITTURA RISULTATI NELLA PAGINA =====
  // Distribuzione lettere ordinate
  write("caesar-distribution", sortedCounts.map(([k,v]) => `${k}: ${v}`).join("\n"));
  // Testo cifrato
  write("caesar-encrypted", encrypted);
  // Testo decifrato automaticamente
  write("caesar-decrypted", guess.plaintext);
  // Info sull'analisi automatica
  write("caesar-info", `Guessed shift: ${guess.shift} | Chi²: ${guess.chi.toFixed(2)} | Bigram score: ${guess.tie}`);
})();
  </code></pre>
</div>

<p><strong>Letter Distribution:</strong></p>
<pre id="caesar-distribution" class="caesar-output"></pre>

<p><strong>🔒 Encrypted text:</strong></p>
<pre id="caesar-encrypted" class="typewriter"></pre>

<p><strong>🧠 Decrypted text (auto-detected shift):</strong></p>
<pre id="caesar-decrypted" class="typewriter"></pre>

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
