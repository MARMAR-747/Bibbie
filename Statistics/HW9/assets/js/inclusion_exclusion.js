// Interactive Venn diagram for inclusion–exclusion
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("vennA");
  const B = document.getElementById("vennB");
  const svg = document.getElementById("vennSVG");
  const formula = document.getElementById("inExFormula");

  if (!A || !B || !svg || !formula) return;

  function renderLatex(latex) {
    // latex è una stringa tipo "P(A \\cup B)=..." etc.
    formula.innerHTML = "$$" + latex + "$$";
    if (window.MathJax && window.MathJax.typesetPromise) {
      MathJax.typesetPromise();
    }
  }

  // Centri dei due cerchi in coordinate SVG (dalla viewBox)
  const cxA = 75, cyA = 75, rA = 50;
  const cxB = 125, cyB = 75, rB = 50;

  // Trasforma coordinate mouse → coordinate SVG
  function getSVGCoords(evt) {
    const pt = svg.createSVGPoint();
    pt.x = evt.clientX;
    pt.y = evt.clientY;
    const inverted = svg.getScreenCTM().inverse();
    return pt.matrixTransform(inverted);
  }

  // Reset completo
  function resetAll() {
    A.style.opacity = "1";
    B.style.opacity = "1";
    renderLatex("P(A \\cup B) = P(A) + P(B) - P(A \\cap B)");
  }

  // Click solo su A → P(A)
  A.addEventListener("click", (e) => {
    A.style.opacity = "1";
    B.style.opacity = "0.3";
    renderLatex("P(A)");
    e.stopPropagation();
  });

  // Click solo su B → P(B)
  B.addEventListener("click", (e) => {
    A.style.opacity = "0.3";
    B.style.opacity = "1";
    renderLatex("P(B)");
    e.stopPropagation();
  });

  // Click sullo SVG: se è nella zona comune → P(A ∩ B)
  svg.addEventListener("click", (e) => {
    const p = getSVGCoords(e);
    const X = p.x;
    const Y = p.y;

    const inA = (X - cxA) ** 2 + (Y - cyA) ** 2 <= rA ** 2;
    const inB = (X - cxB) ** 2 + (Y - cyB) ** 2 <= rB ** 2;

    // Se è dentro entrambi i cerchi → intersezione
    if (inA && inB) {
      A.style.opacity = "1";
      B.style.opacity = "1";
      renderLatex("P(A \\cap B)");
    }
  });

  // Doppio click ovunque sullo SVG → reset formula completa
  svg.addEventListener("dblclick", (e) => {
    e.preventDefault();
    resetAll();
  });

  // Stato iniziale
  resetAll();
});
