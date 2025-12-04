// =====================================================================
// HW11 - Realtime Brownian Motion Simulation + Histogram of B(T)
// =====================================================================

function boxMuller() {
  let u = 0, v = 0;
  while (u === 0) u = Math.random();
  while (v === 0) v = Math.random();

  // explicit parentheses = JS-safe
  return Math.sqrt((-2) * Math.log(u)) * Math.cos(2 * Math.PI * v);
}

function normalPDF(x, mean, variance) {
  const std = Math.sqrt(variance);
  return (1 / (std * Math.sqrt(2 * Math.PI))) *
    Math.exp(-(x - mean) ** 2 / (2 * variance));
}

export function initBrownianUI(cfg) {
  const $ = id => document.getElementById(id);

  // UI elements
  const TInput = $(cfg.ids.TInput);
  const nInput = $(cfg.ids.nInput);
  const pathsInput = $(cfg.ids.pathsInput);

  const startBtn = $(cfg.ids.startBtn);
  const toggleBtn = $(cfg.ids.toggleBtn);
  const resetBtn = $(cfg.ids.resetBtn);
  const infoBox = $(cfg.ids.infoId);

  const trajCtx = $(cfg.ids.canvasPaths).getContext("2d");
  const histCtx = $(cfg.ids.canvasHist).getContext("2d");

  // State
  let T, n, visiblePaths, dt;
  let playing = false;
  let timer = null;

  let paths = [];
  let finalSamples = [];

  // ---------------------------------------------------------
  // 1) Chart for trajectories
  // ---------------------------------------------------------
  const trajChart = new Chart(trajCtx, {
    type: "line",
    data: { datasets: [] },
    options: {
      responsive: true,
      animation: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { type: "linear", min: 0, title: { display: true, text: "t" } },
        y: { title: { display: true, text: "B(t)" } }
      }
    }
  });

  // ---------------------------------------------------------
  // 2) Histogram of B(T)
  // ---------------------------------------------------------
  const histChart = new Chart(histCtx, {
    type: "bar",
    data: {
      labels: [],
      datasets: [
        {
          label: "Empirical histogram",
          data: [],
          backgroundColor: "rgba(54, 162, 235, 0.7)"
        },
        {
          type: "line",
          label: "Theoretical Normal(0, T)",
          data: [],
          borderColor: "orange",
          borderWidth: 2,
          tension: 0.2,
          fill: false
        }
      ]
    },
    options: {
      responsive: true,
      animation: false,
      scales: {
        x: { title: { display: true, text: "x" } },
        y: { title: { display: true, text: "Density" }, beginAtZero: true }
      }
    }
  });

  // ---------------------------------------------------------
  // Utility: Update info panel
  // ---------------------------------------------------------
  function updateInfo() {
    infoBox.textContent =
      `T = ${T}\n` +
      `n = ${n}\n` +
      `dt = ${dt.toExponential(2)}\n` +
      `Visible paths = ${visiblePaths}\n` +
      `Increments: Normal(0, dt) via Box–Muller`;
  }

  // ---------------------------------------------------------
  // Reset simulation
  // ---------------------------------------------------------
  function reset() {
    paths = [];
    finalSamples = [];
    trajChart.data.datasets = [];

    const colors = [];
    for (let i = 0; i < visiblePaths; i++) {
      const hue = Math.floor((360 * i) / visiblePaths);
      colors.push(`hsl(${hue}, 80%, 55%)`);
    }

    for (let i = 0; i < visiblePaths; i++) {
      paths.push({
        k: 0,
        t: 0,
        x: 0,
        data: [{ x: 0, y: 0 }]
      });
      trajChart.data.datasets.push({
        data: paths[i].data,
        borderColor: colors[i],
        borderWidth: 1.6,
        pointRadius: 0,
        fill: false
      });
    }

    trajChart.options.scales.x.max = T;
    trajChart.options.scales.y.min = -3 * Math.sqrt(T);
    trajChart.options.scales.y.max = 3 * Math.sqrt(T);

    trajChart.update();
    histChart.update();
    updateInfo();
  }

  // ---------------------------------------------------------
  // Step once
  // ---------------------------------------------------------
  function stepOnce() {
    let allFinished = true;

    for (let i = 0; i < visiblePaths; i++) {
      const p = paths[i];
      if (p.k >= n) continue;

      allFinished = false;

      p.k += 1;
      p.t = p.k * dt;

      const dW = Math.sqrt(dt) * boxMuller();
      p.x += dW;

      p.data.push({ x: p.t, y: p.x });

      if (p.k === n) {
        finalSamples.push(p.x);
        updateHistogram();
      }
    }

    trajChart.update("none");

    if (allFinished) {
      playing = false;
      clearInterval(timer);
      toggleBtn.textContent = "▶ Play";
    }
  }

  // ---------------------------------------------------------
  // Update histogram
  // ---------------------------------------------------------
  function updateHistogram() {
    if (finalSamples.length === 0) return;

    const bins = 30;
    const min = Math.min(...finalSamples);
    const max = Math.max(...finalSamples);
    const step = (max - min) / bins;

    const counts = Array(bins).fill(0);
    finalSamples.forEach(v => {
      let idx = Math.floor((v - min) / step);
      if (idx === bins) idx--;
      counts[idx]++;
    });

    const pdfVals = [];
    const xs = [];
    for (let i = 0; i < bins; i++) {
      const x = min + (i + 0.5) * step;
      xs.push(x.toFixed(2));
      pdfVals.push(normalPDF(x, 0, T));
    }

    histChart.data.labels = xs;
    histChart.data.datasets[0].data = counts.map(c => c / finalSamples.length);
    histChart.data.datasets[1].data = pdfVals;

    histChart.update("none");
  }

  // ---------------------------------------------------------
  // Control buttons
  // ---------------------------------------------------------
  startBtn.addEventListener("click", () => {
    T = parseFloat(TInput.value) || 1;
    n = parseInt(nInput.value, 10) || 1000;
    visiblePaths = parseInt(pathsInput.value, 10) || 5;

    dt = T / n;

    reset();
    playing = true;
    toggleBtn.textContent = "⏸ Pause";

    if (timer) clearInterval(timer);
    timer = setInterval(stepOnce, cfg.defaults.tickMs || 30);
  });

  toggleBtn.addEventListener("click", () => {
    playing = !playing;
    toggleBtn.textContent = playing ? "⏸ Pause" : "▶ Play";

    if (playing) timer = setInterval(stepOnce, cfg.defaults.tickMs || 30);
    else clearInterval(timer);
  });

  resetBtn.addEventListener("click", () => {
    playing = false;
    clearInterval(timer);
    toggleBtn.textContent = "▶ Play";
    reset();
  });

  // Initial render
  T = cfg.defaults.T || 1;
  n = cfg.defaults.n || 1000;
  visiblePaths = cfg.defaults.visiblePaths || 5;
  dt = T / n;
  reset();
}
