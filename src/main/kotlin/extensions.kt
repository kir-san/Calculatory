import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type

fun KeyEvent.onPressKey(action: (CalcAction) -> Unit): Boolean {
    if (KeyEventType.KeyDown == type) {
        val result = Constants.calcButtons.filter { a ->
            a.key == key || a.key2 == key
        }

        if (result.isNotEmpty()) {
            action(result.first())
            return true
        }
        return false
    }
    return false
}

fun MutableList<CalcAction>.addAction(action: CalcAction) {
    when {
        // первым элементом может быть лишь ограниченное кол-во действий
        isEmpty() -> if (action.rulesAfter.contains(first)) add(action)
        // Удаление последнего элемента
        action is Extra.RemoveLast -> removeLast()
        // Очистка поля
        action is Extra.Clean -> clear()
        // обработка модификатора
        action is Extra.Square -> {
            addAction(Math.Pow)
            if (isNotEmpty() && last() is Math.Pow)
                addAction(Numbers.TWO)
        }
        // Преобразование корней
        (action is Math.Sqrt && last() is CalcNumber) -> {
            add(Math.Root)
        }

        // действия с модификатором Арк
        last().ruleItems.contains(arc) && action.rulesAfter.contains(arc) -> {
            removeLast()
            when (action) {
                is Math.Cos -> add(Math.ArcCos)
                is Math.Sin -> add(Math.ArcSin)
                is Math.Tan -> add(Math.ArcTan)
                is Math.Ctg -> add(Math.ArcCtg)
            }
        }

        // Добавление по правилам
        last().ruleItems.intersect(action.rulesAfter).isNotEmpty() -> add(action)
    }
}

fun List<CalcAction>.buildString(): String {
    var result = ""
    if (isNotEmpty())
        forEach { result += it.expressionText }
    return result.ifEmpty { "0" }
}

// Преобразование подряд идущих Number в одно
fun List<CalcAction>.transformNumbers(): List<CalcAction> {
    val newList = mutableListOf<CalcAction>()
    val zeroList = mutableListOf<CalcAction>()
    var newNumber = ""
    var isDecimal = false
    var isLastZero = false

    forEach { action ->
        when (action) {
            // Добавление чисел во временную строку
            is CalcNumber -> {
                if (action.ruleItems.contains(number)) {
                    newNumber += action.expressionText
                    // индикатор того были ли добавлены нули после точки
                    isLastZero = action is Numbers.ZERO && isDecimal
                }
                if (action.ruleItems.contains(constant)) {
                    newList += action
                }
            }
            is Extra.Decimal -> {
                // запоминаем была ли у нас точка для десятичных дробей
                if (!isDecimal) {
                    newNumber += action.expressionText
                    isDecimal = true
                }
            }
            else -> { // на все остальные действия
                // если временная строка не пустая
                if (newNumber.isNotEmpty()) {
                    // Если была точка, то это FLOAT, Иначе LONG
                    newList += newNumber.createNumber(isDecimal, false)

                    isDecimal = false
                    newNumber = ""
                } else if (isLastZero) {
                    newList += zeroList
                }
                newList += action
            }
        }
    }
    if (newNumber.isNotEmpty()) {
        newList += newNumber.createNumber(isDecimal, isLastZero)
    }
    return newList
}

private fun String.createNumber(isDecimal: Boolean, hasLastZero: Boolean): CalcNumber {
    return try {
        if (hasLastZero) {
            calcNumber(toFloat(), text = this)
        } else {
            if (this == ".") calcNumber(0F)
            else calcNumber(toFloat())
        }
    } catch (ex: NumberFormatException) {
        Errors.BigNumber
    }
}

inline fun CalcNumber.getResult(
    hasPi: Boolean = false,
    number: CalcNumber = Numbers.ZERO,
    action: (n1: Float, n2: Float) -> Float
): CalcNumber {
    val floatResult = action(value, number.value)
    val longResult = floatResult.toLong()

    return calcNumber(floatResult, hasPi)
}

fun Float.toStr(): String {
    return if (this == toLong().toFloat()) {
        toLong().toString()
    } else {
        toString()
    }
}