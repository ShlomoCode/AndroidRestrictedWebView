# הוראות התקנת תעודות נטפרי

## מה נשאר לך לעשות

הכל מוכן! נשאר רק להעלות את קבצי התעודות.

### שלב 1: שינוי שמות קבצים

חלק מהקבצים צריכים שינוי שם כי Android לא מאפשר:
- קבצים שמתחילים במספרים
- מקפים בשמות קבצים (צריך קו תחתון)

**אופציה א' - שימוש בסקריפט (מומלץ):**

```bash
# העתק את כל קבצי ה-.crt לתיקייה זמנית
mkdir /tmp/netfree_certs
cp *.crt /tmp/netfree_certs/
cd /tmp/netfree_certs/

# הרץ את הסקריפט
/home/user/AndroidRestrictedWebView/rename_certificates.sh

# העתק את כל הקבצים לפרויקט
cp *.crt /home/user/AndroidRestrictedWebView/app/src/main/res/raw/
```

**אופציה ב' - שינוי ידני:**

שנה את השמות הבאים:

| שם מקורי | שם חדש |
|----------|--------|
| `018.crt` | `netfree_018.crt` |
| `019.crt` | `netfree_019.crt` |
| `099.crt` | `netfree_099.crt` |
| `ib-itc.crt` | `ib_itc.crt` |
| `ib-partner.crt` | `ib_partner.crt` |
| `ib-spotnet.crt` | `ib_spotnet.crt` |
| `hadran-vpn.crt` | `hadran_vpn.crt` |
| `kosher-sim-cellcom.crt` | `kosher_sim_cellcom.crt` |
| `kosher-sim.crt` | `kosher_sim.crt` |
| `ksim-itc.crt` | `ksim_itc.crt` |
| `ksim-partner.crt` | `ksim_partner.crt` |
| `ksim-pele.crt` | `ksim_pele.crt` |
| `netfree-anywhere.crt` | `netfree_anywhere.crt` |
| `sim-kasher-triple-c.crt` | `sim_kasher_triple_c.crt` |

**הקבצים האלה לא צריכים שינוי:**
- `amitnet.crt`
- `bezeq.crt`
- `hot.crt`
- `itc.crt`
- `x2one.crt`
- `yossi.crt`

### שלב 2: העלאת הקבצים

העתק את **כל 20 הקבצים** (אחרי שינוי השמות) לתיקייה:
```
app/src/main/res/raw/
```

### שלב 3: בדיקה

וודא שיש לך 22 קבצים בתיקייה (20 תעודות + README.md + .gitkeep):

```bash
ls -l app/src/main/res/raw/
```

אמור להראות:
```
.gitkeep
README.md
amitnet.crt
bezeq.crt
hadran_vpn.crt
hot.crt
ib_itc.crt
ib_partner.crt
ib_spotnet.crt
itc.crt
kosher_sim.crt
kosher_sim_cellcom.crt
ksim_itc.crt
ksim_partner.crt
ksim_pele.crt
netfree_018.crt
netfree_019.crt
netfree_099.crt
netfree_anywhere.crt
sim_kasher_triple_c.crt
x2one.crt
yossi.crt
```

### שלב 4: בנייה

```bash
./gradlew assembleDebug
```

### שלב 5: התקנה ובדיקה

התקן על מכשיר עם נטפרי פעיל. האפליקציה אמורה לעבוד ללא שגיאות SSL.

---

## מה כבר נעשה

✅ חסימת HTTP - רק HTTPS מאושר
✅ Certificate Pinning מוגדר
✅ כל 20 התעודות מוגדרות ב-XML
✅ הסרת אמון בתעודות מערכת
✅ תיקייה מוכנה לקבצים
✅ תיעוד מלא

**כל מה שנשאר: להעלות את קבצי ה-.crt!**
