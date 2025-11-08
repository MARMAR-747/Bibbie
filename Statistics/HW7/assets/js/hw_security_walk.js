// ============================================================
// Security Random Walk Simulation
// ============================================================
// Simulates m attackers per week for n weeks. Every week:
// +1 if server remains secure, -1 if at least one attacker breaches.
// Repeats this process for `runs` trajectories, then counts final scores.

export function simulateSecurityWalk(n, m, p, runs) {
  const scoreCounts = {}; // frequency table of final scores

  // Probability server remains secure during a week:
  const q = Math.pow(1 - p, m);

  for (let r = 0; r < runs; r++) {
    let score = 0;
    for (let week = 0; week < n; week++) {
      const secure = Math.random() < q;
      score += secure ? +1 : -1;
    }
    scoreCounts[score] = (scoreCounts[score] || 0) + 1;
  }

  return { scoreCounts, q };
}


// ============================================================
// Plotting the Result (Histogram + Theoretical Binomial Overlay)
// ============================================================

export function plotResults(scoreCounts, q, n, runs, canvasId) {
  const ctx = document.getElementById(canvasId).getContext("2d");

  const scores = Object.keys(scoreCounts).map(Number).sort((a,b)=>a-b);
  const empirical = scores.map(s => scoreCounts[s] / runs);

  function binomial(k, n) {
    let r = 1;
    for (let i = 1; i <= k; i++) {
      r = r * (n - i + 1) / i;
    }
    return r;
  }

  const theoretical = scores.map(s => {
    const K = (s + n) / 2;
    if (K % 1 !== 0 || K < 0 || K > n) return 0;
    return binomial(K, n) * Math.pow(q, K) * Math.pow(1 - q, n - K);
  });

  new Chart(ctx, {
    type: "bar",
    data: {
      labels: scores,
      datasets: [
        {
          label: "Empirical Simulation",
          data: empirical,
          backgroundColor: "orange",
        },
        {
          label: "Theoretical (Binomial)",
          data: theoretical,
          type: "line",
          borderColor: "red",
          borderWidth: 2,
        }
      ]
    },
    options: {
      responsive: true,
      scales: {
        x: { title: { display: true, text: "Final Score Sₙ" }},
        y: { title: { display: true, text: "Probability" }}
      }
    }
  });
}
