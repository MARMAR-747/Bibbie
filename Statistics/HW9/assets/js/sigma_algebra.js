document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("sigmaA");
  const Ac = document.getElementById("sigmaAc");
  const hint = document.getElementById("sigmaHint");

  function reset() {
    A.classList.remove("highlight");
    Ac.classList.remove("highlight");
    hint.textContent = "Hover over A or Aᶜ to highlight the complementary regions.";
  }

  A.addEventListener("mouseenter", () => {
    A.classList.add("highlight");
    Ac.classList.remove("highlight");
    hint.textContent = "A is highlighted (red). Aᶜ is the rest of Ω.";
  });

  Ac.addEventListener("mouseenter", () => {
    Ac.classList.add("highlight");
    A.classList.remove("highlight");
    hint.textContent = "Aᶜ is highlighted (green). A is the red region.";
  });

  A.addEventListener("mouseleave", reset);
  Ac.addEventListener("mouseleave", reset);

  reset();
});
