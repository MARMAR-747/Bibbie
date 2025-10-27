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
// assets/js/hw3_rsa.js
/*
Top-level IIFE (Immediately Invoked Function Expression)
- Encapsulates all functions and variables to avoid polluting the global namespace.
- When this script runs in a browser, it performs:
  1) small-key RSA key generation (for demo),
  2) letter-by-letter RSA encryption of a demo plaintext,
  3) decryption check using the private key,
  4) an attack routine that brute-forces small primes and candidate e values,
     using chi-square and common-bigram scoring to pick the best plaintext guess.
- Suitable for classroom demonstration of RSA basics and statistical cryptanalysis,
  but NOT secure for real cryptographic use (see security notes below).
*/
(function () {
  // ===== Helpers: modular arithmetic =====

  /*
  Extended Euclidean Algorithm (recursive)
  - Inputs: integers a, b
  - Returns: [g, x, y] where g = gcd(a, b) and x,y satisfy a*x + b*y = g
  - Implementation detail: returns [a,1,0] when b === 0 (base case).
  - Used to compute modular multiplicative inverses via x when gcd(a, m) == 1.
  - Complexity: O(log(min(a,b))) recursion depth.
  */
  function egcd(a, b) {
    if (b === 0) return [a, 1, 0];
    const [g, x1, y1] = egcd(b, a % b);
    return [g, y1, x1 - Math.floor(a / b) * y1];
  }

  /*
  Modular inverse using extended GCD
  - Inputs: a, m
  - If gcd(a,m) != 1, inverse doesn't exist -> returns null.
  - Otherwise returns x such that (a * x) % m === 1 (in the range 0..m-1).
  - Note: uses egcd and then normalizes x into positive residue class.
  */
  function modinv(a, m) {
    const [g, x] = egcd(a, m);
    if (g !== 1) return null;
    return ((x % m) + m) % m;
  }

  /*
  Modular exponentiation (binary exponentiation / square-and-multiply)
  - Computes (base^exp) % mod efficiently with O(log exp) multiplications.
  - Uses bitwise operations on the exponent (e & 1) and repeated squaring.
  - Works for non-negative integer exponents.
  - Important for RSA encryption/decryption where exp is e or d.
  */
  function modPow(base, exp, mod) {
    let res = 1 % mod, b = base % mod, e = exp;
    while (e > 0) {
      if (e & 1) res = (res * b) % mod;
      b = (b * b) % mod;
      e >>= 1;
    }
    return res;
  }

  /*
  Simple primality test (trial division)
  - Input: integer n
  - Returns true if n is prime, false otherwise.
  - Implementation: checks divisibility by 2, then odd divisors up to sqrt(n).
  - Complexity: O(sqrt(n)) — fine for small demo primes, not for real crypto primes.
  - WARNING: not suitable for production or large primes.
  */
  function isPrime(n){
    if (n < 2) return false;
    if (n % 2 === 0) return n === 2;
    for (let i = 3; i * i <= n; i += 2) if (n % i === 0) return false;
    return true;
  }

  // ===== Alphabet mapping & English frequency stats =====

  /*
  Constants and helpers for mapping letters ↔ numeric values and for scoring.
  - A: char code for 'a' used to convert between chars and indices 0..25.
  - ENGLISH_FREQ: approximate letter frequencies as percentages (A-Z) for scoring.
  - COMMON_BG: small list of common English bigrams used to boost candidate plaintexts
    that contain frequent pairings like "th", "he", etc.
  - onlyLettersLower: normalizes input string to lowercase and removes non a-z characters.
  - idx2ch / ch2idx: convert numeric index (0..25) to letter and vice versa.
  */
  const A = "a".charCodeAt(0);
  const ENGLISH_FREQ = { // %
    a: 8.167,b: 1.492,c: 2.782,d: 4.253,e:12.702,f: 2.228,g: 2.015,
    h: 6.094,i: 6.966,j: 0.153,k: 0.772,l: 4.025,m: 2.406,n: 6.749,
    o: 7.507,p: 1.929,q: 0.095,r: 5.987,s: 6.327,t: 9.056,u: 2.758,
    v: 0.978,w: 2.360,x: 0.150,y: 1.974,z: 0.074
  };
  const COMMON_BG = ["th","he","in","er","an","re","on","at","en","nd"];

  const onlyLettersLower = s => s.toLowerCase().replace(/[^a-z]/g, "");
  const idx2ch = i => String.fromCharCode(A + i);
  const ch2idx = ch => ch.charCodeAt(0) - A;

  // ===== Key generation (small primes for demo) =====

  /*
  keygen(p, q, e)
  - Inputs: two primes p,q and public exponent e (default 17).
  - Computes:
      n = p * q
      phi = (p - 1) * (q - 1)
      checks that gcd(e, phi) == 1
      d = modular inverse of e mod phi
  - Returns an object { n, e, d, p, q, phi }.
  - Throws Error if e is not coprime with phi.
  - Important caveats:
      * Using small primes (like in this demo) is insecure.
      * In real RSA, e is usually chosen 65537 and p,q are large (>= 2048-bit combined).
  */
  function keygen(p, q, e = 17) {
    const n = p * q;
    const phi = (p - 1) * (q - 1);
    if (egcd(e, phi)[0] !== 1) throw new Error("e not coprime with phi");
    const d = modinv(e, phi);
    return { n, e, d, p, q, phi };
  }

  // ===== RSA letter-by-letter (0..25) =====

  /*
  rsaEncryptLetters(plain, n, e)
  - Encrypts plaintext letter-by-letter mapping a->0, b->1, ..., z->25.
  - onlyLettersLower(plain) removes non-letters and lowercases input.
  - For each letter m in 0..25 compute c = m^e mod n and push to output array.
  - Returns array of integers (ciphertext values).
  - Important: This is *textbook RSA* applied to tiny messages (single-letter integers),
    which is *not secure*: small modulus and deterministic mapping leak massive information.
  - Also if n <= 25, many ciphertexts collide — code guards n > 26 in attack phase.
  */
  function rsaEncryptLetters(plain, n, e) {
    const out = [];
    for (const ch of onlyLettersLower(plain)) {
      const m = ch2idx(ch);          // 0..25
      const c = modPow(m, e, n);     // m^e mod n
      out.push(c);
    }
    return out; // array of integers
  }

  /*
  rsaDecryptLetters(nums, n, d)
  - For each ciphertext integer c in nums compute m = c^d mod n and convert to a char.
  - If resulting m is not in [0..25], returns "?" for that position.
  - Returns the reconstructed string (joined letters).
  - This assumes sender encoded plaintext as 0..25. Any deviation yields "?" marks.
  */
  function rsaDecryptLetters(nums, n, d) {
    return nums.map(c => {
      const m = modPow(c, d, n);
      return (m >= 0 && m <= 25) ? idx2ch(m) : "?";
    }).join("");
  }

  // ===== Linguistic scoring (chi-square + bigrams) =====

  /*
  chiSquareFromText(text)
  - Computes chi-squared statistic comparing letter counts of `text` to expected
    English letter frequencies (ENGLISH_FREQ).
  - Steps:
      * normalize text to letters only (lowercase)
      * count occurrences for each letter
      * compute expected count = (freq% / 100) * N
      * accumulate sum((obs - exp)^2 / exp)
  - Returns numeric chi-squared; lower values indicate closer match to expected English.
  - Edge cases: N = 0 => uses N = 1 to avoid division by zero.
  - Use: statistical filter to prefer plaintexts whose letter distribution resembles English.
  */
  function chiSquareFromText(text) {
    const s = onlyLettersLower(text);
    const counts = new Array(26).fill(0);
    for (const c of s) counts[ch2idx(c)]++;
    const N = counts.reduce((a,b)=>a+b,0) || 1;
    let chi = 0;
    for (let i=0;i<26;i++){
      const exp = (ENGLISH_FREQ[idx2ch(i)]/100)*N;
      if (exp > 0) {
        const diff = counts[i] - exp;
        chi += (diff*diff)/exp;
      }
    }
    return chi;
  }

  /*
  bigramScore(text)
  - Simple heuristic: counts occurrences of a short list of common bigrams (COMMON_BG).
  - Returns cumulative count of occurrences; higher is better.
  - This acts as tie-breaker or secondary score in attack routine to favor realistic English pairs.
  */
  function bigramScore(text) {
    const s = onlyLettersLower(text);
    let score = 0;
    for (const bg of COMMON_BG) {
      const m = s.match(new RegExp(bg, "g"));
      score += m ? m.length : 0;
    }
    return score;
  }

  // ===== Attack: try small (p,q) and typical e values, evaluate with Chi² + BG =====

  /*
  parseCipher(str)
  - Utility: converts a space-separated string of numbers into an array of Number.
  - Trims whitespace and filters out empty tokens.
  - Used when the ciphertext is stored/printed as a single string.
  */
  function parseCipher(str){
    return str.trim().split(/\s+/).filter(Boolean).map(Number);
  }

  /*
  attackRSA(cipherStr)
  - Attempts to cryptanalyze the ciphertext produced by rsaEncryptLetters, assuming:
      * plaintext letters were mapped to 0..25,
      * modulus n = p*q where p and q are small primes (between 29 and 101 here),
      * e is chosen from a small list of candidate exponents.
  - Strategy:
      1) parse ciphertext into array C.
      2) iterate over pairs of distinct small primes p,q (both prime, range 29..101).
      3) form n = p*q; skip if n <= 26 (would always map to small residues badly).
      4) try candidate e values [3,5,7,11,17], compute d using keygen/modinv.
      5) decrypt all ciphertext integers with d; if any decrypted m not in 0..25 produce '?'
         and those candidates are discarded.
      6) score resulting plaintext with chi-square and bigram heuristics.
      7) keep best-scoring plaintext (lowest chi², tiebreak: highest bigram count).
  - Returns an object `best` which contains:
      { score: chiSquare, tie: bigramCount, key: {p,q,e,n,d}, plain: "plaintext" }
  - Notes on effectiveness:
      * Works well when primes are small and plaintext is standard English.
      * Computational cost: O(P^2 * E * L * log d) where P ~ number of smallPrimes,
        E ~ number of eCandidates, L ~ ciphertext length; but P here is small ~ 20 primes.
      * This is a pedagogical demonstration of frequency analysis + brute force.
  */
  function attackRSA(cipherStr) {
    const C = parseCipher(cipherStr);
    const smallPrimes = [];
    for (let x=29; x<=101; x++) if (isPrime(x)) smallPrimes.push(x);
    const eCandidates = [3,5,7,11,17];

    let best = {score: Infinity, tie: -Infinity, key: null, plain: ""};

    for (const p of smallPrimes) {
      for (const q of smallPrimes) {
        if (q === p) continue;
        const n = p*q;
        if (n <= 26) continue; // avoid ridiculous modulus that cannot represent 0..25 distinctly
        for (const e of eCandidates) {
          try {
            const { d } = keygen(p,q,e);
            if (d == null) continue;
            const plain = rsaDecryptLetters(C, n, d);
            if (plain.includes("?")) continue; // discard candidates that map outside 0..25
            const chi = chiSquareFromText(plain);
            const bgs = bigramScore(plain);
            const better = (chi < best.score) || (chi === best.score && bgs > best.tie);
            if (better) best = {score: chi, tie: bgs, key: {p,q,e,n,d}, plain};
          } catch(_) { /* invalid key (e not coprime with phi) => ignore */ }
        }
      }
    }
    return best;
  }

  // ===== Demo (modify DEMO_TEXT for your text) =====

  /*
  DEMO_TEXT
  - Long multi-paragraph English text used for demonstration of encryption / attack.
  - This text is preprocessed by rsaEncryptLetters -> only alphabetic characters are used.
  - If you change DEMO_TEXT to include accents, punctuation, digits, they will be stripped before encryption.
  */
  const DEMO_TEXT = `Freedom is not a starting point but a destination.
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

  /*
  demoKey = keygen(41,53,17)
  - Constructs a small private/public RSA keypair for the demo.
  - p=41 and q=53 are small primes; e=17 chosen so gcd(e,phi)==1.
  - This yields n = 2173 (41*53) and a private exponent d computed via modinv.
  - This is intentionally small for demonstration; do not use in real systems.
  */
  const demoKey = keygen(41,53,17);              // small primes, example
  const cipherNums = rsaEncryptLetters(DEMO_TEXT, demoKey.n, demoKey.e);
  const cipherStr  = cipherNums.join(" ");

  // ===== Writing to the page =====

  /*
  write(id, text)
  - Utility to set textContent of DOM element with given id, if present.
  - This allows a simple demo page to show plaintext, public key, ciphertext, decrypted text, and attack result.
  - If running this file in a non-browser environment (e.g., Node) these writes silently do nothing.
  */
  function write(id, text) { const el = document.getElementById(id); if (el) el.textContent = text; }

  // Populate DOM placeholders (optional)
  write("rsa-plaintext", DEMO_TEXT);
  write("rsa-public", `n=${demoKey.n}, e=${demoKey.e}  (p=${demoKey.p}, q=${demoKey.q})`);
  write("rsa-cipher", cipherStr);

  // Decipher with the private key (simple correctness check)
  const decrypted = rsaDecryptLetters(cipherNums, demoKey.n, demoKey.d);
  write("rsa-decrypted", decrypted);

  // Perform the attack (statistical brute force) without knowing the private key
  const guess = attackRSA(cipherStr);
  if (guess && guess.key) {
    write("rsa-attack-plain", guess.plain);
    write("rsa-attack-key", `Best guess → p=${guess.key.p}, q=${guess.key.q}, e=${guess.key.e}, n=${guess.key.n}, d=${guess.key.d}  |  Chi²=${guess.score.toFixed(2)}  BG=${guess.tie}`);
  }

  // End of IIFE
})();

/* ============================
   Additional notes & recommendations
   - Security:
     * This script uses extremely small primes and encodes letters as raw integers 0..25.
       That is fully insecure: frequency patterns remain obvious and moduli are trivially factorable.
     * In real RSA:
         - Use large random primes (each hundreds or thousands of bits).
         - Use proper padding (OAEP for encryption, PSS for signatures).
         - Use cryptographic libraries — do not implement RSA primitives yourself unless for learning.
   - Pedagogy:
     * The attack demonstrates that textbook RSA without padding leaks structure and can be broken by brute-force on small keys.
     * Chi-square testing and bigram scoring are classic statistical methods to distinguish candidate decryptions.
   - Performance:
     * modPow is efficient (O(log exp)), but egcd and isPrime used here are fine for small numbers only.
     * For larger primes and production use, use specialized big-integer libraries (e.g., BigInt in modern JS or crypto libraries).
   ============================ */
  </code></pre>
</div>

<h3>🔐 RSA on Letters — Demo</h3>

<p><strong>Public key (example):</strong></p>
<pre id="rsa-public" class="caesar-output"></pre>

<p><strong>Ciphertext (space-separated integers):</strong></p>
<pre id="rsa-cipher" class="typewriter"></pre>

<p><strong>Decryption with private key (check):</strong></p>
<pre id="rsa-decrypted" class="typewriter"></pre>

<hr class="big-divider">

<h3>🧠 Statistical Decoding (no keys known)</h3>
<p>We brute small <em>p, q</em> and a few public exponents <em>e</em>, decrypt each candidate, and score the result with <strong>Chi-Square</strong> vs English letter distribution (+ <strong>bigram</strong> tiebreak).</p>

<p><strong>Best guess — plaintext:</strong></p>
<pre id="rsa-attack-plain" class="typewriter"></pre>

<p><strong>Best guess — key & score:</strong></p>
<pre id="rsa-attack-key" class="caesar-output"></pre>

<script src="{{ 'Statistics/HW3/assets/js/hw3_rsa.js' | relative_url }}" defer></script>

---

### 🔎In brief 

**Purpose**: Educational demonstration of a minimal RSA pipeline and a ciphertext-only statistical attack. Intended for teaching and lab use, not secure for production.

**Main components**:
- **Math utilities:** extended GCD, modular inverse, and fast modular exponentiation (`egcd`, `modinv`, `modPow`).
- **Alphabet mapping:** naive mapping `a → 0 … z → 25` and simple primality test for small primes.
- **Key generation:** `keygen(p, q, e)` computes `n`, `phi`, and private exponent `d`.
- **Encryption / Decryption:** `rsaEncryptLetters` and `rsaDecryptLetters` operate letter-by-letter using `m^e mod n` and `c^d mod n`.
- **Scoring:** `chiSquareFromText` (letter-distribution) plus `bigramScore` (common bigrams) to evaluate English-likeness.
- **Attack:** `attackRSA` brute-forces small prime pairs and candidate `e` values, decrypts candidates, and selects the best plaintext by chi-squared (tie-breaker: bigram counts).
- **Demo flow:** generates a small keypair, encrypts a sample paragraph, checks decryption, and runs the statistical attack; results are written to page placeholders.

**Security caveats**:
- Uses very small primes and encodes single letters as integers — **insecure**.
- No padding (OAEP/PSS) and no use of modern big-integer handling.
- For realistic RSA: use large primes (proper bit-length), BigInt or crypto libraries, Miller–Rabin primality testing, and standard padding schemes.

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
