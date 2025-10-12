/*  HW2 — Datasets & Distributions (Part II)
    Tasks:
    1) Letter distribution
    2) Caesar cipher (encrypt)
    3) Decrypt without knowing the shift using language distribution
*/

// ---------- Config ----------
const TEXT =
  `Statistics and cryptography can work together.
   Caesar ciphers are simple but great for teaching ideas about frequency.`;

// scegli lo shift per la cifratura (puoi cambiarlo)
const SHIFT = 5;

// Frequenze relative (%) delle lettere in inglese (stima classica)
const ENGLISH_FREQ = {
  a: 8.167, b: 1.492, c: 2.782, d: 4.253, e: 12.702, f: 2.228, g: 2.015,
  h: 6.094, i: 6.966, j: 0.153, k: 0.772, l: 4.025, m: 2.406, n: 6.749,
  o: 7.507, p: 1.929, q: 0.095, r: 5.987, s: 6.327, t: 9.056, u: 2.758,
  v: 0.978, w: 2.360, x: 0.150, y: 1.974, z: 0.074
};

// Bigrammi comuni in inglese per tie-breaker
const COMMON_BIGRAMS = ["th","he","in","er","an","re","on","at","en","nd"];

// ---------- Utility ----------
const A = "a".charCodeAt(0);
function onlyLettersLower(s) {
  return s.toLowerCase().replace(/[^a-z]/g, "");
}
function caesar(str, shift) {
  shift = ((shift % 26) + 26) % 26;
  return str.replace(/[A-Za-z]/g, ch => {
    const isUpper = ch >= 'A' && ch <= 'Z';
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
function toPercents(counts) {
  const total = counts.reduce((a,b)=>a+b,0) || 1;
  return counts.map(c => (c * 100) / total);
}
function chiSquare(obsCounts, expPercents) {
  const N = obsCounts.reduce((a,b)=>a+b,0) || 1;
  let chi = 0;
  for (let i=0;i<26;i++){
    const expected = (expPercents[i] / 100) * N;
    if (expected > 0) {
      const diff = obsCounts[i] - expected;
      chi += (diff*diff) / expected;
    }
  }
  return chi;
}
function bigramScore(str) {
  const s = onlyLettersLower(str);
  let score = 0;
  for (const bg of COMMON_BIGRAMS) {
    // peso leggero per non dominare sul chi-quadrato
    const matches = s.match(new RegExp(bg, "g"));
    score += (matches ? matches.length : 0);
  }
  return score;
}
function indexToLetter(i){ return String.fromCharCode(A + i); }
function freqObjectFromCounts(counts){
  const obj = {};
  for (let i=0;i<26;i++) obj[indexToLetter(i)] = counts[i];
  return obj;
}
function sortFreqObject(obj){
  return Object.entries(obj).sort((a,b)=>b[1]-a[1]);
}
function printTable(label, entries){
  console.log(`\n== ${label} ==`);
  for (const [k,v] of entries) console.log(`${k}: ${v}`);
}

// ---------- 1) Distribuzione lettere ----------
const letters = onlyLettersLower(TEXT);
const counts = freqCounts(letters);
const sortedCounts = sortFreqObject(freqObjectFromCounts(counts));
printTable("Letter distribution (counts, desc)", sortedCounts);

// ---------- 2) Cifratura Caesar ----------
const encrypted = caesar(TEXT, SHIFT);
console.log("\n== Caesar encryption ==");
console.log("Shift:", SHIFT);
console.log("Encrypted:\n", encrypted);

// ---------- 3) Decifratura senza conoscere lo shift ----------
function guessShiftByLanguage(cipher) {
  // per ogni shift candidato, decifra, calcola chi-quadrato, applica tie-breaker bigrammi
  let best = { shift: 0, chi: Infinity, tie: -Infinity, plaintext: "" };
  for (let s=0; s<26; s++) {
    const candidate = caesar(cipher, 26 - s); // inverti shift s
    const L = onlyLettersLower(candidate);
    const obs = freqCounts(L);
    const expPerc = Array(26).fill(0).map((_,i)=> ENGLISH_FREQ[indexToLetter(i)]);
    const chi = chiSquare(obs, expPerc);
    const tie = bigramScore(candidate); // più alto è meglio
    const better =
      (chi < best.chi) || (chi === best.chi && tie > best.tie);
    if (better) best = { shift: s, chi, tie, plaintext: candidate };
  }
  return best;
}

const guess = guessShiftByLanguage(encrypted);
console.log("\n== Caesar decryption (unknown shift) ==");
console.log("Guessed shift:", guess.shift);
console.log("Chi-square:", guess.chi.toFixed(2), "| Bigram score:", guess.tie);
console.log("Decrypted:\n", guess.plaintext);
