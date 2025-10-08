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
    const span = document.createElement('span');
    span.textContent = randomNumbers(10);
    el.innerHTML = '';            // pulisce la cifra precedente
    el.appendChild(span);
  }

  update();
  setInterval(update, 250);
})();
