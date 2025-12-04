// ==========================================================
// HW11 - Realtime Brownian Motion Simulation
// Continuous time, continuous state increments
// Normal increments generated via Box–Muller transform
// ==========================================================

function boxMuller() {
  // Generate N(0,1) using Box–Muller
  let u = 0, v = 0;
  while (u === 0) u = Math.random();
  while (v === 0) v = Math.random();
  return Math.sqrt(-2 * Math.log(u)) * Math.cos(2 * Math.PI * v);
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

  const ctx = $(cfg.ids.canvasId).getContext("2d");

  let T, n, visiblePaths, dt;
  let playing = false;
  let timer = null;

  let paths = [];

  // Chart.js instance
  const chart = new Chart(ctx, {
    type: "line",
    data: { datasets: [] },
    options: {
      responsive: true,
      animation: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { type: "linear", title: { display: true, text: "t" }, min: 0 },
        y: { title: { display: true, text: "B(t)" } }
      }
    }
  });

  function reset() {
    paths = [];
    chart.data.datasets = [];

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

      chart.data.datasets.push({
        data: paths[i].data,
        borderColor: colors[i],
        borderWidth: 1.7,
        pointRadius: 0,
        fill: false
      });
    }

    chart.options.scales.x.max = T;
    chart.options.scales.y.min = -Math.sqrt(T) * 3;
    chart.options.scales.y.max = Math.sqrt(T) * 3;

    chart.update();
    updateInfo();
  }

  function updateInfo() {
    infoBox.textContent =
      `T = ${T}\n` +
      `n = ${n}\n` +
      `dt = ${dt.toExponential(2)}\n` +
      `Visible paths = ${visiblePaths}\n` +
      `Increments: Normal(0, dt) using Box–Muller`;
  }

  function stepOnce() {
    for (let i = 0; i < visiblePaths; i++) {
      const p = paths[i];
      if (p.k >= n) continue;

      p.k += 1;
      p.t = p.k * dt;

      // increment distributed as Normal(0, dt)
      const dW = Math.sqrt(dt) * boxMuller();
      p.x += dW;

      p.data.push({ x: p.t, y: p.x });

      if (p.k >= n) continue;
    }

    chart.update("none");
  }

  function playLoop() {
    if (timer) clearInterval(timer);
    timer = setInterval(stepOnce, cfg.defaults.tickMs || 30);
  }

  startBtn.addEventListener("click", () => {
    T = parseFloat(TInput.value) || 1;
    n = parseInt(nInput.value, 10) || 1000;
    visiblePaths = parseInt(pathsInput.value, 10) || 5;
    dt = T / n;

    reset();
    playing = true;
    toggleBtn.textContent = "⏸ Pause";
    playLoop();
  });

  toggleBtn.addEventListener("click", () => {
    playing = !playing;
    toggleBtn.textContent = playing ? "⏸ Pause" : "▶ Play";
    if (playing) playLoop();
    else clearInterval(timer);
  });

  resetBtn.addEventListener("click", () => {
    playing = false;
    clearInterval(timer);
    toggleBtn.textContent = "▶ Play";
    reset();
  });

  // initial visual
  T = cfg.defaults.T || 1;
  n = cfg.defaults.n || 1000;
  visiblePaths = cfg.defaults.visiblePaths || 5;
  dt = T / n;
  reset();
}
