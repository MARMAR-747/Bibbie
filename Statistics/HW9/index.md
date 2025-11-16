---
layout: default
title: HW8
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

<h1 class="prob-title"><span class="prob-emoji">🎲</span> Interpretations of Probability and the Axiomatic Framework</h1>

<p>
This homework reviews the main interpretations of probability, explains how the axiomatic approach resolves conceptual inconsistencies, explores the link with measure theory, and derives important consequences such as subadditivity and the inclusion–exclusion principle.
</p>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  1. MAIN INTERPRETATIONS OF PROBABILITY                          -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">📘</span> 1. Main Interpretations of Probability</h2>

<p class="prob-quote">
  “What does it mean for an event to have probability <i>p</i>?”
</p>

<p>The four classical interpretations plus the modern axiomatic approach:</p>

<ul class="prob-list">
  <li><strong>Classical (Laplace)</strong></li>
  <li><strong>Frequentist</strong></li>
  <li><strong>Bayesian</strong></li>
  <li><strong>Geometric</strong></li>
  <li><strong>Axiomatic (Kolmogorov)</strong></li>
</ul>

<div class="prob-separator"></div>

<!-- CLASSICAL -->
<h3 class="prob-subtitle"><span class="prob-emoji">🎲</span> 1.1 Classical Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition</div>
  <div class="prob-premium-content">
    Under symmetry:
    <div class="prob-math">
      $$P(A)=\frac{|A|}{|\Omega|}$$
    </div>
  </div>
</div>

<p><strong>Pros:</strong> simple, intuitive.<br>
<strong>Cons:</strong> limited to symmetric finite models.</p>

<div class="prob-separator"></div>

<!-- FREQUENTIST -->
<h3 class="prob-subtitle"><span class="prob-emoji">🔁</span> 1.2 Frequentist Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition</div>
  <div class="prob-premium-content">
    Probability as long-run frequency:
    <div class="prob-math">
      $$P(A)=\lim_{n\to\infty}\frac{1}{n}\sum_{i=1}^{n}\mathbf{1}_A(\omega_i)$$
    </div>
  </div>
</div>

<p><strong>Pros:</strong> objective, empirical.<br>
<strong>Cons:</strong> cannot assign probability to single non-repeatable events.</p>

<div class="prob-separator"></div>

<!-- BAYESIAN -->
<h3 class="prob-subtitle"><span class="prob-emoji">🧠</span> 1.3 Bayesian Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition</div>
  <div class="prob-premium-content">
    Probability as a degree of belief:
    <div class="prob-math">
      $$P(H\mid D)=\frac{P(D\mid H)P(H)}{P(D)}$$
    </div>
  </div>
</div>

<p><strong>Pros:</strong> handles uncertainty and single events.<br>
<strong>Cons:</strong> subjectivity of priors.</p>

<div class="prob-separator"></div>

<!-- GEOMETRIC -->
<h3 class="prob-subtitle"><span class="prob-emoji">📏</span> 1.4 Geometric Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition</div>
  <div class="prob-premium-content">
    Based on geometric measure:
    <div class="prob-math">
      $$P(A)=\frac{\mu(A)}{\mu(\Omega)}$$
    </div>
  </div>
</div>

<p>Useful in spatial/statistical physics; motivates measure theory.</p>

<div class="prob-separator"></div>

<!-- AXIOMATIC -->
<h3 class="prob-subtitle"><span class="prob-emoji">📐</span> 1.5 Axiomatic Probability (Kolmogorov)</h3>

<p>A probability space is:</p>

<div class="prob-math">
  $$(\Omega, \mathcal{F}, P)$$
</div>

<div class="prob-premium-box">
  <div class="prob-premium-title">Kolmogorov’s Axioms</div>
  <div class="prob-premium-content">
    <ul class="prob-list">
      <li>Non-negativity: $$P(A)\ge 0$$</li>
      <li>Normalization: $$P(\Omega)=1$$</li>
      <li>Countable additivity:
        <div class="prob-math">
          $$P\Big(\bigcup A_i\Big)=\sum P(A_i)\ \text{for disjoint }A_i$$
        </div>
      </li>
    </ul>
  </div>
</div>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  2. AXIOMATIC RESOLUTION OF CONFLICTS                           -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">🧩</span> 2. How the Axiomatic Framework Resolves Conflicts</h2>

<p>
By formalizing probability as a measure on a sigma-algebra, all interpretations must satisfy the same structural rules.
This removes classical paradoxes and contradictions across philosophical views.
</p>

<ul class="prob-list">
  <li>Geometric, frequentist, and Bayesian probabilities become consistent.</li>
  <li>Discrete and continuous models obey the same axioms.</li>
  <li>Impossible pathologies (e.g., non-measurable sets) are excluded.</li>
</ul>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  3. PROBABILITY & MEASURE THEORY                                -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">📚</span> 3. Probability & Measure Theory</h2>

<h3 class="prob-subtitle">3.1 Sigma-Algebras</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition</div>
  <div class="prob-premium-content">
    A sigma-algebra $\mathcal{F}$ is a collection of subsets of $\Omega$ closed under complements and countable unions.
  </div>
</div>

<!-- INTERACTIVE SIGMA ALGEBRA SVG -->
<h3 class="prob-subtitle"><span class="prob-emoji">🌀</span> Interactive Sigma-Algebra Diagram</h3>

<div id="sigmaDiagram" style="max-width:500px;margin:auto;">
  <svg viewBox="0 0 300 200" style="width:100%;">
    <rect x="10" y="10" width="280" height="180" rx="15" 
          id="omegaBox"
          fill="rgba(100,150,255,0.15)" stroke="#4a8ef5" stroke-width="3"></rect>
    <text x="150" y="30" text-anchor="middle" font-size="18" fill="var(--textColor)">Ω</text>

    <ellipse cx="120" cy="110" rx="60" ry="40"
             id="setA"
             fill="rgba(255,100,120,0.20)" stroke="#ff6f87" stroke-width="2"></ellipse>
    <text x="120" y="115" text-anchor="middle" fill="var(--textColor)">A</text>

    <ellipse cx="200" cy="110" rx="60" ry="40"
             id="setAc"
             fill="rgba(120,255,160,0.20)" stroke="#52cc7a" stroke-width="2"></ellipse>
    <text x="200" y="115" text-anchor="middle" fill="var(--textColor)">A<sup>c</sup></text>
  </svg>
</div>

<p style="text-align:center;font-size:0.9rem;opacity:0.8;">
Hover over A or A<sup>c</sup> to highlight complementarity.
</p>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  4. SUBADDITIVITY                                               -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">🧮</span> 4. Subadditivity</h2>

<div class="prob-math">
  $$P(A\cup B)\le P(A)+P(B)$$
</div>

<div class="prob-premium-box">
  <div class="prob-premium-title">Proof</div>
  <div class="prob-premium-content">
    $$A\cup B = A \cup (B\setminus A)$$
    These sets are disjoint, so:
    $$P(A\cup B)=P(A)+P(B\setminus A)\le P(A)+P(B).$$
  </div>
</div>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  5. INCLUSION–EXCLUSION (INTERACTIVE)                           -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">🔗</span> 5. Inclusion–Exclusion Principle</h2>

<div id="vennContainer" style="max-width:350px;margin:auto;">
  <svg id="vennSVG" viewBox="0 0 200 140" style="width:100%;">
    <circle id="vennA" cx="75" cy="75" r="50"
            fill="rgba(255,110,110,0.25)" stroke="#ff6f6f" stroke-width="2"></circle>
    <circle id="vennB" cx="125" cy="75" r="50"
            fill="rgba(110,180,255,0.25)" stroke="#6eb4ff" stroke-width="2"></circle>
    <text x="55" y="70" fill="var(--textColor)">A</text>
    <text x="140" y="70" fill="var(--textColor)">B</text>
  </svg>
</div>

<p style="text-align:center;font-size:0.9rem;opacity:0.8;">
Click A, B, or the overlap to highlight the corresponding term.
</p>

<div id="inExFormula" class="prob-math" style="text-align:center;">
  $$P(A\cup B)=P(A)+P(B)-P(A\cap B)$$
</div>

<div class="prob-separator"></div>

<h2 class="prob-subtitle"><span class="prob-emoji">🌟</span> Summary</h2>

<ul class="prob-list">
  <li>Probability has multiple interpretations (classical, frequentist, Bayesian, geometric).</li>
  <li>The axiomatic approach unifies them mathematically.</li>
  <li>Measure theory provides rigorous foundations via $(\Omega,\mathcal{F},P)$.</li>
  <li>Subadditivity and inclusion–exclusion follow directly from the axioms.</li>
</ul>

<!-- =============================================================== -->
<!-- JAVASCRIPT FOR INTERACTIVITY                                    -->
<!-- =============================================================== -->

<script>
// Sigma algebra hover effect
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("setA");
  const Ac = document.getElementById("setAc");

  A.addEventListener("mouseenter", () => Ac.style.opacity = "0.3");
  A.addEventListener("mouseleave", () => Ac.style.opacity = "1");

  Ac.addEventListener("mouseenter", () => A.style.opacity = "0.3");
  Ac.addEventListener("mouseleave", () => A.style.opacity = "1");
});
</script>

<script>
// Inclusion–Exclusion interactive Venn
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("vennA");
  const B = document.getElementById("vennB");
  const formula = document.getElementById("inExFormula");

  function renderLatex(latex){
    formula.innerHTML = "$$" + latex + "$$";
    if (window.MathJax) MathJax.typesetPromise();
  }

  // Click A
  A.addEventListener("click", e => {
    A.style.opacity = "1";
    B.style.opacity = "0.3";
    renderLatex("P(A)");
    e.stopPropagation();
  });

  // Click B
  B.addEventListener("click", e => {
    A.style.opacity = "0.3";
    B.style.opacity = "1";
    renderLatex("P(B)");
    e.stopPropagation();
  });

  // Intersection click detection
  document.getElementById("vennSVG").addEventListener("click", (e) => {
    const x = e.offsetX;
    const y = e.offsetY;
    const inA = (x-75)**2 + (y-75)**2 <= 50**2;
    const inB = (x-125)**2 + (y-75)**2 <= 50**2;

    if (inA && inB) {
      A.style.opacity = "1";
      B.style.opacity = "1";
      renderLatex("P(A \\cap B)");
    }
  });

  // Reset on double click
  document.getElementById("vennSVG").addEventListener("dblclick", () => {
    A.style.opacity = "1";
    B.style.opacity = "1";
    renderLatex("P(A \\cup B)=P(A)+P(B)-P(A \\cap B)");
  });
});
</script>

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
