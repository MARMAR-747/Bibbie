document.addEventListener("DOMContentLoaded", () => {
  const svg = document.getElementById("vennDiagram");
  const A = document.getElementById("vennA");
  const B = document.getElementById("vennB");
  const text = document.getElementById("vennFormula");

  const r = 110;
  const ax = 200, ay = 160;
  const bx = 300, by = 160;

  function reset() {
    A.classList.remove("venn-highlight");
    B.classList.remove("venn-highlight");
    text.innerHTML = "Click inside A, B, or A ∩ B to highlight the region.";
  }

  const dist2 = (x1, y1, x2, y2) => {
    const dx = x1 - x2, dy = y1 - y2;
    return dx * dx + dy * dy;
  };

  svg.addEventListener("click", ev => {
    const rect = svg.getBoundingClientRect();
    const x = ev.clientX - rect.left;
    const y = ev.clientY - rect.top;

    const inA = dist2(x, y, ax, ay) <= r * r;
    const inB = dist2(x, y, bx, by) <= r * r;

    reset();

    if (inA && inB) {
      A.classList.add("venn-highlight");
      B.classList.add("venn-highlight");
      text.innerHTML = "Intersection A ∩ B highlighted.<br>P(A ∪ B) = P(A) + P(B) − P(A ∩ B)";
    } else if (inA) {
      A.classList.add("venn-highlight");
      text.innerHTML = "Region A highlighted.";
    } else if (inB) {
      B.classList.add("venn-highlight");
      text.innerHTML = "Region B highlighted.";
    }
  });

  svg.addEventListener("dblclick", reset);

  reset();
});

