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
    <span id="pdf-count" class="counter" data-target="14">0</span>
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

## 📂<img src="{{ '/assets/images/IT_flag.png' | relative_url }}" width="30" style="vertical-align: middle;"> Material (Bachelor’s Degree in computer engineering - UniPa)
---
### ➀ First year

- [Algebra](Algebra/) (6 CFU)

### ➁ Second year

- [Signal Theory](Teoria Dei Segnali/) (9 CFU)
- [Electrical Engineering](Elettrotecnica/) (6 CFU)
- [Mathematical and Numerical Methods] (9 CFU)

### ➂ Third year

- [Computer Networks] (9 CFU)
- [Electronics](Elettronica/) (9 CFU)
- [Automatic Control](Controlli Automatici/) (9 CFU)
- [Software Engineering](Ingegneria Del Software/) (9 CFU)
- [Web and Mobile Programming](Programmazione Web e Mobile/) (9 CFU)


## 📂<img src="{{ '/assets/images/UK_flag.png' | relative_url }}" width="30" style="vertical-align: middle;"> Material (Master’s Degree in cybersecurity - UniSapienza)
---
### ➀ First year

- [Statistics](Statistics/) (6 CFU)
- [Cryptography] (6 CFU)
- [Distributed Systems] (6 CFU)
- [Network Infrastructures] (6 CFU)

---
🔒 All material is released under license [CC BY-NC-ND 4.0](https://creativecommons.org/licenses/by-nc-nd/4.0/).  
🔗 Last update: {{ site.time | date: "%d/%m/%Y" }}
