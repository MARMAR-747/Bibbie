// ============================================================
// HW10 - Realtime Poisson counting process (Bernoulli approx)
// ============================================================
//
// We approximate a Poisson process N(t) on [0, T]:
// - Split [0, T] into n small intervals of width dt = T / n
// - In each interval, generate an event with probability p = λ · dt
// - Over many paths, the distribution of N(T) tends to Poisson(λT)
//
// This module exposes initPoissonUI(config) to wire the UI.
// Requires Chart.js to be loaded by the page.
// ============================================================

function binomialCoef(k, n) {
  if (k < 0 || k > n) return 0;
  if (k === 0 || k === n) return 1;
  k = Math.min(k, n - k);
  let r = 1;
  for (let i = 1; i <= k; i++) {
    r = (r * (n - i + 1)) / i;
  }
  return r;
}

// Poisson pmf via recurrence
function poissonPMF(mu, kMax) {
  const vals = [];
  let p0 = Math.exp(-mu);
  vals[0] = p0;
  for (let k = 1; k <= kMax; k++) {
    vals[k] = vals[k - 1] * (mu / k);
  }
  return vals;
}

export function initPoissonUI(cfg) {
  const ids = cfg.ids;
  const $ = (id) => document.getElementById(id);

  // Inputs & UI elements
  const TInput      = $(ids.TInput);
  const lambdaInput = $(ids.lambdaInput);
  const nInput      = $(ids.nInput);
  const pathsInput  = $(ids.pathsInput);
  const runsInput   = $(ids.runsInput);
  const infoBox     = $(ids.infoBoxId);

  const startBtn  = $(ids.startBtn);
  const toggleBtn = $(ids.toggleBtn);
  const resetBtn  = $(ids.resetBtn);

  const trajCtx = $(ids.trajCanvasId).getContext("2d");
  const histCtx = $(ids.histCanvasId).getContext("2d");

  // Defaults
  TInput.value      = cfg.defaults.T ?? 1;
  lambdaInput.value = cfg.defaults.lambda ?? 5;
  nInput.value      = cfg.defaults.n ?? 1000;
  pathsInput.value  = cfg.defaults.visiblePaths ?? 5;
  runsInput.value   = cfg.defaults.runs ?? 2000;

  const tickMs = cfg.defaults.tickMs ?? 40;

  // State for animation
  let T, lambda, n, visiblePaths, totalRuns;
  let dt, p, mu;

  let playing = false;
  let timer = null;

  let paths = [];          // visible trajectories
  let scoreCounts = {};    // histogram for N(T)
  let runsCompleted = 0;

  // Prebuild charts
  const trajChart = new Chart(trajCtx, {
    type: "line",
    data: { datasets: [] },
    options: {
      responsive: true,
      animation: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { type: "linear", title: { display: true, text: "t" }, min: 0 },
        y: { title: { display: true, text: "N(t)" }, beginAtZero: true }
      }
    }
  });

  const histChart = new Chart(histCtx, {
    type: "bar",
    data: {
      labels: [],
      datasets: [
        {
          label: "Empirical P(N(T)=k)",
          data: [],
          backgroundColor: "rgba(54, 162, 235, 0.7)"
        },
        {
          label: "Theoretical Poisson(λT)",
          data: [],
          type: "line",
          borderColor: "orange",
          borderWidth: 2,
          tension: 0.2
        }
      ]
    },
    options: {
      responsive: true,
      animation: false,
      scales: {
        x: { title: { display: true, text: "k" } },
        y: { title: { display: true, text: "Probability" }, beginAtZero: true }
      }
    }
  });

  function updateInfo() {
    const dtStr = (dt).toExponential(2);
    infoBox.textContent =
      `T = ${T}   λ = ${lambda}   μ = λT = ${mu.toFixed(2)}\n` +
      `Subintervals n = ${n}   dt = ${dtStr}   p = λ·dt ≈ ${(p).toExponential(2)}\n` +
      `Visible trajectories = ${visiblePaths},  completed paths = ${runsCompleted}/${totalRuns}\n` +
      `Target process: Poisson counting process with rate λ (independent, stationary increments).`;
  }

  function resetSimulation() {
    runsCompleted = 0;
    scoreCounts = {};

    // Initialize visible paths
    paths = [];
    trajChart.data.datasets = [];
    const colors = [];
    for (let i = 0; i < visiblePaths; i++) {
      const hue = Math.floor((360 * i) / Math.max(1, visiblePaths));
      colors.push(`hsl(${hue} 80% 55%)`);
    }

    for (let i = 0; i < visiblePaths; i++) {
      paths.push({
        k: 0,            // index in [0, n]
        count: 0,        // N(t)
        data: [{ x: 0, y: 0 }]
      });

      trajChart.data.datasets.push({
        label: `Path ${i + 1}`,
        data: paths[i].data,
        borderColor: colors[i],
        borderWidth: 1.5,
        pointRadius: 0,
        stepped: true,
        fill: false
      });
    }

    trajChart.options.scales.x.min = 0;
    trajChart.options.scales.x.max = T;
    trajChart.options.scales.y.min = 0;
    trajChart.options.scales.y.max = Math.max(5, mu * 2);
    trajChart.update();

    // Reset histogram
    histChart.data.labels = [];
    histChart.data.datasets[0].data = [];
    histChart.data.datasets[1].data = [];
    histChart.update();

    updateInfo();
  }

  function stepOnce() {
    // advance each visible path by one subinterval (dt)
    for (let i = 0; i < visiblePaths; i++) {
      const path = paths[i];
      if (path.k >= n) continue; // this instance finished

      path.k += 1;
      if (Math.random() < p) {
        path.count += 1;
      }
      const t = path.k * dt;
      path.data.push({ x: t, y: path.count });

      // path just finished
      if (path.k === n) {
        scoreCounts[path.count] = (scoreCounts[path.count] || 0) + 1;
        runsCompleted += 1;

        // if we want more total runs, recycle this path slot
        if (runsCompleted < totalRuns) {
          paths[i] = { k: 0, count: 0, data: [{ x: 0, y: 0 }] };
          trajChart.data.datasets[i].data = paths[i].data;
        }
      }
    }

    // Update trajectories chart
    trajChart.update("none");

    // Update histogram if we have at least one completed run
    if (runsCompleted > 0) {
      const ks = Object.keys(scoreCounts).map(Number).sort((a, b) => a - b);
      const maxK = ks[ks.length - 1];
      const poisVals = poissonPMF(mu, maxK);

      histChart.data.labels = [];
      histChart.data.datasets[0].data = [];
      histChart.data.datasets[1].data = [];

      for (let k = 0; k <= maxK; k++) {
        const emp = (scoreCounts[k] || 0) / runsCompleted;
        histChart.data.labels.push(k);
        histChart.data.datasets[0].data.push(emp);
        histChart.data.datasets[1].data.push(poisVals[k] ?? 0);
      }
      histChart.update("none");
    }

    updateInfo();

    if (runsCompleted >= totalRuns) {
      playing = false;
      if (timer) {
        clearInterval(timer);
        timer = null;
      }
      toggleBtn.textContent = "▶ Play";
      updateInfo();
    }
  }

  function startLoop() {
    if (timer) clearInterval(timer);
    timer = setInterval(stepOnce, tickMs);
  }

  // ---- Buttons ----
  startBtn.addEventListener("click", () => {
    T      = Math.max(0.1, parseFloat(TInput.value) || 1);
    lambda = Math.max(0.01, parseFloat(lambdaInput.value) || 1);
    n      = Math.max(50, parseInt(nInput.value, 10) || 1000);
    visiblePaths = Math.max(1, parseInt(pathsInput.value, 10) || 3);
    totalRuns    = Math.max(visiblePaths, parseInt(runsInput.value, 10) || 500);

    dt = T / n;
    p  = lambda * dt;
    mu = lambda * T;

    resetSimulation();
    playing = true;
    toggleBtn.textContent = "⏸ Pause";
    startLoop();
  });

  toggleBtn.addEventListener("click", () => {
    if (!timer && !playing && runsCompleted < totalRuns) {
      // resume from stopped state
      playing = true;
      toggleBtn.textContent = "⏸ Pause";
      startLoop();
      return;
    }
    playing = !playing;
    toggleBtn.textContent = playing ? "⏸ Pause" : "▶ Play";
    if (playing) {
      startLoop();
    } else if (timer) {
      clearInterval(timer);
      timer = null;
    }
    updateInfo();
  });

  resetBtn.addEventListener("click", () => {
    playing = false;
    if (timer) {
      clearInterval(timer);
      timer = null;
    }
    toggleBtn.textContent = "▶ Play";
    resetSimulation();
  });

  // Initial empty state (charts visible)
  T      = parseFloat(TInput.value) || 1;
  lambda = parseFloat(lambdaInput.value) || 5;
  n      = parseInt(nInput.value, 10) || 1000;
  visiblePaths = parseInt(pathsInput.value, 10) || 5;
  totalRuns    = parseInt(runsInput.value, 10) || 2000;
  dt = T / n;
  p  = lambda * dt;
  mu = lambda * T;

  resetSimulation();
}
