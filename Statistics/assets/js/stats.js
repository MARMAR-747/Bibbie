(function () {
  var el = document.getElementById('stats-numbers');
  if (!el) return;

  function randomNumbers(n) {
    var out = '';
    for (var i = 0; i < n; i++) {
      out += Math.floor(Math.random() * 10);
    }
    return out;
  }

  function update() {
    el.textContent = randomNumbers(10);
  }

  // Aggiornamento regolare
  update();
  setInterval(update, 250);
})();
