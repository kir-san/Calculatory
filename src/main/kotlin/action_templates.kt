@file:OptIn(ExperimentalComposeUiApi::class)

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key

// Главный родитель всех действий
sealed interface CalcAction {
    val buttonText: String // текст отображаемый на кнопке
    val expressionText: String // текст отображаемы в выражении
        get() = buttonText
    val key: Key // определение кнопки на клавиатуре
        get() = Key.Unknown
    val key2: Key // дополнительное определение кнопки
        get() = Key.Unknown
    val ruleItems: Set<Int> // множество видов групп к которому принадлежит действие
        get() = setOf()
    val rulesAfter: Set<Int> // множество видов групп после которых может идти это действие
        get() = setOf()
}

interface CalcError // Индикатор ошибки

// Родитель всех цифр и констант
interface CalcNumber : CalcAction {
    val value: Float // значение цифры
    override val buttonText: String
        get() = value.toStr()
    val hasPi: Boolean // флаг взаимодействия с числом Пи
        get() = false
}

// функция простого создания цифры
fun calcNumber(float: Float, hasPi: Boolean = false, text: String = "") = object : CalcNumber {
    override val value = float
    override val hasPi = hasPi
    override val expressionText: String
        get() = text.ifEmpty { super.expressionText }
}

// Родитель всех математических операций
interface CalcMath : CalcAction {
    val order: Int // приоритет выполнения
}

// Родитель всех математических операций с двумя параметрами (до и после)
interface CalcMathWith2P : CalcMath {
    // Метод вычисляющий результат для операции
    fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
        return Numbers.ZERO
    }
}

// Математические операции с одним параметром
interface CalcMathWith1P : CalcMath {
    fun calculate(number: CalcNumber): CalcNumber {
        return Numbers.ZERO
    }
}