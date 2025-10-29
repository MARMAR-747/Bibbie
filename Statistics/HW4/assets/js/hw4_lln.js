(function () {
  // ---------- Utilities ----------
  const $ = (id) => document.getElementById(id);
  const clamp = (x, a, b) => Math.max(a, Math.min(b, x));

  // Simple seeded RNG (Mulberry32)
  function mulberry32(seed) {
    let t = seed >>> 0;
    return function () {
      t += 0x6D2B79F5;
      let r = Math.imul(t ^ (t >>> 15), 1 | t);
      r = (r + Math.imul(r ^ (r >>> 7), 61 | r)) ^ r;
      return ((r ^ (r >>> 14)) >>> 0) / 4294967296;
    };
  }

  // Generate Bernoulli(p) 0/1 using rng()
  const bernoulli = (p, rng) => (rng() < p ? 1 : 0);

  // Precompute m trajectories up to nMax:
  // returns { f: Array[m][nMax], x: [1..nMax] }
  function generateData(m, nMax, p, seed = Math.floor(Math.random() * 1e9)) {
    const rng = mulberry32(seed);
    const x = Array.from({ length: nMax }, (_, i) => i + 1);
    const f = new Array(m);
    for (let j = 0; j < m; j++) {
      let s = 0;
      const fj = new Array(nMax);
      for (let i = 0; i < nMax; i++) {
        s += bernoulli(p, rng);
        fj[i] = s / (i + 1); // relative frequency
      }
      f[j] = fj;
    }
    return { f, x, seed };
  }

  // Compute histogram of values in [0,1]
  function histogram(values, bins) {
    const counts = new Array(bins).fill(0);
    const edges = new Array(bins + 1);
    for (let i = 0; i <= bins; i++) edges[i] = i / bins;
    const centers = new Array(bins);
    for (let i = 0; i < bins; i++) centers[i] = (edges[i] + edges[i + 1]) / 2;
    for (const v of values) {
      let k = Math.floor(v * bins);
      if (k === bins) k = bins - 1; // include 1.0 in last bin
      if (k >= 0 && k < bins) counts[k]++;
    }
    return { centers, counts, edges };
  }

  // ---------- State ----------
  const state = {
    n: 200,
    nMax: 1000,
    m: 50,
    p: 0.5,
    bins: 20,
    seed: Math.floor(Math.random() * 1e9),
    playing: false,
    timer: null,
    data: null
  };

  // ---------- Charts ----------
  let trajChart, histChart;

  function buildTrajChart(ctx, data, pLine) {
    // For performance: one dataset per trajectory can be heavy for m>100.
    // Here we do multiple datasets but keep strokeWidth low.
    const labels = data.x;
    const datasets = data.f.slice(0, state.m).map((arr, idx) => ({
      label: `T${idx + 1}`,
      data: arr.map((y, i) => ({ x: i + 1, y })),
      parsing: false,
      segment: { borderWidth: 1 },
      borderWidth: 1,
      pointRadius: 0,
      borderColor: 'rgba(33,150,243,0.35)'
    }));
    // Reference line at p
    datasets.push({
      label: 'p',
      data: labels.map((n) => ({ x: n, y: state.p })),
      borderColor: 'rgba(220,0,0,0.9)',
      borderWidth: 2,
      borderDash: [6, 6],
      pointRadius: 0
    });

    if (trajChart) trajChart.destroy();
    trajChart = new Chart(ctx, {
      type: 'line',
      data: { datasets },
      options: {
        animation: false,
        maintainAspectRatio: false,
        scales: {
          x: {
            type: 'linear',
            min: 1,
            max: state.nMax,
            title: { display: true, text: 'n (trials)' }
          },
          y: {
            min: 0, max: 1,
            title: { display: true, text: 'f(n) = successes / n' }
          }
        },
        plugins: {
          legend: { display: false },
          tooltip: { mode: 'nearest', intersect: false }
        },
        elements: { line: { tension: 0 } }
      }
    });
  }

  function buildHistChart(ctx, values) {
    const { centers, counts } = histogram(values, state.bins);
    if (histChart) histChart.destroy();
    histChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: centers.map((c) => c.toFixed(2)),
        datasets: [{
          label: 'count',
          data: counts
        }]
      },
      options: {
        animation: false,
        maintainAspectRatio: false,
        scales: {
          x: { title: { display: true, text: 'f(n)' } },
          y: { title: { display: true, text: 'frequency' }, beginAtZero: true }
        },
        plugins: {
          legend: { display: false },
          annotation: {
            annotations: {
              pLine: {
                type: 'line',
                xMin: state.p.toFixed(2),
                xMax: state.p.toFixed(2),
                borderColor: 'rgba(220,0,0,0.9)',
                borderWidth: 2,
                borderDash: [6, 6]
              }
            }
          }
        }
      },
      plugins: []
    });
  }

  // Update only data (no rebuild) for speed
  function updateTrajectories() {
    if (!trajChart) return;
    // Update visible part up to state.n
    trajChart.options.scales.x.max = state.nMax;
    // Rebuild datasets to current m and p quickly
    const labels = state.data.x;
    const ds = state.data.f.slice(0, state.m).map((arr) => ({
      label: '',
      data: arr.slice(0, state.n).map((y, i) => ({ x: i + 1, y })),
      parsing: false,
      borderColor: 'rgba(33,150,243,0.35)',
      borderWidth: 1,
      pointRadius: 0
    }));
    ds.push({
      label: 'p',
      data: labels.slice(0, state.n).map((n) => ({ x: n, y: state.p })),
      borderColor: 'rgba(220,0,0,0.9)',
      borderWidth: 2,
      borderDash: [6, 6],
      pointRadius: 0
    });
    trajChart.data.datasets = ds;
    trajChart.update('none');
  }

  function updateHistogram() {
    if (!histChart) return;
    // Take f(n) for each trajectory at current n
    const vals = state.data.f.slice(0, state.m).map((arr) => arr[state.n - 1]);
    const { centers, counts } = histogram(vals, state.bins);
    histChart.data.labels = centers.map((c) => c.toFixed(2));
    histChart.data.datasets[0].data = counts;
    histChart.update('none');
  }

  function regenerate() {
    state.data = generateData(state.m, state.nMax, state.p, state.seed);
    // (Re)build charts first time
    buildTrajChart($('trajChart'), state.data);
    buildHistChart($('histChart'), state.data.f.map((arr) => arr[state.n - 1]));
    updateTrajectories();
    updateHistogram();
  }

  // ---------- Controls ----------
  function syncLabels() {
    $('nVal').textContent = state.n;
    $('nMaxVal').textContent = state.nMax;
    $('mVal').textContent = state.m;
    $('pVal').textContent = state.p.toFixed(2);
    $('binsVal').textContent = state.bins;
  }

  function attachEvents() {
    $('nSlider').addEventListener('input', (e) => {
      state.n = clamp(+e.target.value, 1, state.nMax);
      syncLabels(); updateTrajectories(); updateHistogram();
    });
    $('nMaxSlider').addEventListener('input', (e) => {
      state.nMax = +e.target.value;
      $('nSlider').max = state.nMax;
      if (state.n > state.nMax) { state.n = state.nMax; $('nSlider').value = state.n; }
      syncLabels();
      regenerate();
    });
    $('mSlider').addEventListener('input', (e) => {
      state.m = +e.target.value;
      syncLabels();
      regenerate();
    });
    $('pSlider').addEventListener('input', (e) => {
      state.p = +e.target.value;
      syncLabels();
      regenerate();
    });
    $('binsSlider').addEventListener('input', (e) => {
      state.bins = +e.target.value;
      syncLabels();
      updateHistogram();
    });
    $('playBtn').addEventListener('click', () => {
      if (state.playing) return;
      state.playing = true;
      state.timer = setInterval(() => {
        if (state.n < state.nMax) {
          state.n += 1;
          $('nSlider').value = state.n;
          $('nVal').textContent = state.n;
          updateTrajectories(); updateHistogram();
        } else {
          state.playing = false; clearInterval(state.timer);
        }
      }, 16); // ~60 FPS
    });
    $('pauseBtn').addEventListener('click', () => {
      state.playing = false; clearInterval(state.timer);
    });
    $('regenBtn').addEventListener('click', () => {
      // new random seed to change all trajectories
      state.seed = Math.floor(Math.random() * 1e9);
      regenerate();
    });
    $('resetBtn').addEventListener('click', () => {
      state.n = Math.min(200, state.nMax);
      $('nSlider').value = state.n;
      syncLabels();
      updateTrajectories(); updateHistogram();
    });
  }

  // ---------- Init ----------
  function init() {
    // Sync sliders to defaults
    $('nSlider').value = state.n;
    $('nMaxSlider').value = state.nMax;
    $('mSlider').value = state.m;
    $('pSlider').value = state.p;
    $('binsSlider').value = state.bins;
    syncLabels();
    attachEvents();
    regenerate();
  }

  document.addEventListener('DOMContentLoaded', init);
})();
