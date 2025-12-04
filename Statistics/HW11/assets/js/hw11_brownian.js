// =====================================================================
// HW11 - Realtime Brownian Motion Simulation + Histogram of B(T)
// =====================================================================

function boxMuller() {
  let u = 0, v = 0;
  while (u === 0) u = Math.random();
  while (v === 0) v = Math.random();
  return Math.sqrt(-2 * Math.log(u)) * Math.cos(2 * Math.PI * v);
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
