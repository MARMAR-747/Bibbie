<script>
  (function () {
    const A   = document.getElementById('sigmaA');
    const Ac  = document.getElementById('sigmaAc');
    const hint = document.getElementById('sigmaHint');

    function clear() {
      A.classList.remove('highlight');
      Ac.classList.remove('highlight');
      hint.textContent =
        'Hover over A or A^c to highlight their complementarity inside Ω.';
    }

    A.addEventListener('mouseenter', () => {
      A.classList.add('highlight');
      Ac.classList.remove('highlight');
      hint.textContent = 'A is highlighted (in red), its complement A^c is everything else inside Ω.';
    });

    Ac.addEventListener('mouseenter', () => {
      Ac.classList.add('highlight');
      A.classList.remove('highlight');
      hint.textContent = 'A^c is highlighted (in green), its complement A is the red region.';
    });

    ['mouseleave', 'mouseout'].forEach(ev => {
      A.addEventListener(ev, clear);
      Ac.addEventListener(ev, clear);
    });
  })();
</script>
