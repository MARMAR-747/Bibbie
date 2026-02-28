---
layout: default
title: Home Eng
nav_exclude: true
---

<script>
  document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('theme-toggle');
    const saved = localStorage.getItem('theme');
    if (saved) {
      jtd.setTheme(saved);
      if (btn) btn.textContent = saved === 'dark' ? '☀️' : '🌙';
    }
    if (btn) {
      btn.addEventListener('click', () => {
        const curr = jtd.getTheme();
        const next = curr === 'dark' ? 'light' : 'dark';
        jtd.setTheme(next);
        localStorage.setItem('theme', next);
        btn.textContent = next === 'dark' ? '☀️' : '🌙';
      });
    }
  });
</script>

<script>
document.addEventListener('DOMContentLoaded', () => {
  const counter = document.getElementById('pdf-count');
  if (!counter) return;
  
  const target = parseInt(counter.dataset.target);
  if (isNaN(target)) return;

  let count = 0;
  const step = 1;
  const delay = 400;

  const update = () => {
    if (count >= target) {
      counter.textContent = target;
      return;
    }
    count += step;
    counter.textContent = count;
    setTimeout(update, delay);
  };

  update();
});
</script>

<h2>📘 <span id="typed"></span></h2>

<script src="https://cdn.jsdelivr.net/npm/typed.js@2.0.12"></script>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    new Typed('#typed', {
      strings: [
        'The Computer Engineering Bibles',
        'University notes in digital format',
        'Universities of Palermo and Rome'
      ],
      typeSpeed: 50,
      backSpeed: 25,
      loop: true
    });
  });
</script>
---
**Computer Engineering** is universally recognized as one of the **most challenging degrees** to tackle.
Every day, students face problems of **considerable difficulty** — one of the toughest being the exam in **Signal Theory**.

But what happens if you don’t have access to **good study material**?

I began writing notes **for myself**, as a memory exercise and to practice typing on the computer (mainly with Word).
Over time, however, I realized that **hours and hours of work** couldn’t just be left in a drawer: I had found a new purpose —
**helping those who, like me, struggled to navigate through the professors’ handouts**.

**Let’s be clear**: I don’t consider my notes “better” than the official ones.
I do believe, however, that in the excessive academic formalism, there is sometimes a loss of real connection with students.

### 📖 Why “The Bibles”?

Because even courses worth just **6 CFU**, seemingly harmless, can hide **vast syllabi**. The name is ironic, yet also
symbolic: these notes became for me — and I hope for you as well — **a complete and reliable guide**.

Below you will find **all the material** I produced during my **Bachelor’s degree in Computer Engineering** at the
**University of Palermo**, organized **by year and by subject**.


<div class="counter-container">
  <div class="counter-box">
    <span id="pdf-count" class="counter" data-target="17">0</span>
    <p data-i18n="counter"></p>
    <p>📚 Available PDFs</p>
  </div>
</div>

<style>
.counter-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
  margin-top: 2rem;
}
.counter-box {
  text-align: center;
  background-color: var(--card-background-color, var(--body-background-color));
  padding: 20px 30px;
  border-radius: 15px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  transition: background-color 0.3s ease;
}
.counter-box p {
  margin: 0.5rem 0 0;
  color: var(--body-text-color);
}
.counter {
  font-size: 2.5rem;
  font-weight: bold;
  color: var(--link-color);
  display: block;
}
</style>

<!-- ===================== BACHELOR ===================== -->
<section class="material-section">
  <div class="material-title">
    📂
    <span>Bachelor Material (IT)</span>
  </div>

  <!-- 1st year -->
  <div class="year-block">
    <div class="year-card">
      <span class="year-number">1°</span>
      <div>
        <div class="year-main">First Year</div>
        <div class="year-sub">Computer Engineering - UniPa</div>
      </div>
    </div>

    <div class="cards-grid">
      <a class="course-card" href="{{ '/Algebra/' | relative_url }}">
        <div class="card-title">Algebra</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>
    </div>
  </div>

  <!-- 2nd year -->
  <div class="year-block">
    <div class="year-card">
      <span class="year-number">2°</span>
      <div>
        <div class="year-main">Second Year</div>
        <div class="year-sub">Computer Engineering - UniPa</div>
      </div>
    </div>

    <div class="cards-grid">
      <a class="course-card" href="{{ '/Teoria%20Dei%20Segnali/' | relative_url }}">
        <div class="card-title">Signals and Systems</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Elettrotecnica/' | relative_url }}">
        <div class="card-title">Electrical Engineering</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Metodi%20matematici%20e%20numerici/' | relative_url }}">
        <div class="card-title">Mathematical and Numerical Methods (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>
    </div>
  </div>

  <!-- 3rd year -->
  <div class="year-block">
    <div class="year-card">
      <span class="year-number">3°</span>
      <div>
        <div class="year-main">Third Year</div>
        <div class="year-sub">Computer Engineering - UniPa</div>
      </div>
    </div>

    <div class="cards-grid">
      <a class="course-card" href="{{ '/Reti%20di%20calcolatori/' | relative_url }}">
        <div class="card-title">Computer Networks (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Elettronica/' | relative_url }}">
        <div class="card-title">Electronics</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Controlli%20Automatici/' | relative_url }}">
        <div class="card-title">Automatic Control</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Ingegneria%20Del%20Software/' | relative_url }}">
        <div class="card-title">Software Engineering</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Programmazione%20Web%20e%20Mobile/' | relative_url }}">
        <div class="card-title">Web and Mobile Programming</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>
    </div>
  </div>
</section>

<!-- ===================== MASTER ===================== -->
<section class="material-section">
  <div class="material-title">
    📂
    <span>Master Material (ENG)</span>
  </div>

  <!-- 1st year -->
  <div class="year-block">
    <div class="year-card">
      <span class="year-number">1°</span>
      <div>
        <div class="year-main">First Year</div>
        <div class="year-sub">Cybersecurity - UniSapienza</div>
      </div>
    </div>

    <div class="cards-grid">
      <a class="course-card" href="{{ '/Statistics/' | relative_url }}">
        <div class="card-title">Statistics</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Cryptography/' | relative_url }}">
        <div class="card-title">Cryptography (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Distributed%20Systems/' | relative_url }}">
        <div class="card-title">Distributed Systems (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Network%20Infrastructures/' | relative_url }}">
        <div class="card-title">Network Infrastructures (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Ethical%20Hacking/' | relative_url }}">
        <div class="card-title">Ethical Hacking (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">9 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Practical%20Network%20Defense/' | relative_url }}">
        <div class="card-title">Practical Network Defense (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Economics%20of%20Technology%20and%20Management/' | relative_url }}">
        <div class="card-title">Economics of Technology and Management</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>

      <a class="course-card" href="{{ '/Cyber%20and%20Computer%20Law/' | relative_url }}">
        <div class="card-title">Cyber and Computer Law (SOON)</div>
        <div class="card-meta">
          <span class="cfu-pill">6 ECTS</span>
          <span class="card-arrow">→</span>
        </div>
      </a>
    </div>
  </div>
</section>

---
🔒 All material is released under license [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Last update: {{ site.time | date: "%d/%m/%Y" }}
