---
layout: default
title: La mia build
nav_order: 7
---

<script>
  document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('theme-toggle');
    const saved = localStorage.getItem('theme');
    if (saved) {
      jtd.setTheme(saved);
      if (btn) btn.textContent = saved === 'dark' ? '☀️' : '🌙';
    }
    if (btn) {
      btn.addEventListener('click', () => {
        const curr = jtd.getTheme();
        const next = curr === 'dark' ? 'light' : 'dark';
        jtd.setTheme(next);
        localStorage.setItem('theme', next);
        btn.textContent = next === 'dark' ? '☀️' : '🌙';
      });
    }
  });
</script>

# 💻 La mia build
---

<img src="{{ '/assets/images/Nuclear Reactor-share.png' | relative_url }}" width="100%">  

BuildCores Parts List: [Link](https://buildcores.com/builds/KgPW5luha?share=true)

 
|**Component**|**Title**|**Price**|
|:-|:-|:-|
|Case|[Corsair 3500X ARGB ATX Mid Tower White Tempered Glass](https://buildcores.com//products/PCCase/csoz78cnd/Corsair-3500X-ARGB-ATX-Mid-Tower-White-Tempered-Glass)|[$109.99 (Amazon)](https://amazon.com/dp/B0CZV1KPXL?tag=buildcoresx-20)|
|CPU|[AMD Ryzen 7 7800X3D 4.2 GHz 8-Core AM5](https://buildcores.com//products/CPU/8apivzgu6/AMD-Ryzen-7-7800X3D-4.2-GHz-8-Core-AM5)|[$385.99 (Amazon)](https://amazon.com/dp/B0BTZB7F88?tag=buildcoresx-20)|
|Motherboard|[Asus B850-F ROG STRIX GAMING WIFI DDR5 ATX](https://buildcores.com//products/Motherboard/umear9rtw/Asus-B850-F-ROG-STRIX-GAMING-WIFI-DDR5-ATX)|[$259.00 (Amazon)](https://amazon.com/dp/B0DPLQWLBD?tag=buildcoresx-20)|
|GPU|[Gigabyte AERO OC GeForce RTX 5070 Ti 16 GB](https://buildcores.com//products/GPU/1q9g3vmsx/Gigabyte-AERO-OC-GeForce-RTX-5070-Ti-16-GB)|[$859.99 (Amazon)](https://amazon.com/dp/B0DTQVHQ6G?tag=buildcoresx-20)|
|RAM|[Corsair Vengeance RGB DDR5-6000 CL30 32GB (2x16GB)](https://buildcores.com//products/RAM/3z798wdqg/Corsair-Vengeance-RGB-DDR5-6000-CL30-32GB-(2x16GB))|[$556.99 (Best Buy)](https://api.bestbuy.com/click/-/6580809/pdp?IPID=2299452)|
|CPU Cooler|[NZXT Kraken Water 360mm RGB 78.02 CFM White](https://buildcores.com//products/CPUCooler/bk0z1zmyl/NZXT-Kraken-Water-360mm-RGB-78.02-CFM-White)|[$219.99 (Best Buy)](https://api.bestbuy.com/click/-/6541446/pdp?IPID=2299452)|
|Storage|[Samsung 9100 PRO 2TB SSD M.2 PCIe 5.0 NVMe](https://buildcores.com//products/Storage/zoxaf18m0/Samsung-9100-PRO-2TB-SSD-M.2-PCIe-5.0-NVMe)|[$283.03 (Amazon)](https://amazon.com/dp/B0DX2DPJZ5?tag=buildcoresx-20)|
|Storage|[Seagate Barracuda 2TB HDD 3.5" 7200RPM SATA](https://buildcores.com//products/Storage/0sq55h58v/Seagate-Barracuda-2TB-HDD-3.5%22-7200RPM-SATA)|[$69.99 (Best Buy)](https://api.bestbuy.com/click/-/6616036/pdp?IPID=2299452)|
|Power Supply|[MSI MAG A850GL PCIE5 850W Fully Modular 80+ Gold Certified](https://buildcores.com//products/PSU/v0qv6k9dp/MSI-MAG-A850GL-PCIE5-850W-Fully-Modular-80%2B-Gold-Certified)|[$129.97 (Newegg)](https://click.linksynergy.com/deeplink?id=ZoXDlCrTuis&mid=44583&murl=https%3A%2F%2Fwww.newegg.com%2Fmsi-atx12v-850-w-80-plus-gold-certified-power-supplies-black-mag-a850gl-pcie5%2Fp%2FN82E16817701021%3Fitem%3DN82E16817701021)|
|Monitor|[Samsung Odyssey G60SD 27" 1440p 360Hz QD-OLED Monitor](https://buildcores.com//products/Monitor/w2ajnm0xt/Samsung-Odyssey-G60SD-27%22-1440p-360Hz-QD-OLED-Monitor)|[$749.99 (Amazon)](https://amazon.com/dp/B0D1DPFZLZ?tag=buildcoresx-20)|
| | | |
|**Total Price**|**$3624.93**| |
|[BuildCores Web](https://buildcores.com)|[BuildCores iOS](https://apps.apple.com/us/app/buildcores-mobile-pc-building/id1441971434)|[BuildCores Android](https://play.google.com/store/apps/details?id=com.buildcores.buildcores&pcampaignid=web_share)|

