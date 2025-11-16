// Interactive effect for sigma-algebra diagram
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("setA");
  const Ac = document.getElementById("setAc");

  if (!A || !Ac) return;

  A.addEventListener("mouseenter", () => Ac.style.opacity = "0.3");
  A.addEventListener("mouseleave", () => Ac.style.opacity = "1");

  Ac.addEventListener("mouseenter", () => A.style.opacity = "0.3");
  Ac.addEventListener("mouseleave", () => A.style.opacity = "1");
});
