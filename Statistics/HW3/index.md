---
layout: default
title: HW3
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

# 🔐 Understanding RSA Systems

RSA (**Rivest–Shamir–Adleman**) is one of the most widely used **public-key cryptographic systems**. It allows two parties to exchange information securely **without sharing a secret key in advance**. The strength of RSA lies in the **difficulty of factoring large numbers** into their prime components.

## ⚙️ How RSA Works — Step by Step

RSA is based on **number theory** and the properties of **modular arithmetic**.  
Let’s explore each step in detail. 👇

---

### 1️⃣ Choose Two Prime Numbers

Select two distinct prime numbers `p` and `q`.

> For illustration, we’ll use small primes — in real-world RSA, these are extremely large.  
`p = 41`  
`q = 53`

---

### 2️⃣ Compute the Modulus and Euler’s Totient

Compute the following:

`n = p × q = 41 × 53 = 2173`  
`φ(n) = (p - 1) × (q - 1) = 40 × 52 = 2080`

These two values are **fundamental** to the RSA system.

---

### 3️⃣ Choose the public exponent *e*

Pick an integer *e* such that:

- *e*  is greater than 1 and less than φ(n): `1 < e < φ(n)` 
- *e*  is coprime with φ(n) (they share no common divisors other than 1): `gcd(e, φ(n)) = 1`

For example:  
`e = 17`  
`gcd(17, 2080) = 1` ✅

---

### 4️⃣ Compute the private exponent *d*

The private exponent *d* is the **modular inverse** of *e* with respect to φ(n):

`d × e ≡ 1 mod φ(n)`

To find *d*, we use the **Extended Euclidean Algorithm**, which allows us to find integers
x and y such that:

`a × x + b × y = gcd(a,b)`

When a = e and b = φ(n), the value x gives us the modular inverse *d*.

For example:  
e = 17, φ(n) = 2080
→ d = 367 (since 17 × 367 ≡ 1 mod 2080)

---

### 5️⃣ Create the keys

- **Public key** (shared with everyone): (n,e)
- **Private key** (kept secret): (n,d)

Public key  → `(n=2173, e=17)`  
Private key → `(n=2173, d=367)`

---

### 6️⃣ Encryption and Decryption

To **encrypt** a message `m` (represented as a number):

`c = m^e mod n`

To **decrypt** it:

`m = c^d mod n`

Because of the mathematical relationship between *e*, *d*, and *φ(n)*, this process perfectly reverses the encryption operation — the decrypted message m is identical to the original one.

---

## 💡 Why RSA Works

RSA relies on the fact that it’s **easy to multiply two large primes**, but **extremely hard to factor their product n back into p and q**. Without knowing p and q, it’s practically **impossible** (for large enough numbers) to compute φ(n) and thus find the private key d. This **asymmetry** — easy to compute in one direction, hard to reverse — is what makes RSA a secure **one-way function**.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

# 🔨 RSA at work

Let's consider the text used in Homework 2 ("Freedom" by Italo Calvino). Instead of the Caesar cipher, in this case we will try to encrypt and decrypt it using the principles of RSA systems.

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
  // Original text to be encrypted
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

  // Shift value for Caesar cipher
  const SHIFT = 5;

  // Typical letter frequencies in the English language (in percentages)
  const ENGLISH_FREQ = {
    a: 8.167, b: 1.492, c: 2.782, d: 4.253, e: 12.702, f: 2.228, g: 2.015,
    h: 6.094, i: 6.966, j: 0.153, k: 0.772, l: 4.025, m: 2.406, n: 6.749,
    o: 7.507, p: 1.929, q: 0.095, r: 5.987, s: 6.327, t: 9.056, u: 2.758,
    v: 0.978, w: 2.360, x: 0.150, y: 1.974, z: 0.074
  };

  // Common bigrams in English, used to refine decryption
  const COMMON_BIGRAMS = ["th", "he", "in", "er", "an", "re", "on", "at", "en", "nd"];

  // ASCII code of the letter 'a'
  const A = "a".charCodeAt(0);

  // Function to keep only lowercase letters in the text
  const onlyLettersLower = s => s.toLowerCase().replace(/[^a-z]/g, "");

  // Converts an index (0–25) to the corresponding letter
  const indexToLetter = i => String.fromCharCode(A + i);

  // === CAESAR'S CIPHER ===
  function caesar(str, shift) {
    // Normalizes the shift to keep it in the range 0–25
    shift = ((shift % 26) + 26) % 26;
    // Replaces each letter with the shifted one of "shift" steps
    return str.replace(/[A-Za-z]/g, ch => {
      const isUpper = ch >= "A" && ch <= "Z"; // Capital?
      const base = isUpper ? 65 : 97; // Basic ASCII code for A/a
      const code = ch.charCodeAt(0) - base; // Letter index (0–25)
      return String.fromCharCode(((code + shift) % 26) + base); // New letter
    });
  }

  // === LETTER COUNT ===
  function freqCounts(s) {
    const counts = Array(26).fill(0); // Initialize array of 26 zeros
    for (const c of s) counts[c.charCodeAt(0) - A]++; // Increment counter for each letter
    return counts; // Returns an array of absolute frequencies
  }

  // Transform the counts array into a { letter: count } object
  function freqObjectFromCounts(counts) {
    const obj = {};
    for (let i = 0; i < 26; i++) obj[indexToLetter(i)] = counts[i];
    return obj;
  }

  // Sort the frequency object by value (descending)
  function sortFreqObject(obj) {
    return Object.entries(obj).sort((a, b) => b[1] - a[1]);
  }

  // === STATISTICAL ANALYSIS (Chi-square) ===
  // Compare the observed distribution with the expected one (English)
  function chiSquare(obsCounts, expPercents) {
    const N = obsCounts.reduce((a, b) => a + b, 0) || 1; // Total number of letters
    let chi = 0;
    for (let i = 0; i < 26; i++) {
      const expected = (expPercents[i] / 100) * N; // Expected frequency
      if (expected > 0) {
        const diff = obsCounts[i] - expected;
        chi += (diff * diff) / expected; // Partial sum for the chi² test
      }
    }
    return chi;
  }

  // === BIGRAM SCORE ===
  // Count how many common bigrams appear in the text
  function bigramScore(str) {
    const s = onlyLettersLower(str);
    let score = 0;
    for (const bg of COMMON_BIGRAMS) {
      const matches = s.match(new RegExp(bg, "g"));
      score += matches ? matches.length : 0;
    }
    return score;
  }

  // === AUTOMATIC SHIFT DETECTION ===
  function guessShiftByLanguage(cipher) {
    let best = { shift: 0, chi: Infinity, tie: -Infinity, plaintext: "" };
    for (let s = 0; s < 26; s++) { // Try all possible shifts
      const candidate = caesar(cipher, 26 - s); // Decrypt with reverse shift
      const L = onlyLettersLower(candidate); // Cleans the text
      const obs = freqCounts(L); // Observed frequencies
      const expPerc = Array(26).fill(0).map((_, i) => ENGLISH_FREQ[indexToLetter(i)]); // Expected frequencies
      const chi = chiSquare(obs, expPerc); // Calculate chi²
      const tie = bigramScore(candidate); // Calculate bigram score
      // If chi² is smaller (or the same but with more common bigrams), it is a better solution
      const better = (chi < best.chi) || (chi === best.chi && tie > best.tie);
      if (better) best = { shift: s, chi, tie, plaintext: candidate };
    }
    return best;
  }

  // Writes text to an HTML element using id
  function write(id, text) {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
  }

  // ===== MAIN =====
  // Cleans up the text by keeping only the letters
  const letters = onlyLettersLower(TEXT);
  // Calculate the absolute frequency of letters
  const counts = freqCounts(letters);
  // Sort the distribution by decreasing frequency
  const sortedCounts = sortFreqObject(freqObjectFromCounts(counts));

  // Encrypt the text with the Caesar Cipher (shift defined above)
  const encrypted = caesar(TEXT, SHIFT);
  // Attempts to guess the shift and decrypt automatically
  const guess = guessShiftByLanguage(encrypted);

  // ===== WRITING RESULTS ON THE PAGE =====
  // Distribution of ordered letters
  write("caesar-distribution", sortedCounts.map(([k,v]) => `${k}: ${v}`).join("\n"));
  // Ciphertext
  write("caesar-encrypted", encrypted);
  // Text automatically decrypted
  write("caesar-decrypted", guess.plaintext);
  // About automatic analysis
  write("caesar-info", `Guessed shift: ${guess.shift} | Chi²: ${guess.chi.toFixed(2)} | Bigram score: ${guess.tie}`);
})();
  </code></pre>
</div>

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
