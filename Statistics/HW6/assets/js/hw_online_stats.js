// ============================================================
// Online Mean & Variance (Welford's Algorithm)
// ============================================================
// This class updates the mean and variance incrementally,
// without storing past data and without recomputing everything.
// It is O(1) in memory and computation and numerically stable.

export class OnlineStats {
  constructor() {
    this.n = 0;      // Number of observed samples
    this.mean = 0;   // Running mean
    this.M2 = 0;     // Running sum of squared deviations
  }

  // Add one new data point x
  push(x) {
    this.n += 1;
    const delta = x - this.mean;   // Deviation from previous mean
    this.mean += delta / this.n;   // Update mean
    const delta2 = x - this.mean;  // Deviation from updated mean
    this.M2 += delta * delta2;     // Update M2 for variance tracking
  }

  // Sample variance (unbiased estimator)
  get variance() {
    return this.n > 1 ? this.M2 / (this.n - 1) : 0;
  }

  // Sample standard deviation
  get std() {
    return Math.sqrt(this.variance);
  }
}


// ============================================================
// Streaming Demo Controller
// ============================================================
// This function attaches chart + live updates to the page.

export function startOnlineDemo(outputElementId, canvasElementId, pauseButtonId, resetButtonId, listElementId) {
  const stats = new OnlineStats();
  const output = document.getElementById(outputElementId);
  const canvas = document.getElementById(canvasElementId).getContext("2d");
  const pauseBtn = document.getElementById(pauseButtonId);
  const resetBtn = document.getElementById(resetButtonId);
  const listBox = document.getElementById(listElementId);

  let isRunning = true;
  let values = []; // store recent values for display

  const xValues = [];
  const meanValues = [];

  const chart = new Chart(canvas, {
    type: "line",
    data: {
      labels: xValues,
      datasets: [{
        label: "Running Mean",
        data: meanValues,
        borderColor: "orange",
        borderWidth: 2,
        fill: false,
        tension: 0.15
      }]
    },
    options: { responsive: true, animation: false }
  });

  function refreshOutput() {
    output.textContent =
      `Count:    ${stats.n}\n` +
      `Mean:     ${stats.mean.toFixed(4)}\n` +
      `Variance: ${stats.variance.toFixed(4)}\n` +
      `Std Dev:  ${stats.std.toFixed(4)}`;
  }

  function refreshList() {
    listBox.textContent = values.join(", ");
  }

  pauseBtn.addEventListener("click", () => {
    isRunning = !isRunning;
    pauseBtn.textContent = isRunning ? "⏸ Pause" : "▶ Play";
  });

  resetBtn.addEventListener("click", () => {
    stats.n = 0;
    stats.mean = 0;
    stats.M2 = 0;
    values = [];
    xValues.length = 0;
    meanValues.length = 0;
    chart.update();
    refreshOutput();
    refreshList();
  });

  setInterval(() => {
    if (!isRunning) return;

    const x = Math.random() * 10; // example: new value in [0,10]
    stats.push(x);

    values.push(x.toFixed(2));
    if (values.length > 200) values.shift(); // keep list manageable

    xValues.push(stats.n);
    meanValues.push(stats.mean);

    if (xValues.length > 200) {
      xValues.shift();
      meanValues.shift();
    }

    chart.update();
    refreshOutput();
    refreshList();

  }, 1000);
}
