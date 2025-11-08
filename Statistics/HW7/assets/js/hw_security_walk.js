export function simulateSecurityWalk(n, m, p, runs) {
  const scoreCounts = {}; // track final scores frequency

  // Probability server stays secure in a given week
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

export function plotResults(scoreCounts, q, n, runs, canvasId) {
  const ctx = document.getElementById(canvasId).getContext("2d");

  const scores = Object.keys(scoreCounts).map(Number).sort((a,b)=>a-b);
  const empirical = scores.map(s => scoreCounts[s] / runs);

  // Theoretical binomial distribution for comparison
  const theoretical = scores.map(s => {
    const K = (s + n) / 2;
    if (K % 1 !== 0 || K < 0 || K > n) return 0;
    const binom = (k, n) => {
      let r = 1;
      for (let i = 1; i <= k; i++) r = r * (n - i + 1) / i;
      return r;
    };
    return binom(K, n) * Math.pow(q, K) * Math.pow(1 - q, n - K);
  });

  new Chart(ctx, {
    type: "bar",
    data: {
      labels: scores,
      datasets: [
        { label: "Empirical", data: empirical, backgroundColor: "orange" },
        { label: "Theoretical (Binomial)", data: theoretical, type: "line", borderColor: "red", borderWidth: 2 }
      ]
    },
    options: {
      responsive: true,
      scales: { x: { title: { text: "Final Score", display: true }},
                y: { title: { text: "Probability", display: true }} }
    }
  });
}
