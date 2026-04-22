# Single Site Android Browser

הפרויקט מאפשר לך ליצור אפליקציית אנדרואיד עבור כל אתר אינטרנט, עם אפשרות להגביל את הגישה לאתרים באמצעות רשימה לבנה.

בנוי כהרחבה למתקדמים לפרויקט:
https://github.com/ShlomoCode/AndroidRestrictedWebView

### תכונות

- מאפשר רשימה לבנה לא מוגבלת.
- מאפשר לציין כתובת ייחודית שתהיה נקודת ההתחלה (אם לא צוינה, יתחיל בכתובת הראשונה ברשימה הלבנה).
- טיפול ב-URI מותאם של `tel`, `mailto` ו-`whatsapp`.
- כפיית מצב תצוגה.
- חסימת מדיה.
- מצב no ssl.
- הסתרת הבאנר של נטפרי.
- טעינה מחדש של הדף בהחלקה למטה בשליש העליון של המסך.

## Configuration - קונפיגורציה

ניתן לערוך את ההגדרות ישירות ב-[`app/build.gradle`](./app/build.gradle) (או לטעון באמצעות קובץ `env` שממוקם בתיקיית `app`).

לאחר ביצוע השינויים יש לבצע הידור מחדש של הפרויקט כדי לסנכרן את השינויים.

### אפשרויות בקובץ `env`:

```env
ALLOWED_DOMAINS=example.com // כתובות דומיין שיאופשרו באפליקציה
STARTUP_URL=example.com // כתובת האתר שבו תתחיל האפליקציה
ALLOW_GOOGLE_LOGIN=true // אפשר התחברות עם גוגל
VIEW_MODE=AUTO // מצב תצוגה (אפשרי: AUTO, PORTRAIT, LANDSCAPE)
BLOCK_MEDIA=false
BLOCK_ADS=true
NO_SSL=false
HIDE_NETFREE=true // הסתרת הבאנר של נטפרי
APP_NAME=my app
APPLICATION_ID=com.myapp.webapp
VERSION=1.0.0
APP_ICON_PATH= // אייקון לאפליקציה, יש לשים לב שצריך לוכסנים כפולים או הפוכים
```

## License - רישיון

This application is distributed under the GNU General Public License version 3 (GNU GPL-3.0). See the [LICENSE](LICENSE) file for more details.

## Disclaimer - כתב ויתור

לידיעתך, אינני עורך דין ואין לראות במידע הניתן כאן ייעוץ משפטי. חשוב להתייעץ עם גורם משפטי מוסמך כדי להבטיח עמידה בכל דרישות הרישוי הרלוונטיות וחובות.

Please note that I am not a lawyer and the information provided here should not be considered legal advice. It is important to consult with a qualified legal professional to ensure compliance with all relevant licensing requirements and obligations.
