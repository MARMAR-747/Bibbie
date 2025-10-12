// assets/js/hw2_caesar.js

(function () {
  // ===== CONFIG =====
  const TEXT = `Statistics and cryptography can work together.
Caesar ciphers are simple but great for teaching ideas about frequency.`;

  const SHIFT = 5;

  const ENGLISH_FREQ = {
    a: 8.167, b: 1.492, c: 2.782, d: 4.253, e: 12.702, f: 2.228, g: 2.015,
    h: 6.094, i: 6.966, j: 0.153, k: 0.772, l: 4.025, m: 2.406, n: 6.749,
    o: 7.507, p: 1.929, q: 0.095, r: 5.987, s: 6.327, t: 9.056, u: 2.758,
    v: 0.978, w: 2.360, x: 0.150, y: 1.974, z: 0.074
  };

  const COMMON_BIGRAMS = ["th", "he", "in", "er", "an", "re", "on", "at", "en", "nd"];

  const A = "a".charCodeAt(0);
  const onlyLettersLower = s => s.toLowerCase().replace(/[^a-z]/g, "");
  const indexToLetter = i => String.fromCharCode(A + i);

  function caesar(str, shift) {
    shift = ((shift % 26) + 26) % 26;
    return str.replace(/[A-Za-z]/g, ch => {
      const isUpper = ch >= "A" && ch <= "Z";
      const base = isUpper ? 65 : 97;
      const code = ch.charCodeAt(0) - base;
      return String.fromCharCode(((code + shift) % 26) + base);
    });
  }

  function freqCounts(s) {
    const counts = Array(26).fill(0);
    for (const c of s) counts[c.charCodeAt(0) - A]++;
    return counts;
  }

  function freqObjectFromCounts(counts) {
    const obj = {};
    for (let i = 0; i < 26; i++) obj[indexToLetter(i)] = counts[i];
    return obj;
  }

  function sortFreqObject(obj) {
    return Object.entries(obj).sort((a, b) => b[1] - a[1]);
  }

  function chiSquare(obsCounts, expPercents) {
    const N = obsCounts.reduce((a, b) => a + b, 0) || 1;
    let chi = 0;
    for (let i = 0; i < 26; i++) {
      const expected = (expPercents[i] / 100) * N;
      if (expected > 0) {
        const diff = obsCounts[i] - expected;
        chi += (diff * diff) / expected;
      }
    }
    return chi;
  }

  function bigramScore(str) {
    const s = onlyLettersLower(str);
    let score = 0;
    for (const bg of COMMON_BIGRAMS) {
      const matches = s.match(new RegExp(bg, "g"));
      score += matches ? matches.length : 0;
    }
    return score;
  }

  function guessShiftByLanguage(cipher) {
    let best = { shift: 0, chi: Infinity, tie: -Infinity, plaintext: "" };
    for (let s = 0; s < 26; s++) {
      const candidate = caesar(cipher, 26 - s);
      const L = onlyLettersLower(candidate);
      const obs = freqCounts(L);
      const expPerc = Array(26).fill(0).map((_, i) => ENGLISH_FREQ[indexToLetter(i)]);
      const chi = chiSquare(obs, expPerc);
      const tie = bigramScore(candidate);
      const better = (chi < best.chi) || (chi === best.chi && tie > best.tie);
      if (better) best = { shift: s, chi, tie, plaintext: candidate };
    }
    return best;
  }

  function write(id, text) {
    const el = document.getElementById(id);
    if (el) el.textContent = text;
  }

  // ===== MAIN =====
  const letters = onlyLettersLower(TEXT);
  const counts = freqCounts(letters);
  const sortedCounts = sortFreqObject(freqObjectFromCounts(counts));

  const encrypted = caesar(TEXT, SHIFT);
  const guess = guessShiftByLanguage(encrypted);

  // ===== WRITE TO PAGE =====
  write("caesar-distribution", sortedCounts.map(([k,v]) => `${k}: ${v}`).join("\n"));
  write("caesar-encrypted", encrypted);
  write("caesar-decrypted", guess.plaintext);
  write("caesar-info", `Guessed shift: ${guess.shift} | Chi²: ${guess.chi.toFixed(2)} | Bigram score: ${guess.tie}`);
})();
