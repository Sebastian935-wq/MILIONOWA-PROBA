# FAKE RANGI v1.4 — Fabric 1.21.4

Poprawki względem v1.3:
- Prefixy bez nawiasów: `GVIP Nick` zamiast `[GVIP] Nick` (TAB / HUD / lokalny napis nad głową).
- Czwarta opcja wyboru napisu: `VOUCHER` (różowy napis; wykorzystuje ikonę GVIP jako zastępczą).
- Klik lewym na voucher w GUI: podgląd vouchera na hotbarze pod warunkiem, że trzymasz dokładnie 1 sztukę przedmiotu.
- Prawy klik poza GUI: lokalne aktywowanie wybranej rangi (faktyczny przedmiot nadal może zostać użyty na serwerze).
- Cofnij/przywróć: usuwa tylko efekty lokalne, nie zmienia serwerowego ekwipunku.

**Istotne ograniczenie:** Aktualna mechanika jest tylko nakładką HUD na hotbar, nie prawdziwym przedmiotem ani podmianą modelu 3D w ręce. Inni gracze nadal widzą oryginalny przedmiot. Nie można obiecywać rzeczywistego vouchera, którego nie nadał serwer.

## Budowanie
Wrzuć wypakowany projekt do głównego katalogu repozytorium i uruchom `.github/workflows/main.yml` przez GitHub Actions. Prawidłowy plik to JAR bez `-sources`.

**Nie przetestowano uruchomienia w Minecraft.** Przetestuj najpierw na osobnym profilu Fabric 1.21.4 z Fabric API, bez innych modów. 
