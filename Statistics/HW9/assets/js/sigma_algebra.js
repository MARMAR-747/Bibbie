// Interactive effect for sigma-algebra diagram (A and A^c inside Ω)
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("setA");
  const Ac = document.getElementById("setAc");

  if (!A || !Ac) return;

  // Helper: highlight one, dim the other
  function focusA() {
    A.style.opacity = "1";
    Ac.style.opacity = "0.3";
  }
  function focusAc() {
    A.style.opacity = "0.3";
    Ac.style.opacity = "1";
  }
  function reset() {
    A.style.opacity = "1";
    Ac.style.opacity = "1";
  }

  // Hover A → dim A^c
  A.addEventListener("mouseenter", focusA);
  A.addEventListener("mouseleave", reset);

  // Hover A^c → dim A
  Ac.addEventListener("mouseenter", focu
