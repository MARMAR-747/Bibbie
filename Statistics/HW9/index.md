---
layout: default
title: HW9
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
This homework reviews the main interpretations of probability, explains how the axiomatic approach resolves apparent conceptual inconsistencies, explores the link with measure theory, and derives important consequences such as subadditivity and the inclusion–exclusion principle directly from the axioms.
</p>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  1. MAIN INTERPRETATIONS OF PROBABILITY                          -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">📘</span> 1. Main Interpretations of Probability</h2>

<p class="prob-quote">
  “What does it mean for an event to have probability <i>p</i>?”
</p>

<p>
Historically, probability has been given different meanings, depending on the context and philosophical viewpoint. 
However, all these interpretations are intended to describe the same object: a function assigning numbers between 0 and 1 to events.
Here we briefly review the classical, frequentist, Bayesian, geometric and axiomatic viewpoints.
</p>

<ul class="prob-list">
  <li><strong>Classical (Laplace):</strong> probability as a ratio of favourable over total equally likely cases.</li>
  <li><strong>Frequentist:</strong> probability as a long-run relative frequency in repeated experiments.</li>
  <li><strong>Bayesian:</strong> probability as degree of belief, updated using Bayes’ theorem.</li>
  <li><strong>Geometric:</strong> probability as the ratio of lengths/areas/volumes within a continuous space.</li>
  <li><strong>Axiomatic (Kolmogorov):</strong> probability as a measure on a sigma-algebra.</li>
</ul>

<div class="prob-separator"></div>

<!-- CLASSICAL -->
<h3 class="prob-subtitle"><span class="prob-emoji">🎲</span> 1.1 Classical Probability (Laplace)</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Laplace)</div>
  <div class="prob-premium-content">
    Under the assumption that all outcomes in a finite sample space $\Omega$ are equally likely, the probability of an event $A \subseteq \Omega$ is
    <div class="prob-math">
      $$P(A)=\frac{|A|}{|\Omega|}.$$
    </div>
  </div>
</div>

<p>
This coincides with the intuitive idea used in card games, dice, and roulette: each elementary outcome has the same chance, so probabilities are just normalized counts.
</p>

<p>
<strong>Advantages.</strong> Very simple, easy to apply in symmetric and finite settings.<br>
<strong>Limitations.</strong> It does not cover:
</p>
<ul class="prob-list">
  <li>continuous sample spaces (e.g., random points on a segment),</li>
  <li>situations where symmetry is not justified or not obvious,</li>
  <li>infinite sets of outcomes.</li>
</ul>

<div class="prob-separator"></div>

<!-- FREQUENTIST -->
<h3 class="prob-subtitle"><span class="prob-emoji">🔁</span> 1.2 Frequentist Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Frequentist)</div>
  <div class="prob-premium-content">
    Probability is defined as the limit of the relative frequency of an event $A$ in an infinite sequence of independent repetitions:
    <div class="prob-math">
      $$P(A)=\lim_{n\to\infty}\frac{1}{n}\sum_{i=1}^{n}\mathbf{1}_A(\omega_i),$$
    </div>
    where $\mathbf{1}_A(\omega_i)$ is 1 if the $i$-th outcome falls in $A$, and 0 otherwise.
  </div>
</div>

<p>
The frequentist interpretation is operational: in principle we estimate probabilities by running many identical trials and looking at empirical frequencies.
</p>

<p>
<strong>Advantages.</strong> Closely tied to experimental data and objective procedures.<br>
<strong>Limitations.</strong> It struggles with:
</p>
<ul class="prob-list">
  <li>events that are not repeatable (e.g. “probability a particular person becomes president”),</li>
  <li>finite sequences of data (in practice the limit as $n \to \infty$ is never observed exactly).</li>
</ul>

<div class="prob-separator"></div>

<!-- BAYESIAN -->
<h3 class="prob-subtitle"><span class="prob-emoji">🧠</span> 1.3 Bayesian Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Bayesian)</div>
  <div class="prob-premium-content">
    Probability is interpreted as a <strong>degree of belief</strong>, or subjective uncertainty, about a hypothesis $H$. When new data $D$ arrive, beliefs are updated using Bayes’ theorem:
    <div class="prob-math">
      $$P(H\mid D)=\frac{P(D\mid H)P(H)}{P(D)}.$$
    </div>
  </div>
</div>

<p>
Here:
</p>
<ul class="prob-list">
  <li>$P(H)$ is the <strong>prior</strong> probability of the hypothesis,</li>
  <li>$P(D\mid H)$ is the <strong>likelihood</strong> of observing the data under $H$,</li>
  <li>$P(H\mid D)$ is the <strong>posterior</strong> probability after observing $D$.</li>
</ul>

<p>
<strong>Advantages.</strong> Handles one-off events, incorporates prior knowledge, and is central in modern machine learning and decision theory.<br>
<strong>Limitations.</strong> The choice of prior can be subjective and sometimes controversial; computationally demanding in complex models.
</p>

<div class="prob-separator"></div>

<!-- GEOMETRIC -->
<h3 class="prob-subtitle"><span class="prob-emoji">📏</span> 1.4 Geometric Probability</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Geometric)</div>
  <div class="prob-premium-content">
    In geometric settings, probability is defined in terms of a notion of <em>size</em> (length, area, volume, Lebesgue measure):
    <div class="prob-math">
      $$P(A)=\frac{\mu(A)}{\mu(\Omega)},$$
    </div>
    where $\mu$ is a measure on $\Omega$.
  </div>
</div>

<p>
Examples include choosing a point uniformly at random in an interval or region, and asking for the probability that it falls in a subregion.
</p>

<p>
This framework naturally leads to measure theory, because we need a consistent notion of “size” on possibly complicated sets.
</p>

<div class="prob-separator"></div>

<!-- AXIOMATIC -->
<h3 class="prob-subtitle"><span class="prob-emoji">📐</span> 1.5 Axiomatic Probability (Kolmogorov)</h3>

<p>
The axiomatic approach, introduced by Kolmogorov, abstracts away from any specific interpretation and defines probability purely in terms of a measure space.
</p>

<div class="prob-premium-box">
  <div class="prob-premium-title">Probability Space</div>
  <div class="prob-premium-content">
    A probability space is a triple
    <div class="prob-math">
      $$(\Omega, \mathcal{F}, P),$$
    </div>
    where:
    <ul class="prob-list">
      <li>$\Omega$ is the sample space,</li>
      <li>$\mathcal{F}$ is a sigma-algebra of subsets of $\Omega$,</li>
      <li>$P:\mathcal{F}\to[0,1]$ is a probability measure.</li>
    </ul>
  </div>
</div>

<div class="prob-premium-box">
  <div class="prob-premium-title">Kolmogorov’s Axioms</div>
  <div class="prob-premium-content">
    <ul class="prob-list">
      <li><strong>Non-negativity:</strong> $P(A)\ge 0$ for all $A\in\mathcal{F}$.</li>
      <li><strong>Normalization:</strong> $P(\Omega)=1$.</li>
      <li><strong>Countable additivity:</strong> if $(A_i)_{i=1}^\infty$ are pairwise disjoint,
        <div class="prob-math">
          $$P\Big(\bigcup_{i=1}^{\infty}A_i\Big)=\sum_{i=1}^{\infty}P(A_i).$$
        </div>
      </li>
    </ul>
  </div>
</div>

<p>
Any interpretation (classical, frequentist, Bayesian, geometric) is acceptable <em>only if</em> the probabilities it defines satisfy these axioms. In this way, philosophical differences are separated from the common mathematical structure.
</p>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  2. AXIOMATIC FRAMEWORK & CONCEPTUAL CONSISTENCY                -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">🧩</span> 2. How the Axiomatic Framework Resolves Inconsistencies</h2>

<p>
Each interpretation alone may suffer from conceptual issues:
</p>

<ul class="prob-list">
  <li>The classical view depends on sometimes unjustified symmetry assumptions.</li>
  <li>The frequentist view struggles with unique or non-repeatable events.</li>
  <li>The Bayesian view is criticized for subjective priors.</li>
  <li>Geometric probability can run into paradoxes if “areas” are not rigorously defined.</li>
</ul>

<p>
The axiomatic approach does not take any side in this debate. Instead, it makes a structural requirement:
</p>

<div class="prob-box">
  Any assignment of “probabilities” to events must be a function $P:\mathcal{F}\to[0,1]$ satisfying Kolmogorov’s axioms on some sigma-algebra $\mathcal{F}$.
</div>

<p>
As a consequence:
</p>
<ul class="prob-list">
  <li>
    Classical probability becomes a special case where $\Omega$ is finite and $P$ is the normalized counting measure.
  </li>
  <li>
    Frequentist probabilities, when they converge, define a measure $P$ satisfying the axioms.
  </li>
  <li>
    Bayesian priors and posteriors must be probability measures (e.g. on parameter spaces), and therefore obey additivity and normalization.
  </li>
  <li>
    Geometric probability is just Lebesgue measure (or similar) normalized to 1 over $\Omega$.
  </li>
</ul>

<p>
Thus, the axioms provide a single, coherent language in which all valid interpretations must agree. Conflicts that arise from informal reasoning are resolved by requiring consistency with this formal framework.
</p>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  3. PROBABILITY & MEASURE THEORY                                -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">📚</span> 3. Probability & Measure Theory</h2>

<p>
From a modern perspective, probability theory is simply <strong>measure theory with total mass 1</strong>. This viewpoint clarifies many concepts and allows the extension of probability to very general spaces.
</p>

<h3 class="prob-subtitle">3.1 Sigma-Algebras</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Sigma-Algebra)</div>
  <div class="prob-premium-content">
    A sigma-algebra $\mathcal{F}$ over $\Omega$ is a collection of subsets of $\Omega$ such that:
    <ul class="prob-list">
      <li>$\Omega\in\mathcal{F}$;</li>
      <li>if $A\in\mathcal{F}$, then $A^c\in\mathcal{F}$;</li>
      <li>if $(A_i)_{i=1}^\infty\subseteq\mathcal{F}$, then $\bigcup_{i=1}^\infty A_i\in\mathcal{F}$.</li>
    </ul>
  </div>
</div>

<p>
Intuitively, $\mathcal{F}$ is the collection of “events” to which we are allowed to assign probabilities. Closure under complements and countable unions ensures that standard set operations produce events that are still measurable.
</p>

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
Hover over A or A<sup>c</sup> to highlight their complementarity inside Ω.
</p>

<h3 class="prob-subtitle">3.2 Probability Measure</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Probability Measure)</div>
  <div class="prob-premium-content">
    A probability measure is a function
    <div class="prob-math">
      $$P:\mathcal{F}\to[0,1]$$
    </div>
    satisfying Kolmogorov’s axioms. It is simply a measure with total mass 1.
  </div>
</div>

<p>
Once $P$ is defined, we can compute probabilities of events, combine them, and develop concepts such as independence, conditional probability, and expectation.
</p>

<h3 class="prob-subtitle">3.3 Random Variables as Measurable Functions</h3>

<div class="prob-premium-box">
  <div class="prob-premium-title">Definition (Random Variable)</div>
  <div class="prob-premium-content">
    A random variable is a measurable function
    <div class="prob-math">
      $$X:\Omega\to\mathbb{R},$$
    </div>
    meaning that for every Borel set $B\subseteq\mathbb{R}$, the preimage
    <div class="prob-math">
      $$\{\omega\in\Omega:X(\omega)\in B\} \in \mathcal{F}.$$
    </div>
  </div>
</div>

<p>
This measurability condition guarantees that events defined by conditions on $X$ are still in $\mathcal{F}$ and hence have well-defined probabilities. The distribution of $X$ is then a new measure on $\mathbb{R}$, defined by:
</p>

<div class="prob-math">
  $$P_X(B)=P(X^{-1}(B)).$$
</div>

<p>
With this, expectations and variances become integrals with respect to $P$:
</p>

<div class="prob-math">
  $$\mathbb{E}[X]=\int_{\Omega}X\,dP,\qquad \mathrm{Var}(X)=\mathbb{E}[(X-\mathbb{E}[X])^2].$$
</div>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  4. SUBADDITIVITY                                               -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">🧮</span> 4. Subadditivity</h2>

<p>
Starting from the axioms, we can derive several useful properties. One fundamental consequence is <strong>subadditivity</strong>.
</p>

<div class="prob-premium-box">
  <div class="prob-premium-title">Theorem (Subadditivity)</div>
  <div class="prob-premium-content">
    For any events $A$ and $B$,
    <div class="prob-math">
      $$P(A\cup B)\le P(A)+P(B).$$
    </div>
  </div>
</div>

<h3 class="prob-subtitle">Proof</h3>

<div class="prob-box">
  <p>
    Consider the decomposition
    <div class="prob-math">
      $$A\cup B = A \cup (B\setminus A).$$
    </div>
    The sets $A$ and $B\setminus A$ are disjoint, so by finite additivity (which follows from countable additivity),
    <div class="prob-math">
      $$P(A\cup B)=P(A)+P(B\setminus A).$$
    </div>
  </p>
  <p>
    On the other hand, $B\setminus A\subseteq B$, so by monotonicity (also derived from the axioms),
    <div class="prob-math">
      $$P(B\setminus A)\le P(B).$$
    </div>
    Combining the two inequalities,
    <div class="prob-math">
      $$P(A\cup B)=P(A)+P(B\setminus A)\le P(A)+P(B).$$
    </div>
    This proves subadditivity.
  </p>
</div>

<div class="prob-separator"></div>

<!-- =============================================================== -->
<!--  5. INCLUSION–EXCLUSION (INTERACTIVE)                           -->
<!-- =============================================================== -->

<h2 class="prob-subtitle"><span class="prob-emoji">🔗</span> 5. Inclusion–Exclusion Principle</h2>

<p>
Another key identity is the inclusion–exclusion formula for two events, which corrects for double counting in the union.
</p>

<div class="prob-premium-box">
  <div class="prob-premium-title">Theorem (Inclusion–Exclusion for Two Events)</div>
  <div class="prob-premium-content">
    For any events $A$ and $B$,
    <div class="prob-math">
      $$P(A\cup B)=P(A)+P(B)-P(A\cap B).$$
    </div>
  </div>
</div>

<h3 class="prob-subtitle">Proof</h3>

<div class="prob-box">
  <p>
    First, decompose $B$ into two disjoint parts:
    <div class="prob-math">
      $$B=(B\setminus A)\cup(A\cap B).$$
    </div>
    By additivity,
    <div class="prob-math">
      $$P(B)=P(B\setminus A)+P(A\cap B).$$
    </div>
  </p>
  <p>
    Next, from the decomposition of the union,
    <div class="prob-math">
      $$A\cup B = A\cup(B\setminus A),$$
    </div>
    we obtain
    <div class="prob-math">
      $$P(A\cup B)=P(A)+P(B\setminus A).$$
    </div>
  </p>
  <p>
    Solve the first equation for $P(B\setminus A)$:
    <div class="prob-math">
      $$P(B\setminus A)=P(B)-P(A\cap B).$$
    </div>
    Substitute into the expression for $P(A\cup B)$:
    <div class="prob-math">
      \begin{aligned}
      P(A\cup B)
      &=P(A)+P(B\setminus A) \\
      &=P(A)+P(B)-P(A\cap B).
      \end{aligned}
    </div>
    This proves the inclusion–exclusion principle for two events.
  </p>
</div>

<h3 class="prob-subtitle"><span class="prob-emoji">🧩</span> Interactive Venn Diagram</h3>

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
Click on A, B, or the overlap to highlight the corresponding term in the formula. Double-click to reset.
</p>

<div id="inExFormula" class="prob-math" style="text-align:center;">
  $$P(A\cup B)=P(A)+P(B)-P(A\cap B)$$
</div>

<div class="prob-separator"></div>

<h2 class="prob-subtitle"><span class="prob-emoji">🌟</span> 6. Summary</h2>

<ul class="prob-list">
  <li>Different interpretations (classical, frequentist, Bayesian, geometric) offer complementary perspectives on probability.</li>
  <li>The axiomatic (Kolmogorov) framework unifies these interpretations under a single measure-theoretic structure.</li>
  <li>Probability theory is measure theory on a probability space $(\Omega,\mathcal{F},P)$.</li>
  <li>Important properties such as subadditivity and inclusion–exclusion are derived directly from the axioms.</li>
</ul>

<!-- =============================================================== -->
<!-- JAVASCRIPT FOR INTERACTIVITY                                    -->
<!-- =============================================================== -->

<script>
// Sigma-algebra hover effect
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("setA");
  const Ac = document.getElementById("setAc");

  if (A && Ac) {
    A.addEventListener("mouseenter", () => Ac.style.opacity = "0.3");
    A.addEventListener("mouseleave", () => Ac.style.opacity = "1");

    Ac.addEventListener("mouseenter", () => A.style.opacity = "0.3");
    Ac.addEventListener("mouseleave", () => A.style.opacity = "1");
  }
});
</script>

<script>
// Inclusion–Exclusion interactive Venn
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("vennA");
  const B = document.getElementById("vennB");
  const svg = document.getElementById("vennSVG");
  const formula = document.getElementById("inExFormula");

  if (!A || !B || !svg || !formula) return;

  function renderLatex(latex){
    formula.innerHTML = "$$" + latex + "$$";
    if (window.MathJax && window.MathJax.typesetPromise) {
      MathJax.typesetPromise();
    }
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

  // Click: check if inside both => intersection
  svg.addEventListener("click", (e) => {
    const rect = svg.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const sx = svg.viewBox.baseVal.width / rect.width;
    const sy = svg.viewBox.baseVal.height / rect.height;
    const X = x * sx;
    const Y = y * sy;

    const inA = (X-75)**2 + (Y-75)**2 <= 50**2;
    const inB = (X-125)**2 + (Y-75)**2 <= 50**2;

    if (inA && inB) {
      A.style.opacity = "1";
      B.style.opacity = "1";
      renderLatex("P(A \\\\cap B)");
    }
  });

  // Double-click reset
  svg.addEventListener("dblclick", () => {
    A.style.opacity = "1";
    B.style.opacity = "1";
    renderLatex("P(A\\\\cup B)=P(A)+P(B)-P(A\\\\cap B)");
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
