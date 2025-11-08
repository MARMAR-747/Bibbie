// ======================================================================
// Security Random Walk — Animated Simulation with Play/Pause
// ======================================================================
// Model:
//  - n weeks
//  - m attackers per week, each breaches with prob p
//  - week score: +1 if all m attackers fail, -1 if at least one breaches
//  - per-week security prob: q = (1 - p)^m
//  - Random walk step: +1 with prob q, -1 with prob 1-q
//  - Final score S_n in [-n, -n+2, ..., n-2, n]
//  - Animate V visible trajectories; accumulate counts over R total runs
//
// Exports:
//  - initSecurityUI: wire up charts and buttons
//
// Dependencies: Chart.js loaded by the page
// ======================================================================

function binomialCoef(k, n) {
  // C(n,k) without overflow (multiplicative formula)
  if (k < 0 || k > n) return 0;
  if (k === 0 || k === n) return 1;
  k = Math.min(k, n - k);
  let r = 1;
  for (let i = 1; i <= k; i++) {
    r = (r * (n - i + 1)) / i;
  }
  return r;
}

function theoreticalProbForScore(s, n, q) {
  // Map score s -> K = (s+n)/2 successes; then Bin(n,q) at K
  const K = (s + n) / 2;
  if (K % 1 !== 0 || K < 0 || K > n) return 0;
  return binomialCoef(K, n) * Math.pow(q, K) * Math.pow(1 - q, n - K);
}

export function initSecurityUI(cfg) {
  // ------------------------------------------------------------------
  // cfg: { ids: {...}, defaults: {...} }
  // ids: {
  //   nInput, mInput, pInput, runsInput, visiblesInput,
  //   startBtn, toggleBtn, resetBtn,
  //   pathCanvasId, distCanvasId, infoBoxId
  // }
  // defaults: { n, m, p, runs, visibles, tickMs }
  // ------------------------------------------------------------------
  const ids = cfg.ids;

  // ====== State ======
  let n = cfg.defaults.n ?? 20;
  let m = cfg.defaults.m ?? 5;
  let p = cfg.defaults.p ?? 0.2;
  let runsTarget = cfg.defaults.runs ?? 2000;
  let visibles = cfg.defaults.visibles ?? 10; // animated visible paths
  let tickMs = cfg.defaults.tickMs ?? 50;

  // Probability of secure week
  let q = Math.pow(1 - p, m);

  // Animation state
  let playing = false;
  let timer = null;

  // Per-visible trajectory runtime buffers
  let paths = []; // array of {t, score, xs (time), ys (score)}
  // Distribution accumulator
  let scoreCounts = {}; // final score -> frequency
  let runsCompleted = 0;

  // Precompute possible score grid for x-axis (even steps from -n to n)
  function scoreGrid(n) {
    const arr = [];
    for (let s = -n; s <= n; s += 2) arr.push(s);
    return arr;
  }
  let scoresAxis = scoreGrid(n);

  // ====== DOM ======
  const $ = (id) => document.getElementById(id);

  // Hook inputs
  const nInput = $(ids.nInput);
  const mInput = $(ids.mInput);
  const pInput = $(ids.pInput);
  const runsInput = $(ids.runsInput);
  const visInput = $(ids.visiblesInput);

  const startBtn = $(ids.startBtn);
  const toggleBtn = $(ids.toggleBtn);
  const resetBtn = $(ids.resetBtn);

  const infoBox = $(ids.infoBoxId);

  // Initialize inputs with defaults
  nInput.value = n;
  mInput.value = m;
  pInput.value = p;
  runsInput.value = runsTarget;
  if (visInput) visInput.value = visibles;

  // ====== Charts ======
  // Paths chart: multiple lines (visible trajectories)
  const pathCtx = $(ids.pathCanvasId).getContext("2d");
  const distCtx = $(ids.distCanvasId).getContext("2d");

  // Build datasets for V visible paths
  function makePathDatasets(V) {
    const colors = [];
    for (let i = 0; i < V; i++) {
      // generate pastel-ish hues
      const hue = Math.floor((360 * i) / Math.max(1, V));
      colors.push(`hsl(${hue} 90% 55%)`);
    }
    return Array.from({ length: V }, (_, k) => ({
      label: `Path ${k + 1}`,
      data: [], // {x: t, y: score}
      borderColor: colors[k],
      borderWidth: 1.5,
      pointRadius: 0,
      fill: false,
      tension: 0
    }));
  }

  const pathChart = new Chart(pathCtx, {
    type: "line",
    data: {
      datasets: makePathDatasets(visibles)
    },
    options: {
      responsive: true,
      animation: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { type: "linear", min: 0, max: n, title: { display: true, text: "Week (t)" } },
        y: { min: -n, max: n, title: { display: true, text: "Cumulative score S_t" } }
      }
    }
  });

  // Distribution chart: bar with theoretical overlay
  let distChart = new Chart(distCtx, {
    type: "bar",
    data: {
      labels: scoresAxis,
      datasets: [
        {
          label: "Empirical (completed runs)",
          data: scoresAxis.map((s) => 0),
          backgroundColor: "rgba(255, 165, 0, 0.85)" // orange
        },
        {
          label: "Theoretical Binomial",
          data: scoresAxis.map((s) => theoreticalProbForScore(s, n, q)),
          type: "line",
          borderColor: "red",
          borderWidth: 2,
          cubicInterpolationMode: "monotone",
          tension: 0.2
        }
      ]
    },
    options: {
      responsive: true,
      animation: false,
      scales: {
        x: { title: { display: true, text: "Final score S_n" } },
        y: { title: { display: true, text: "Probability" }, min: 0, max: 1 }
      }
    }
  });

  function updateInfo() {
    infoBox.textContent =
      `q = (1 - p)^m = ${(q).toFixed(4)}\n` +
      `Completed runs: ${runsCompleted} / ${runsTarget}\n` +
      `Playing: ${playing ? "yes" : "no"}`;
  }

  // ====== Helpers ======
  function resetSimulation() {
    // Reset counters
    runsCompleted = 0;
    scoreCounts = {};
    // Reset paths
    paths = Array.from({ length: visibles }, () => ({ t: 0, score: 0, xs: [0], ys: [0] }));

    // Reset path datasets
    pathChart.data.datasets = makePathDatasets(visibles);
    for (let i = 0; i < visibles; i++) {
      pathChart.data.datasets[i].data = [{ x: 0, y: 0 }];
    }
    pathChart.options.scales.x.max = n;
    pathChart.options.scales.y.min = -n;
    pathChart.options.scales.y.max = n;
    pathChart.update();

    // Reset dist dataset
    scoresAxis = scoreGrid(n);
    distChart.data.labels = scoresAxis;
    distChart.data.datasets[0].data = scoresAxis.map(() => 0);
    distChart.data.datasets[1].data = scoresAxis.map((s) => theoreticalProbForScore(s, n, q));
    distChart.update();

    updateInfo();
  }

  function stepOnce() {
    // Advance each visible path one week (if not finished)
    for (let i = 0; i < visibles; i++) {
      const path = paths[i];
      if (path.t >= n) continue; // finished

      // Bernoulli step: +1 with prob q, -1 otherwise
      const secure = Math.random() < q;
      path.score += secure ? +1 : -1;
      path.t += 1;

      // push point to dataset
      path.xs.push(path.t);
      path.ys.push(path.score);
      pathChart.data.datasets[i].data.push({ x: path.t, y: path.score });

      // If finished, record score and optionally restart this slot
      if (path.t === n) {
        scoreCounts[path.score] = (scoreCounts[path.score] || 0) + 1;
        runsCompleted += 1;

        // If we still need more runs overall, restart this visible path
        if (runsCompleted < runsTarget) {
          paths[i] = { t: 0, score: 0, xs: [0], ys: [0] };
          pathChart.data.datasets[i].data = [{ x: 0, y: 0 }];
        }
      }
    }

    // Update empirical distribution (normalize by completed runs if > 0)
    if (runsCompleted > 0) {
      const emp = scoresAxis.map((s) => (scoreCounts[s] || 0) / runsCompleted);
      distChart.data.datasets[0].data = emp;
    }

    // Repaint charts
    pathChart.update();
    distChart.update();
    updateInfo();

    // Stop automatically if we reached target runs
    if (runsCompleted >= runsTarget) {
      playing = false;
      if (timer) clearInterval(timer);
      timer = null;
      toggleBtn.textContent = "▶ Play";
      updateInfo();
    }
  }

  function startLoop() {
    if (timer) clearInterval(timer);
    timer = setInterval(stepOnce, tickMs);
  }

  // ====== Wire buttons ======
  startBtn.addEventListener("click", () => {
    // read fresh params
    n = Math.max(1, parseInt(nInput.value, 10) || 1);
    m = Math.max(1, parseInt(mInput.value, 10) || 1);
    p = Math.min(1, Math.max(0, parseFloat(pInput.value) || 0));
    runsTarget = Math.max(1, parseInt(runsInput.value, 10) || 1);
    visibles = Math.max(1, parseInt((visInput && visInput.value) || cfg.defaults.visibles || 5, 10));
    q = Math.pow(1 - p, m);

    // reconfigure charts & reset state
    pathChart.options.scales.x.max = n;
    pathChart.options.scales.y.min = -n;
    pathChart.options.scales.y.max = n;

    resetSimulation();

    // start playing immediately
    playing = true;
    toggleBtn.textContent = "⏸ Pause";
    startLoop();
  });

  toggleBtn.addEventListener("click", () => {
    if (!timer && !playing && runsCompleted < runsTarget) {
      // resume
      playing = true;
      toggleBtn.textContent = "⏸ Pause";
      startLoop();
      return;
    }
    playing = !playing;
    toggleBtn.textContent = playing ? "⏸ Pause" : "▶ Play";
    if (playing) startLoop(); else { if (timer) { clearInterval(timer); timer = null; } }
    updateInfo();
  });

  resetBtn.addEventListener("click", () => {
    // stop timers and reset everything, keep current params
    playing = false;
    if (timer) { clearInterval(timer); timer = null; }
    toggleBtn.textContent = "▶ Play";
    resetSimulation();
  });

  // ====== Initial empty render (charts visible from the start) ======
  resetSimulation();
}
