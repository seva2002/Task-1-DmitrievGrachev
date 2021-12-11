## Lets-plot example

Небольшой пример использования библиотеки `lets-plot`. Необходимые зависимости прописаны в файле
`build.gradle.kts`, и будут скачаны автоматически при сборке проекта с помощью Gradle.

Подробнее о сборке проектов, использующих lets-plot -
[здесь](https://github.com/JetBrains/lets-plot-kotlin/blob/master/README_DEV.md).

Для сборки в **IntelliJ IDEA**, возможно, потребуется изменить версию kotlin-плагина, указанную в
файле `build.gradle.kts`: текущую версию можно узнать в меню File/Settings/Plugins/Search Kotlin.
Текущую версию компонент `lets-plot` можно узнать [здесь](https://search.maven.org/). При
необходимости используемую версию Gradle можно изменить в файле
`gradle/wrapper/gradle-wrapper.properties` (например, на `gradle-6.8-bin.zip` или
`gradle-5.3.1-bin.zip`). После изменений конфигурации проекта необходимо выполнить View/Tool
Windows/Gradle/Reload All Gradle Projects.
