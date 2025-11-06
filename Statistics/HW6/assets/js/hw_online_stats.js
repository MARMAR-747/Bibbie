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

export function startOnlineDemo(outputElementId, canvasElementId, buttonElementId) {
  const stats = new OnlineStats();
  const output = document.getElementById(outputElementId);
  const canvas = document.getElementById(canvasElementId).getContext("2d");
  const button = document.getElementById(buttonElementId);

  let isRunning = true; // controls play / pause

  // Chart storage
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
    options: {
      responsive: true,
      animation: false
    }
  });

  // Toggle play/pause when button is pressed
  button.addEventListener("click", () => {
    isRunning = !isRunning;
    button.textContent = isRunning ? "⏸ Pause" : "▶ Play";
  });

  setInterval(() => {
    if (!isRunning) return; // do nothing if paused

    const x = Math.random() * 10;
    stats.push(x);

    xValues.push(stats.n);
    meanValues.push(stats.mean);

    if (xValues.length > 200) {
      xValues.shift();
      meanValues.shift();
    }

    chart.update();

    output.textContent =
      `Count:    ${stats.n}\n` +
      `Mean:     ${stats.mean.toFixed(4)}\n` +
      `Variance: ${stats.variance.toFixed(4)}\n` +
      `Std Dev:  ${stats.std.toFixed(4)}`;

  }, 1000);
}
