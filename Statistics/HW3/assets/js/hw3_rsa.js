<div class="code-window">
  <div class="code-header">
    <span class="dot red"></span>
    <span class="dot yellow"></span>
    <span class="dot green"></span>
  </div>
  <pre><code class="language-javascript">
// assets/js/hw3_rsa.js
(function () {
  // ===== Helpers: aritmetica modulare =====
  function egcd(a, b) {
    if (b === 0) return [a, 1, 0];
    const [g, x1, y1] = egcd(b, a % b);
    return [g, y1, x1 - Math.floor(a / b) * y1];
  }
  function modinv(a, m) {
    const [g, x] = egcd(a, m);
    if (g !== 1) return null;
    return ((x % m) + m) % m;
  }
  function modPow(base, exp, mod) {
    let res = 1 % mod, b = base % mod, e = exp;
    while (e > 0) {
      if (e & 1) res = (res * b) % mod;
      b = (b * b) % mod;
      e >>= 1;
    }
    return res;
  }
  function isPrime(n){
    if (n < 2) return false;
    if (n % 2 === 0) return n === 2;
    for (let i = 3; i * i <= n; i += 2) if (n % i === 0) return false;
    return true;
  }

  // ===== Mappatura alfabeto & frequenze lingua =====
  const A = "a".charCodeAt(0);
  const ENGLISH_FREQ = { // %
    a: 8.167,b: 1.492,c: 2.782,d: 4.253,e:12.702,f: 2.228,g: 2.015,
    h: 6.094,i: 6.966,j: 0.153,k: 0.772,l: 4.025,m: 2.406,n: 6.749,
    o: 7.507,p: 1.929,q: 0.095,r: 5.987,s: 6.327,t: 9.056,u: 2.758,
    v: 0.978,w: 2.360,x: 0.150,y: 1.974,z: 0.074
  };
  const COMMON_BG = ["th","he","in","er","an","re","on","at","en","nd"];

  const onlyLettersLower = s => s.toLowerCase().replace(/[^a-z]/g, "");
  const idx2ch = i => String.fromCharCode(A + i);
  const ch2idx = ch => ch.charCodeAt(0) - A;

  // ===== Keygen (piccoli primi) =====
  function keygen(p, q, e = 17) {
    const n = p * q;
    const phi = (p - 1) * (q - 1);
    if (egcd(e, phi)[0] !== 1) throw new Error("e not coprime with phi");
    const d = modinv(e, phi);
    return { n, e, d, p, q, phi };
  }

  // ===== RSA letter-by-letter (0..25) =====
  function rsaEncryptLetters(plain, n, e) {
    const out = [];
    for (const ch of onlyLettersLower(plain)) {
      const m = ch2idx(ch);          // 0..25
      const c = modPow(m, e, n);     // m^e mod n
      out.push(c);
    }
    return out; // array di interi
  }
  function rsaDecryptLetters(nums, n, d) {
    return nums.map(c => {
      const m = modPow(c, d, n);
      return (m >= 0 && m <= 25) ? idx2ch(m) : "?";
    }).join("");
  }

  // ===== Scoring linguistico (chi-quadrato + bigrammi) =====
  function chiSquareFromText(text) {
    const s = onlyLettersLower(text);
    const counts = new Array(26).fill(0);
    for (const c of s) counts[ch2idx(c)]++;
    const N = counts.reduce((a,b)=>a+b,0) || 1;
    let chi = 0;
    for (let i=0;i<26;i++){
      const exp = (ENGLISH_FREQ[idx2ch(i)]/100)*N;
      if (exp > 0) {
        const diff = counts[i] - exp;
        chi += (diff*diff)/exp;
      }
    }
    return chi;
  }
  function bigramScore(text) {
    const s = onlyLettersLower(text);
    let score = 0;
    for (const bg of COMMON_BG) {
      const m = s.match(new RegExp(bg, "g"));
      score += m ? m.length : 0;
    }
    return score;
  }

  // ===== Attacco: prova piccoli (p,q) ed e tipici, valuta con Chi²+BG =====
  function parseCipher(str){
    return str.trim().split(/\s+/).filter(Boolean).map(Number);
  }
  function attackRSA(cipherStr) {
    const C = parseCipher(cipherStr);
    const smallPrimes = [];
    for (let x=29; x<=101; x++) if (isPrime(x)) smallPrimes.push(x);
    const eCandidates = [3,5,7,11,17];

    let best = {score: Infinity, tie: -Infinity, key: null, plain: ""};

    for (const p of smallPrimes) {
      for (const q of smallPrimes) {
        if (q === p) continue;
        const n = p*q;
        if (n <= 26) continue; // evita modulo ridicolo
        for (const e of eCandidates) {
          try {
            const { d } = keygen(p,q,e);
            if (d == null) continue;
            const plain = rsaDecryptLetters(C, n, d);
            if (plain.includes("?")) continue; // scarta mappe fuori 0..25
            const chi = chiSquareFromText(plain);
            const bgs = bigramScore(plain);
            const better = (chi < best.score) || (chi === best.score && bgs > best.tie);
            if (better) best = {score: chi, tie: bgs, key: {p,q,e,n,d}, plain};
          } catch(_) { /* chiavi non valide */ }
        }
      }
    }
    return best;
  }

  // ===== Demo (modifica DEMO_TEXT per il tuo testo) =====
  const DEMO_TEXT = `Freedom is not a starting point but a destination.
It is a slow, difficult conquest, built day by day through choices, actions, and thoughts. There is no moment in which we can truly say: “Now I am free.” Because at every moment, something tries to bind us again — fear, judgment, habit, memory.
Freedom is not the absence of constraints, but the ability to move within them without being crushed. It is like walking through a maze, knowing that each wall can be a guide rather than a prison.

Many confuse freedom with license — with the right to do whatever they please.
But being free does not mean being able to do everything; it means knowing what is worth doing, and doing it. It means choosing your own path, even when it is hard, even when others take the easier road.
Freedom is quiet; it does not need to shout. It lives in the smallest gestures: in telling the truth when lying would be easier; in staying faithful to an idea when everyone else has forgotten it; in looking at the world through your own eyes, not through the eyes others lend you.

Every true form of freedom is born from knowledge.
No one can be free without knowing from what they wish to be freed.
Those who do not recognize their own chains will never be able to break them.
That is why culture is the first form of freedom.
To read, to understand, to question — these acts liberate us more than any revolution. Human freedom begins the moment a person learns to think for themselves.

And yet, freedom is never solitude.
No one is truly free unless others are free as well.
Freedom is a good that grows only when shared — like air, like light, like hope.
It cannot be hoarded; whoever tries to keep it for themselves will lose it.

We live in times when freedom is often reduced to a word, a slogan, an abstract right. But true freedom does not need solemn declarations; it needs awareness, commitment, courage. It is a path measured in the everyday.
To be free is to accept responsibility for who we are.
It means not making excuses, not delegating, not looking away.
It means understanding that every choice we make shapes a part of the world, and that every free act is also a just one.

Perhaps absolute freedom does not exist. But that should not discourage us.
Freedom is made of degrees, of attempts, of falls and new beginnings.
It is a fragile flame that must be protected even when it seems useless.
And if one day we lose it, it will not be because someone took it from us, but because we failed to defend it.

Because freedom is not a gift.
It is a responsibility.
It is the highest test of our humanity.`;
  const demoKey = keygen(41,53,17);              // piccoli primi, esempio
  const cipherNums = rsaEncryptLetters(DEMO_TEXT, demoKey.n, demoKey.e);
  const cipherStr  = cipherNums.join(" ");

  // ===== Scrittura in pagina (se esistono i placeholder) =====
  function write(id, text) { const el = document.getElementById(id); if (el) el.textContent = text; }

  write("rsa-plaintext", DEMO_TEXT);
  write("rsa-public", `n=${demoKey.n}, e=${demoKey.e}  (p=${demoKey.p}, q=${demoKey.q})`);
  write("rsa-cipher", cipherStr);

  // Decifra con la privata (check)
  const decrypted = rsaDecryptLetters(cipherNums, demoKey.n, demoKey.d);
  write("rsa-decrypted", decrypted);

  // Attacco statistico (senza chiavi)
  const guess = attackRSA(cipherStr);
  if (guess && guess.key) {
    write("rsa-attack-plain", guess.plain);
    write("rsa-attack-key", `Best guess → p=${guess.key.p}, q=${guess.key.q}, e=${guess.key.e}, n=${guess.key.n}, d=${guess.key.d}  |  Chi²=${guess.score.toFixed(2)}  BG=${guess.tie}`);
  }
})();
  </code></pre>
</div>
