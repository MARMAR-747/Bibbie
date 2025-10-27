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

RSA (Rivest–Shamir–Adleman) is one of the most widely used public-key cryptographic systems.
It allows two parties to exchange information securely without sharing a secret key in advance.
The strength of RSA lies in the difficulty of factoring large numbers into their prime components.

<hr style="margin-top: 2rem; margin-bottom: 1rem;">

## ⚙️ How RSA Works — Step by Step

RSA is based on number theory and the properties of modular arithmetic.
Let’s go through its core concepts step by step.

### 1️⃣ Choose two prime numbers

We begin by selecting two distinct prime numbers p and q.
For learning purposes, we’ll use small primes (e.g., p = 41, q = 53),
but in real applications, these primes are extremely large (hundreds of digits long).

### 2️⃣ Compute the modulus and Euler’s totient

We compute:

n = p × q = 41 × 53 = 2173
φ(n) = (p-1) × (q-1) = (40 × 52) = 2080

These values are fundamental to the RSA system.

### 3️⃣ Choose the public exponent *e*

We now select an integer *e* that is:

- greater than 1 and less than φ(n)
- coprime with φ(n) (they share no common divisors other than 1)

For example:
e = 17
gcd(17, 2080) = 1 ✅

### 4️⃣ Compute the private exponent *d*

The private exponent d is the modular inverse of e with respect to φ(n):

d × e ≡ 1 mod φ(n)

To find d, we use the Extended Euclidean Algorithm, which allows us to find integers
x and y such that:

a × x + b × y = gcd(a,b)

When a = e and b = φ(n), the value x gives us the modular inverse d.

For example:

e = 17,  φ(n) = 2080
→ d = 367  (since 17 × 367 ≡ 1 mod 2080)

### 5️⃣ Create the keys

- **Public key** (shared with everyone): (n,e)
- **Private key** (kept secret): (n,d)

Public key  → (n=2173, e=17)
Private key → (n=2173, d=367)

### 6️⃣ Encryption and Decryption

To encrypt a message m (represented as a number):

c = m^e mod n

To decrypt it:

m = c^d mod n

Because of the mathematical relationship between e, d, and φ(n),
this process perfectly reverses the encryption operation —
the decrypted message m is identical to the original one.

## 💡 Why RSA Works

RSA relies on the fact that it’s easy to multiply two large primes,
but extremely hard to factor their product n back into p and q.

Without knowing p and q, it’s practically impossible (for large enough numbers)
to compute φ(n) and thus find the private key d.

This asymmetry — easy to compute in one direction, hard to reverse —
is what makes RSA a secure one-way function.

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
