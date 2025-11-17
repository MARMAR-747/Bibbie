<script>
  (function () {
    const svg   = document.getElementById('vennDiagram');
    const A     = document.getElementById('vennA');
    const B     = document.getElementById('vennB');
    const text  = document.getElementById('vennFormula');

    const r  = 110;
    const ax = 200, ay = 160;
    const bx = 300, by = 160;

    function clearHighlight() {
      A.classList.remove('venn-highlight');
      B.classList.remove('venn-highlight');
      text.innerHTML =
        'Click inside A, B, or the overlap to highlight the corresponding region.<br>' +
        'Double-click anywhere to reset.';
    }

    function distanceSq(x1, y1, x2, y2) {
      const dx = x1 - x2, dy = y1 - y2;
      return dx*dx + dy*dy;
    }

    svg.addEventListener('click', (ev) => {
      const rect = svg.getBoundingClientRect();
      const x = ev.clientX - rect.left;
      const y = ev.clientY - rect.top;

      const inA = distanceSq(x, y, ax, ay) <= r*r;
      const inB = distanceSq(x, y, bx, by) <= r*r;

      clearHighlight();

      if (inA && inB) {
        // intersection: highlight both
        A.classList.add('venn-highlight');
        B.classList.add('venn-highlight');
        text.innerHTML = 'You clicked on the intersection A ∩ B.<br>' +
          'Formula: <span style="font-family:serif;">P(A ∪ B) = P(A) + P(B) − P(A ∩ B)</span>';
      } else if (inA) {
        A.classList.add('venn-highlight');
        text.innerHTML = 'You clicked inside A only.<br>' +
          'This corresponds to the term P(A) in the formula.';
      } else if (inB) {
        B.classList.add('venn-highlight');
        text.innerHTML = 'You clicked inside B only.<br>' +
          'This corresponds to the term P(B) in the formula.';
      } else {
        // outside both → just reset message
        clearHighlight();
      }
    });

    svg.addEventListener('dblclick', (ev) => {
      ev.preventDefault();
      clearHighlight();
    });

    clearHighlight();
  })();
</script>
