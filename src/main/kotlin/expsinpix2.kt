// файл main.kt
// Сделано на основе примера работы с графиками

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.*
import jetbrains.letsPlot.letsPlot
import kotlin.math.pow
import kotlin.math.sin

fun main() {

    val pi = 3.141592
    val e = 2.718281
    var dxVals = mutableListOf<Double>()                // Значения по x для построенного графика
    var dyVals = mutableListOf<Double>()                // Значения по н для построенного графика
    var dxIdVals = mutableListOf<Double>()              // Значения по x идеальной (теоретической) dN/dx
    var dyIdVals = mutableListOf<Double>()              // Значения по x идеальной (теоретической) dN/dx
    var dyIntVals = mutableListOf<Double>()
    val quarelOfNumb: Int = 10000000                     // Количество генерируемых точек
    val quarelOfBuckets: Int = 1000                      // Количество точек на графике распредения ("корзинок")
    val bucketStep: Double = 1 / (quarelOfBuckets * 1.0)    // Промежуток между корзинками
    // var currentBucketNumber: Int = 0                 // Текущий номер корзинки
    // var currentBucketVal: Int = 0                    // Количество точек в текущей корзинке
    var numCurr: Double = 0.0                           // Только что сгенерированное число

    // Создаем пустой массив "корзинок" для экспериментальной зависимости
    // потом будем "класть" в них числа: увеличивать значение на 1
    for (i in 0..quarelOfBuckets - 1) {
        dyVals.add(i, 0.0)
        dxVals.add(i, i * bucketStep)
    }

    // Создаем точки для функции exp(sin(pi x^2)) с которой будем сравнивать
    for (i in 0..quarelOfBuckets - 1) {
        dyIdVals.add(i, e.pow(sin(pi * i * i * bucketStep * bucketStep)))
        dxIdVals.add(i, (i + .5) * bucketStep)
    }

    // Берем интеграл чтобы нормировать
    dyIntVals.add(0, dyIdVals[0] * bucketStep)

    for (i in 1..quarelOfBuckets - 1) {
        dyIntVals.add(i, dyIntVals[i - 1] + dyIdVals[i] * bucketStep)
    }

    println(dyIntVals[quarelOfBuckets - 1])

    // Максимальное значение функции на [0,1] это e, чтобы при е у нас была максимальная вероятность
    // и чтобы не терять лишние значения сделаем значение в этой точке равным 1
    // тогда все сгенерированные для этого интервала значения попадут
    // а в точках в которых функция меньше будет теряться минимум
    for (i in 0..quarelOfBuckets - 1) {
        dyIdVals[i] = dyIdVals[i] / e
    }

    for (i in 0..quarelOfBuckets - 1) {
        for (s in 0..quarelOfNumb / quarelOfBuckets) {
            numCurr = (0..Int.MAX_VALUE).random() * 1.0 / Int.MAX_VALUE
            if (numCurr <= dyIdVals[i])
                dyVals[i]++
        }
    }

    for (i in 0..quarelOfBuckets - 1) {
        dyIdVals[i] = e*bucketStep * dyIdVals[i] / dyIntVals[quarelOfBuckets - 1]
    }

    var summ = 0.0      // сумма игреков
    var summx = 0.0     // сумма иксов для подсчета среднего

    // Подсчет среднего
    for(i in 0..quarelOfBuckets-1) {
        summ += dyVals[i]
        summx += dyVals[i] * i * bucketStep
    }

    // Вывод центра масс
    // В консоли можно посмотреть правильно ли рассчитан центр масс
    println(summx/summ)
    //println(summ)
    //println(quarelOfNumb)

    for (i in 0..quarelOfBuckets-1) {
        dyVals[i]= dyVals[i] / summ
    }

    val data = mapOf<String, Any>("dxvals" to dxVals, "dyvals" to dyVals)
    val dataId = mapOf<String, Any>("xivals" to dxIdVals, "yivals" to dyIdVals)

    // График экспериментальный + идеальный для exp(sin(pi x^2))
    val fig_2 = letsPlot(dataId + data) +
            geomPoint( color = "blue"
                , size = 4.0
            ) { x = "dxvals"; y = "dyvals" } +
            geomLine() { x = "dxvals"; y = "dyvals" }+
            geomPoint( color = "Red"
                , size = 2.0
            ) { x = "xivals"; y = "yivals" } +
                    geomLine() { x = "xivals"; y = "yivals" }

    ggsave(fig_2, "exp+id.png")

    // График экспериментальный для exp(sin(pi x^2))
    val fig_3 = letsPlot(data) +
    geomPoint( color = "Red"
        , size = 2.0
    ) { x = "dxvals"; y = "dyvals" } +
            geomLine() { x = "dxvals"; y = "dyvals" }
    ggsave(fig_3, "exp.png")

    println("bye!")
}
