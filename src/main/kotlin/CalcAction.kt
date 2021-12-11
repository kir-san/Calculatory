@file:OptIn(ExperimentalComposeUiApi::class)

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key

sealed interface CalcAction {
    val buttonText: String
    val expressionText: String
        get() = buttonText
    val key: Key
        get() = Key.Unknown
    val key2: Key
        get() = Key.Unknown
    val ruleItems: Set<Int>
        get() = setOf()
    val rulesAfter: Set<Int>
        get() = setOf()
}

interface CalcError // Индикатор ошибки

interface CalcNumber : CalcAction {
    val value: Float
    override val buttonText: String
        get() = value.toStr()
    val hasPi: Boolean
        get() = false
}

fun calcNumber(float: Float, hasPi: Boolean = false, text: String = "") = object : CalcNumber {
    override val value = float
    override val hasPi = hasPi
    override val expressionText: String
        get() = text.ifEmpty { super.expressionText }
}

interface CalcMath : CalcAction {
    val order: Int
}

interface CalcMathWith2P : CalcMath {
    fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
        return Numbers.ZERO
    }
}

interface CalcMathWith2PSimple : CalcMathWith2P
interface CalcMathWith2PAdvance : CalcMathWith2P

interface CalcMathWith1P : CalcMath {
    fun calculate(number: CalcNumber): CalcNumber {
        return Numbers.ZERO
    }
}

interface CalcMathWith1PBefore : CalcMathWith1P
interface CalcMathWith1PAfter : CalcMathWith1P
