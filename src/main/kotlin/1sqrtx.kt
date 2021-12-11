// файл main.kt
// Сделано на основе примера работы с графиками

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.*
import jetbrains.letsPlot.letsPlot
import kotlin.math.pow

fun main() {

    // var xVals = mutableListOf<Double>()              // Значения по x в массиве точек (Нужно было для отладки в начале)
    // var yVals = mutableListOf<Double>()              // Значения по y (просто квадраты иксов)
    var dxVals = mutableListOf<Double>()                // Значения по x для построенного графика
    var dyVals = mutableListOf<Double>()                // Значения по н для построенного графика
    var dxIdVals = mutableListOf<Double>()              // Значения по x идеальной (теоретической) dN/dx
    var dyIdVals = mutableListOf<Double>()              // Значения по x идеальной (теоретической) dN/dx
    var quarelOfNumb: Int = 1000000                     // Количество генерируемых точек
    var quarelOfBuckets: Int = 1000                     // Количество точек на графике распредения ("корзинок")
    var bucketStep: Double = 1/(quarelOfBuckets*1.0)    // Промежуток между корзинками
    // var currentBucketNumber: Int = 0                 // Текущий номер корзинки
    // var currentBucketVal: Int = 0                    // Количество точек в текущей корзинке
    var numCurr: Double = 0.0                           // Только что сгенерированное число

    // Создаем пустой массив "корзинок" для экспериментальной зависимости
    // потом будем "класть" в них числа: увеличивать значение на 1
    for (i in 0..quarelOfBuckets-1) {
        dyVals.add(i, 0.0)
        dxVals.add(i, (i+.5) * bucketStep)
    }

    // Создаем точки для функции 1/sqrt(x) с которой будем сравнивать
    for (i in 0..quarelOfBuckets-1) {
        dyIdVals.add(i, 0.5*bucketStep / (bucketStep*(i+.5)).pow(0.5))//1 / sqrt(i * bucketStep)
        dxIdVals.add(i, (i+.5) * bucketStep)
    }

    for(i in 0..quarelOfNumb) {
        numCurr = (0..Int.MAX_VALUE).random() * 1.0 / Int.MAX_VALUE
        // xVals.add(i, numCurr)
        // yVals.add(i, numCurr * numCurr)
        dyVals[(numCurr*numCurr / bucketStep).toInt()]++ // Кладем число в корзинку с номером y_vals[i] / bucketStep).toInt()
        // y_vals[i] / bucketStep).toInt() будет иметь величину от 0 до (количества корзинок - 1)
        // таким образом мы положим число в корзинку соответсвующую промежутку от i*bucketStep до (i+1)*bucketStep
        // например число 0.12345 при количестве корзинок 10 попадет в 1 (есть еще нулевая)
        // а при 100 -- в 12

        //println((y_vals[i] / bucketStep).toInt())
    }

    var summ = 0.0      // сумма игреков
    var summx = 0.0     // сумма иксов для подсчета среднего

    // Подсчет среднего
    for(i in 0..quarelOfBuckets-1) {
        summ += dyVals[i] * bucketStep
        summx += dyVals[i] * i * bucketStep * bucketStep
    }
    // Вывод центра масс
    // В консоли можно посмотреть молодциы мы или нет
    println(summx/summ)

    for (i in 0..quarelOfBuckets-1) {
        dyVals[i]= dyVals[i] / quarelOfNumb

        // Вывод для экселя, нужно было получить коэффициэнт
        // нормировки для теоретического графика
        // не могли угадать что там кроме bucketStep еще и 0,5
        //print(dx_vals[i])
        //print("\t")
        //println(dy_vals[i])
    }

    // Точно по образцу
    // val data = mapOf<String, Any>("xvals" to xVals, "yvals" to yVals)

    val data = mapOf<String, Any>("xvals" to dxVals, "yvals" to dyVals)
    val dataId = mapOf<String, Any>("xivals" to dxIdVals, "yivals" to dyIdVals)
    // график точек нужен был в процессе отладки
    /*
    val fig_1 = letsPlot(data) +
            geomPoint( color = "dark-green"
                     , size = 3.0
                     ) { x = "xvals"; y = "yvals" } +
            geomLine() { x = "xvals"; y = "yvals" }

    ggsave(fig_1, "plot_1.png")
    */

    // График экспериментальный + идеальный для 1/sqrt(x)
    val fig_2 = letsPlot(data+dataId) +
            geomPoint( color = "blue"
                , size = 4.0
            ) { x = "xvals"; y = "yvals" } +
            geomLine() { x = "xvals"; y = "yvals" }+
            geomPoint( color = "Red"
                , size = 2.0
            ) { x = "xivals"; y = "yivals" } +
            geomLine() { x = "xivals"; y = "yivals" }
    ggsave(fig_2, "exp+id_sqrt.png")

    // График экспериментальный для 1/sqrt(x)
    val fig_3 = letsPlot(data) +
            geomPoint( color = "blue"
                , size = 4.0
            ) { x = "xvals"; y = "yvals" } +
            geomLine() { x = "xvals"; y = "yvals" }
    ggsave(fig_3, "exp_sqrt.png")

    // это мы отдельно строили отношение теоретического графика к обчному
    // Мы надеялись что у нас графики отличаются на константу но оказалось что первые точки *окахзалось только первая*
    // плохо соответствуют идеальной зависимости
    // это связано с тем, что на самом деле в нуле -- бесконечность, а то значение которое мы получаем для 0-ой корзинки
    // Соответствует какой-то промежуточной точке от 0 до bucketStep, мы берем 0,5 но на самом деле фиг знает
    // чему она там соответсвует
    // в процесее этот кусок кода отвалился за ненадобностью
    /*
    val fig_3 = letsPlot(data_dist) +
            geomPoint( color = "green"
                , size = 1.0
            ) { x = "xivals"; y = "yivals" } +
            geomLine() { x = "xivals"; y = "yivals" }

    ggsave(fig_3, "plot_2.png")
    */
    println("bye!")
}
