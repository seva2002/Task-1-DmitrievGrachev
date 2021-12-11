// файл build.gradle.kts

// Этот плагин добавляет поддержку Котлина
plugins {
    kotlin("jvm") version "1.4.32"
}

// версия нашего проекта
version = "1.1"

// Используем известный репозиторий Java-проектов,
// поддерживаемый Apache Software foundation
repositories {
    mavenCentral()
}

// библиотеки из Maven Central,
// функции из которых мы будем использовать в нашем коде
dependencies {
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:3.0.2")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:2.1.0")
}
