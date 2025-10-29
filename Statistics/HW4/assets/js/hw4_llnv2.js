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
  const bernoulli = (p, rng) => (rng() < p ? 1 : 0);

  // Precompute m trajectories up to nMax
  function generateData(m, nMax, p, seed = Math.floor(Math.random() * 1e9)) {
    const rng = mulberry32(seed);
    const x = Array.from({ length: nMax }, (_, i) => i + 1);
    const f = new Array(m);
    for (let j = 0; j < m; j++) {
      let s = 0;
      const fj = new Array(nMax);
      for (let i = 0; i < nMax; i++) {
        s += bernoulli(p, rng);
        fj[i] = s / (i + 1);
      }
      f[j] = fj;
    }
    return { f, x, seed };
  }

  // Histogram of values in [0,1]
  function histogram(values, bins) {
    const counts = new Array(bins).fill(0);
    const edges = new Array(bins + 1);
    for (let i = 0; i <= bins; i++) edges[i] = i / bins;
    const centers = new Array(bins);
    for (let i = 0; i < bins; i++) centers[i] = (edges[i] + edges[i + 1]) / 2;
    for (const v of values) {
      let k = Math.floor(v * bins);
      if (k === bins) k = bins - 1;
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
    data: null,
    hist: { centers: [], counts: [], max: 1 }
  };

  // ---------- Chart.js plugin: draw histogram overlay on the right ----------
  const histOverlay = {
    id: 'histOverlay',
    afterDraw(chart) {
      const { ctx, chartArea } = chart;
      const { left, top, bottom, right, width, height } = chartArea;

      // Reserve a vertical panel on the right (e.g., 28% width)
      const W = width * 0.28;
      const gap = 10; // gap between trajectories area and histogram
      const x0 = right - W; // left boundary of histogram panel
      const y0 = top, H = height;

      // Background (light) to distinguish panel
      ctx.save();
      ctx.fillStyle = 'rgba(255,255,0,0.06)'; // subtle
      ctx.fillRect(x0 + gap, y0, W - gap, H);

      // Axis lines
      ctx.strokeStyle = 'rgba(0,0,0,0.15)';
      ctx.beginPath();
      ctx.moveTo(x0 + gap, y0);
      ctx.lineTo(x0 + gap, bottom);
      ctx.stroke();

      // Draw histogram bars (horizontal)
      const counts = state.hist.counts;
      const bins = counts.length || 1;
      const maxC = state.hist.max || 1;

      const barH = H / bins;
      for (let i = 0; i < bins; i++) {
        const c = counts[i];
        const len = (c / maxC) * (W - 2 * gap); // normalized length
        const y = y0 + (bins - 1 - i) * barH + 2; // invert so low p at bottom
        ctx.fillStyle = 'rgba(255, 215, 0, 0.9)'; // yellow-ish
        ctx.strokeStyle = 'rgba(120, 120, 0, 0.7)';
        ctx.lineWidth = 1;
        ctx.fillRect(x0 + W - len, y, len, barH - 3);
        ctx.strokeRect(x0 + W - len, y, len, barH - 3);
      }

      // Label "f(n)" along the bottom of the histogram panel
      ctx.fillStyle = 'rgba(0,0,0,0.6)';
      ctx.font = '12px system-ui, -apple-system, Segoe UI, Roboto, sans-serif';
      ctx.textAlign = 'center';
      ctx.fillText('f(n)', x0 + gap + (W - gap) / 2, bottom - 6);

      // Vertical p line across the whole plot (already a dataset line; this just reinforces at panel)
      // Optional: draw a small marker at the panel mid corresponding to p
      const pY = chart.scales.y.getPixelForValue(state.p);
      ctx.strokeStyle = 'rgba(220,0,0,0.9)';
      ctx.setLineDash([6, 6]);
      ctx.beginPath();
      ctx.moveTo(x0 + gap, pY);
      ctx.lineTo(right, pY);
      ctx.stroke();
      ctx.setLineDash([]);

      ctx.restore();
    }
  };

  // ---------- Chart ----------
  let trajChart;

  function buildChart(ctx) {
    const labels = state.data.x;
    const lineDS = state.data.f.slice(0, state.m).map((arr) => ({
      label: '',
      data: arr.slice(0, state.n).map((y, i) => ({ x: i + 1, y })),
      parsing: false,
      borderColor: 'rgba(33,150,243,0.35)',
      borderWidth: 1,
      pointRadius: 0
    }));

    // Reference line at p (as a dataset so resta sincronizzato con lo zoom/scale)
    lineDS.push({
      label: 'p',
      data: labels.slice(0, state.n).map((n) => ({ x: n, y: state.p })),
      borderColor: 'rgba(220,0,0,0.9)',
      borderWidth: 2,
      borderDash: [6, 6],
      pointRadius: 0
    });

    if (trajChart) trajChart.destroy();
    trajChart = new Chart(ctx, {
      type: 'line',
      data: { datasets: lineDS },
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
      },
      plugins: [histOverlay]
    });
  }

  function updateLinesOnly() {
    if (!trajChart) return;
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
    trajChart.options.scales.x.max = state.nMax;
    trajChart.update('none'); // triggers overlay draw too
  }

  function updateHistogramOverlay() {
    // take f(n) across trajectories
    const vals = state.data.f.slice(0, state.m).map((arr) => arr[state.n - 1]);
    const { centers, counts } = histogram(vals, state.bins);
    state.hist.centers = centers;
    state.hist.counts = counts;
    state.hist.max = Math.max(1, ...counts);
    if (trajChart) trajChart.update('none');
  }

  function regenerate() {
    state.data = generateData(state.m, state.nMax, state.p, state.seed);
    buildChart($('trajChart'));
    updateHistogramOverlay();
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
      syncLabels();
      updateLinesOnly();
      updateHistogramOverlay();
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
      updateHistogramOverlay();
    });
    $('playBtn').addEventListener('click', () => {
      if (state.playing) return;
      state.playing = true;
      state.timer = setInterval(() => {
        if (state.n < state.nMax) {
          state.n += 1;
          $('nSlider').value = state.n;
          $('nVal').textContent = state.n;
          updateLinesOnly();
          updateHistogramOverlay();
        } else {
          state.playing = false; clearInterval(state.timer);
        }
      }, 16);
    });
    $('pauseBtn').addEventListener('click', () => {
      state.playing = false; clearInterval(state.timer);
    });
    $('regenBtn').addEventListener('click', () => {
      state.seed = Math.floor(Math.random() * 1e9);
      regenerate();
    });
    $('resetBtn').addEventListener('click', () => {
      state.n = Math.min(200, state.nMax);
      $('nSlider').value = state.n;
      syncLabels();
      updateLinesOnly();
      updateHistogramOverlay();
    });
  }

  // ---------- Init ----------
  function init() {
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
