// ============================================================
// HW10 - Poisson counting process (Bernoulli approximation)
// ============================================================
// We simulate a counting process on [0, T]:
// - Divide [0, T] into n subintervals of width dt = T / n
// - In each small interval, generate an event with probability p = λ * dt
// - The cumulative count over time approximates a Poisson process N(t)
//   with rate λ, and N(T) ~ Poisson(λT).
// This file exposes initPoissonUI(config) to wire up an interactive demo.
// ============================================================

function simulateCountingProcess(T, lambda, n, numPaths) {
  const dt = T / n;
  const p = lambda * dt;              // event probability in each subinterval
  const times = [];
  for (let k = 0; k <= n; k++) {
    times.push(k * dt);
  }

  const paths = [];
  const finalCounts = [];

  for (let j = 0; j < numPaths; j++) {
    let count = 0;
    const path = [0];                // N(0) = 0
    for (let k = 1; k <= n; k++) {
      if (Math.random() < p) {
        count += 1;
      }
      path.push(count);
    }
    paths.push(path);
    finalCounts.push(count);
  }

  return { times, paths, finalCounts };
}

// Compute empirical histogram for integer counts
function buildHistogram(finalCounts) {
  const hist = {};
  for (const c of finalCounts) {
    hist[c] = (hist[c] || 0) + 1;
  }
  return hist;
}

// Poisson(μ) PMF for k = 0,...,kMax, using a stable recurrence
function poissonPMF(mu, kMax) {
  const vals = [];
  let p0 = Math.exp(-mu);  // P(0)
  vals[0] = p0;
  for (let k = 1; k <= kMax; k++) {
    vals[k] = vals[k - 1] * (mu / k);
  }
  return vals;
}

// ============================================================
// UI bootstrap
// ============================================================
export function initPoissonUI(cfg) {
  const ids = cfg.ids;

  const $ = (id) => document.getElementById(id);

  const TInput      = $(ids.TInput);
  const lambdaInput = $(ids.lambdaInput);
  const nInput      = $(ids.nInput);
  const pathsInput  = $(ids.pathsInput);
  const runsInput   = $(ids.runsInput); // how many paths for histogram
  const infoBox     = $(ids.infoBoxId);

  const runBtn      = $(ids.runBtn);

  const trajCtx = $(ids.trajCanvasId).getContext("2d");
  const histCtx = $(ids.histCanvasId).getContext("2d");

  // Default values
  TInput.value      = cfg.defaults.T ?? 1;
  lambdaInput.value = cfg.defaults.lambda ?? 5;
  nInput.value      = cfg.defaults.n ?? 5000;
  pathsInput.value  = cfg.defaults.visiblePaths ?? 5;
  runsInput.value   = cfg.defaults.runs ?? 2000;

  // Trajectories chart (line chart)
  const trajChart = new Chart(trajCtx, {
    type: "line",
    data: { datasets: [] },
    options: {
      responsive: true,
      animation: false,
      plugins: { legend: { display: false } },
      scales: {
        x: { type: "linear", title: { display: true, text: "t" } },
        y: { title: { display: true, text: "N(t)" }, beginAtZero: true }
      }
    }
  });

  // Histogram + theoretical Poisson at T
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
        x: { title: { display: true, text: "k (total events in [0,T])" } },
        y: { title: { display: true, text: "Probability" }, beginAtZero: true }
      }
    }
  });

  function updateInfo(T, lambda, n, numPaths, runs) {
    infoBox.textContent =
      `T = ${T}   λ = ${lambda}\n` +
      `Subintervals n = ${n}  (dt = ${(T / n).toExponential(2)})\n` +
      `Visible trajectories = ${numPaths},  paths for histogram = ${runs}\n` +
      `Target process: Poisson counting process with rate λ, N(T) ~ Poisson(λT).`;
  }

  runBtn.addEventListener("click", () => {
    const T       = parseFloat(TInput.value)      || 1;
    const lambda  = parseFloat(lambdaInput.value) || 1;
    const n       = Math.max(10, parseInt(nInput.value, 10) || 1000);
    const vPaths  = Math.max(1, parseInt(pathsInput.value, 10) || 3);
    const runs    = Math.max(vPaths, parseInt(runsInput.value, 10) || 1000);

    const { times, paths, finalCounts } =
      simulateCountingProcess(T, lambda, n, runs);

    // 1) Update trajectories chart (only first vPaths paths)
    const maxVisible = Math.min(vPaths, paths.length);
    const colors = [];
    for (let i = 0; i < maxVisible; i++) {
      const hue = Math.floor((360 * i) / Math.max(1, maxVisible));
      colors.push(`hsl(${hue} 80% 55%)`);
    }

    trajChart.data.datasets = [];
    for (let i = 0; i < maxVisible; i++) {
      const data = [];
      // downsample for plotting if n is huge (for speed)
      const step = Math.max(1, Math.floor(n / 400));
      for (let k = 0; k <= n; k += step) {
        data.push({ x: times[k], y: paths[i][k] });
      }
      // ensure final point
      if (n % step !== 0) {
        data.push({ x: times[n], y: paths[i][n] });
      }

      trajChart.data.datasets.push({
        label: `Path ${i + 1}`,
        data,
        borderColor: colors[i],
        borderWidth: 1.5,
        pointRadius: 0,
        stepped: true,
        fill: false
      });
    }
    trajChart.options.scales.x.min = 0;
    trajChart.options.scales.x.max = T;
    trajChart.update();

    // 2) Update histogram chart
    const hist = buildHistogram(finalCounts);
    const ks = Object.keys(hist).map(Number).sort((a, b) => a - b);

    // empirical probabilities
    const empirical = ks.map(k => hist[k] / runs);

    // theoretical Poisson(λT)
    const mu = lambda * T;
    const maxK = ks.length ? ks[ks.length - 1] : Math.ceil(mu + 5 * Math.sqrt(mu + 1e-9));
    const poisVals = poissonPMF(mu, maxK);

    histChart.data.labels = [];
    histChart.data.datasets[0].data = [];
    histChart.data.datasets[1].data = [];

    for (let k = 0; k <= maxK; k++) {
      histChart.data.labels.push(k);
      histChart.data.datasets[0].data.push(hist[k] ? hist[k] / runs : 0);
      histChart.data.datasets[1].data.push(poisVals[k] ?? 0);
    }
    histChart.update();

    updateInfo(T, lambda, n, vPaths, runs);
  });

  // Initial info text
  updateInfo(
    parseFloat(TInput.value),
    parseFloat(lambdaInput.value),
    parseInt(nInput.value, 10),
    parseInt(pathsInput.value, 10),
    parseInt(runsInput.value, 10)
  );
}
