(function () {
  // ---------- Helpers ----------
  const $ = (id) => document.getElementById(id);
  const clamp = (x, a, b) => Math.max(a, Math.min(b, x));

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

  // ---------- Overlay plugin (histogram on the right) ----------
  const histOverlay = {
    id: 'histOverlayV2',
    afterDraw(chart) {
      if (!state.hist.counts.length) return;
      const { ctx, chartArea } = chart;
      const { left, top, bottom, right, width, height } = chartArea;

      const W = width * 0.28;
      const gap = 10;
      const x0 = right - W;
      const y0 = top, H = height;

      ctx.save();
      ctx.fillStyle = 'rgba(255,255,0,0.06)';
      ctx.fillRect(x0 + gap, y0, W - gap, H);

      ctx.strokeStyle = 'rgba(0,0,0,0.15)';
      ctx.beginPath();
      ctx.moveTo(x0 + gap, y0);
      ctx.lineTo(x0 + gap, bottom);
      ctx.stroke();

      const counts = state.hist.counts;
      const bins = counts.length;
      const maxC = state.hist.max || 1;
      const barH = H / bins;

      for (let i = 0; i < bins; i++) {
        const c = counts[i];
        const len = (c / maxC) * (W - 2 * gap);
        const y = y0 + (bins - 1 - i) * barH + 2;
        ctx.fillStyle = 'rgba(255, 215, 0, 0.9)';     // bars
        ctx.strokeStyle = 'rgba(120, 120, 0, 0.7)';   // outline
        ctx.lineWidth = 1;
        ctx.fillRect(x0 + W - len, y, len, barH - 3);
        ctx.strokeRect(x0 + W - len, y, len, barH - 3);
      }

      // Marker line for p across the panel
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
  let chart;

  function buildChart() {
    const labels = state.data.x;
    const datasets = state.data.f.slice(0, state.m).map((arr) => ({
      label: '',
      data: arr.slice(0, state.n).map((y, i) => ({ x: i + 1, y })),
      parsing: false,
      borderColor: 'rgba(33,150,243,0.35)',
      borderWidth: 1,
      pointRadius: 0
    }));
    datasets.push({
      label: 'p',
      data: labels.slice(0, state.n).map((n) => ({ x: n, y: state.p })),
      borderColor: 'rgba(220,0,0,0.9)',
      borderWidth: 2,
      borderDash: [6, 6],
      pointRadius: 0
    });

    if (chart) chart.destroy();
    chart = new Chart($('trajChartV2'), {
      type: 'line',
      data: { datasets },
      options: {
        animation: false,
        maintainAspectRatio: false,
        scales: {
          x: { type: 'linear', min: 1, max: state.nMax, title: { display: true, text: 'n (trials)' } },
          y: { min: 0, max: 1, title: { display: true, text: 'f(n) = successes / n' } }
        },
        plugins: { legend: { display: false }, tooltip: { mode: 'nearest', intersect: false } },
        elements: { line: { tension: 0 } }
      },
      plugins: [histOverlay]
    });
  }

  function updateLines() {
    if (!chart) return;
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
    chart.data.datasets = ds;
    chart.options.scales.x.max = state.nMax;
    chart.update('none');
  }

  function updateHistogramOverlay() {
    const vals = state.data.f.slice(0, state.m).map((arr) => arr[state.n - 1]);
    const { centers, counts } = histogram(vals, state.bins);
    state.hist.centers = centers;
    state.hist.counts = counts;
    state.hist.max = Math.max(1, ...counts);
    if (chart) chart.update('none');
  }

  function regenerate() {
    state.data = generateData(state.m, state.nMax, state.p, state.seed);
    buildChart();
    updateHistogramOverlay();
  }

  // ---------- Controls ----------
  function syncLabels() {
    $('nValV2').textContent = state.n;
    $('nMaxValV2').textContent = state.nMax;
    $('mValV2').textContent = state.m;
    $('pValV2').textContent = state.p.toFixed(2);
    $('binsValV2').textContent = state.bins;
  }

  function attachEvents() {
    $('nSliderV2').addEventListener('input', (e) => {
      state.n = clamp(+e.target.value, 1, state.nMax);
      syncLabels(); updateLines(); updateHistogramOverlay();
    });
    $('nMaxSliderV2').addEventListener('input', (e) => {
      state.nMax = +e.target.value;
      $('nSliderV2').max = state.nMax;
      if (state.n > state.nMax) { state.n = state.nMax; $('nSliderV2').value = state.n; }
      syncLabels(); regenerate();
    });
    $('mSliderV2').addEventListener('input', (e) => {
      state.m = +e.target.value;
      syncLabels(); regenerate();
    });
    $('pSliderV2').addEventListener('input', (e) => {
      state.p = +e.target.value;
      syncLabels(); regenerate();
    });
    $('binsSliderV2').addEventListener('input', (e) => {
      state.bins = +e.target.value;
      syncLabels(); updateHistogramOverlay();
    });
    $('playBtnV2').addEventListener('click', () => {
      if (state.playing) return;
      state.playing = true;
      state.timer = setInterval(() => {
        if (state.n < state.nMax) {
          state.n += 1;
          $('nSliderV2').value = state.n;
          $('nValV2').textContent = state.n;
          updateLines(); updateHistogramOverlay();
        } else {
          state.playing = false; clearInterval(state.timer);
        }
      }, 16);
    });
    $('pauseBtnV2').addEventListener('click', () => {
      state.playing = false; clearInterval(state.timer);
    });
    $('regenBtnV2').addEventListener('click', () => {
      state.seed = Math.floor(Math.random() * 1e9);
      regenerate();
    });
    $('resetBtnV2').addEventListener('click', () => {
      state.n = Math.min(200, state.nMax);
      $('nSliderV2').value = state.n;
      syncLabels(); updateLines(); updateHistogramOverlay();
    });
  }

  // ---------- Init ----------
  function init() {
    $('nSliderV2').value = state.n;
    $('nMaxSliderV2').value = state.nMax;
    $('mSliderV2').value = state.m;
    $('pSliderV2').value = state.p;
    $('binsSliderV2').value = state.bins;
    syncLabels();
    attachEvents();
    regenerate();
  }
  document.addEventListener('DOMContentLoaded', init);
})();
