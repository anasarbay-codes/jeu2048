# كيفية تشغيل المشروع

## الطريقة 1: Maven (موصى بها)
```bash
mvn package
java -jar target/jeu-2048-1.0.jar
```

## الطريقة 2: Eclipse
1. افتح المشروع في Eclipse
2. أضف sqlite-jdbc-3.45.1.0.jar إلى Build Path
3. تحميل الـ JAR: https://github.com/xerial/sqlite-jdbc/releases
4. شغّل Main.java

## الطريقة 3: IntelliJ IDEA
- افتح المشروع كـ Maven Project
- IDEA ستحمّل sqlite-jdbc تلقائياً من pom.xml
- شغّل Main.java

## ملاحظة
- ملف `database.db` يُنشأ تلقائياً عند أول تشغيل
- يحفظ جميع الـ scores بشكل دائم
