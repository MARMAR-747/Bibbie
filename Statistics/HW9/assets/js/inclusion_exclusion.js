// Interactive Venn diagram for inclusion–exclusion
document.addEventListener("DOMContentLoaded", () => {
  const A = document.getElementById("vennA");
  const B = document.getElementById("vennB");
  const svg = document.getElementById("vennSVG");
  const formula = document.getElementById("inExFormula");

  if (!A || !B || !svg || !formula) return;

  function renderLatex(latex) {
    formula.innerHTML = "$$" + latex + "$$";
    if (window.MathJax && window.MathJax.typesetPromise) {
      MathJax.typesetPromise();
    }
  }

  A.addEventListener("click", (e) => {
    A.style.opacity = "1";
    B.style.opacity = "0.3";
    renderLatex("P(A)");
    e.stopPropagation();
  });

  B.addEventListener("click", (e) => {
    A.style.opacity = "0.3";
    B.style.opacity = "1";
    renderLatex("P(B)");
    e.stopPropagation();
  });

  svg.addEventListener("click", (e) => {
    const rect = svg.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const sx = svg.viewBox.baseVal.width / rect.width;
    const sy = svg.viewBox.baseVal.height / rect.height;
    const X = x * sx;
    const Y = y * sy;

    const inA = (X - 75) ** 2 + (Y - 75) ** 2 <= 50 ** 2;
    const inB = (X - 125) ** 2 + (Y - 75) ** 2 <= 50 ** 2;

    if (inA && inB) {
      A.style.opacity = "1";
      B.style.opacity = "1";
      renderLatex("P(A \\\\cap B)");
    }
  });

  svg.addEventListener("dblclick", () => {
    A.style.opacity = "1";
    B.style.opacity = "1";
    renderLatex("P(A\\\\cup B)=P(A)+P(B)-P(A\\\\cap B)");
  });
});
