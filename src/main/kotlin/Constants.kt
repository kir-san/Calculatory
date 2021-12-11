import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

object Constants {
    val buttonHeight = 50.dp
    val buttonWidth = 80.dp

    val historyWidth = 250.dp

    val buttonPadding = 1.dp
    val mainPadding = 3.dp
    val bigTextVertPad = 9.dp
    val smallTextVerPad = 4.dp

    val windowColor = Color(0xFF272727)
    val textColor = Color.White
    val btnColor = Color.Black

    val resultTextSize = 40.sp
    val expressionTextSize = 17.sp

    const val defaultValue = "0"

    // Макет расположения кнопок
    @OptIn(ExperimentalComposeUiApi::class)
    val calcButtons = listOf(
//        Cos  Pi   (     )   C   <-
        Math.Cos, Numbers.PI, Math.LeftParenthesis, Math.RightParenthesis, Extra.Clean, Extra.RemoveLast,
//        Sin    e   7   8   9   /
        Math.Sin, Numbers.E, Numbers.SEVEN, Numbers.EIGHT, Numbers.NINE, Math.Div,
//        tan    √    4   5   6   x
        Math.Tan, Math.Sqrt, Numbers.FOUR, Numbers.FIVE, Numbers.SIX, Math.Times,
//        ctg    ^2   1   2   3   -
        Math.Ctg, Extra.Square, Numbers.ONE, Numbers.TWO, Numbers.THREE, Math.Minus,
//        arc     ^   .   0   =   +
        Extra.Arc, Math.Pow, Extra.Decimal, Numbers.ZERO, Extra.Equals, Math.Plus
    )
    const val buttonCols = 6
    private const val buttonRows = 5
    private val textSize = 101.dp + bigTextVertPad * 2 + smallTextVerPad * 2

    val calcWidth = buttonWidth * buttonCols + buttonPadding * (buttonCols - 1) + mainPadding * 2
    private val width = calcWidth + historyWidth
    private val height = buttonHeight * buttonRows + buttonPadding * (buttonRows - 1) + textSize + mainPadding * 2

    val windowSize = DpSize(width = width, height = height)
}

@OptIn(ExperimentalComposeUiApi::class)
object Numbers {
    object ZERO : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 0F
        override val key = Key.Zero
        override val key2 = Key.NumPad0
    }

    object ONE : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 1F
        override val key = Key.One
        override val key2 = Key.NumPad1
    }

    object TWO : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 2F
        override val key = Key.Two
        override val key2 = Key.NumPad2
    }

    object THREE : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 3F
        override val key = Key.Three
        override val key2 = Key.NumPad3
    }

    object FOUR : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 4F
        override val key = Key.Four
        override val key2 = Key.NumPad4
    }

    object FIVE : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 5F
        override val key = Key.Five
        override val key2 = Key.NumPad5
    }

    object SIX : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 6F
        override val key = Key.Six
        override val key2 = Key.NumPad6
    }

    object SEVEN : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 7F
        override val key = Key.Seven
        override val key2 = Key.NumPad7
    }

    object EIGHT : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 8F
        override val key = Key.Eight
        override val key2 = Key.NumPad8
    }

    object NINE : CalcNumber, AddRuleAfterFirst, AddRuleNumber, AddRuleAfterDot, AddRuleAfterTri, AddRuleAfterBaseMath,
        AddRuleAfterRoot {
        override val value = 9F
        override val key = Key.Nine
        override val key2 = Key.NumPad9
    }

    object PI : CalcNumber, AddRuleAfterFirst, AddRuleAfterTri, AddRuleAfterBaseMath, AddRuleAfterRoot, AddRuleConst {
        override val value = kotlin.math.PI.toFloat()
        override val buttonText = "Pi"
        override val expressionText = "Pi"
        override val hasPi = true
    }

    object E : CalcNumber, AddRuleAfterFirst, AddRuleAfterTri, AddRuleAfterBaseMath, AddRuleAfterRoot, AddRuleConst {
        override val value = kotlin.math.E.toFloat()
        override val buttonText = "E"
        override val expressionText = "E"
        override val hasPi = false
    }
}

@OptIn(ExperimentalComposeUiApi::class)
object Math {
    object Div : CalcMathWith2PSimple, AddRuleAfterNumber, AddRuleAfterConst, AddRuleBaseMath,
        AddRuleAfterRightParenthesis {
        override val buttonText = "/"
        override val expressionText = " $buttonText "
        override val key = Key.Slash
        override val key2 = Key.NumPadDivide
        override val order = 2

        override fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
            if (number2.value == 0f) {
                return Errors.DivideZero
            }

            val hasPi = number1.hasPi || number2.hasPi

            return number1.getResult(hasPi, number2) { n1, n2 -> n1 / n2 }
        }
    }

    object Plus : CalcMathWith2PSimple, AddRuleAfterNumber, AddRuleAfterConst, AddRuleBaseMath,
        AddRuleAfterRightParenthesis {
        override val buttonText = "+"
        override val expressionText = " $buttonText "
        override val key = Key.Plus
        override val key2 = Key.NumPadAdd
        override val order = 1

        override fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
            val hasPi = number1.hasPi || number2.hasPi
            return number1.getResult(hasPi, number2) { n1, n2 -> n1 + n2 }
        }
    }

    object Minus : CalcMathWith2PSimple, AddRuleAfterFirst, AddRuleAfterNumber,
        AddRuleAfterRoot, AddRuleAfterConst, AddRuleBaseMath, AddRuleAfterRightParenthesis {
        override val buttonText = "-"
        override val expressionText = " $buttonText "
        override val key = Key.Minus
        override val key2 = Key.NumPadSubtract
        override val order = 1

        override fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
            val hasPi = number1.hasPi || number2.hasPi
            return number1.getResult(hasPi, number2) { n1, n2 -> n1 - n2 }
        }
    }

    object Times : CalcMathWith2PSimple, AddRuleAfterNumber, AddRuleAfterConst, AddRuleBaseMath,
        AddRuleAfterRightParenthesis {
        override val buttonText = "x"
        override val expressionText = " $buttonText "
        override val key = Key.Multiply
        override val key2 = Key.NumPadMultiply
        override val order = 2

        override fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
            val hasPi = number1.hasPi || number2.hasPi
            return number1.getResult(hasPi, number2) { n1, n2 -> n1 * n2 }
        }
    }

    object Pow : CalcMathWith2PAdvance, AddRuleAfterNumber, AddRuleAfterConst, AddRuleBaseMath,
        AddRuleAfterRightParenthesis {
        override val buttonText = "x^y"
        override val expressionText = "^"
        override val key = Key.P
        override val order = 3

        override fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
            val hasPi = number1.hasPi || number2.hasPi
            return number1.getResult(hasPi, number2) { n1, n2 -> n1.pow(n2) }
        }
    }

    object Root : CalcMathWith2PAdvance, AddRuleAfterNumber, AddRuleRoot, AddRuleAfterConst {
        override val buttonText = "y√x"
        override val expressionText = "√"
        override val key = Key.R
        override val order = 3

        override fun calculate(number1: CalcNumber, number2: CalcNumber): CalcNumber {
            if (number2.value < 0F) {
                return Errors.RootMinus
            }

            val hasPi = number1.hasPi || number2.hasPi

            if (number1.value == 0f || number1.value == 2f) {
                return number1.getResult(hasPi, number2) { _, n2 -> sqrt(n2) }
            }

            return number1.getResult(hasPi, number2) { n1, n2 -> n2.pow(1 / n1) }
        }
    }

    object Sqrt : CalcMathWith1PAfter, AddRuleAfterFirst, AddRuleAfterNumber, AddRuleRoot, AddRuleAfterBaseMath {
        override val buttonText = "√x"
        override val expressionText = "√"
        override val key = Key.S
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            return Root.calculate(Numbers.TWO, number)
        }
    }

    object Cos : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "cos"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            if (number.hasPi) {
                return number.getResult(false) { n1, _ -> cos(n1) }
            }
            return number.getResult(false) { n1, _ ->
                cos(n1 * PI.toFloat() / 180f)
            }
        }
    }

    object Sin : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "sin"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            if (number.hasPi) {
                return number.getResult(false) { n1, _ -> sin(n1) }
            }
            return number.getResult(false) { n1, _ ->
                sin(n1 * PI.toFloat() / 180f)
            }
        }
    }

    object Tan : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "tan"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            if (number.hasPi) {
                return number.getResult(false) { n1, _ -> tan(n1) }
            }

            val odd = (number.value.toFloat() / 90) % 2
            if (odd == 1f) {
                return Errors.WrongTan
            }
            return number.getResult(false) { n1, _ ->
                tan(n1 * PI.toFloat() / 180f)
            }
        }
    }

    object Ctg : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "ctg"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            if (number.hasPi) {
                return number.getResult(false) { n1, _ -> tan(n1) }
            }

            val odd = (number.value.toFloat() / 180)
            if (odd == odd.toLong().toFloat()) {
                return Errors.WrongTan
            }
            return number.getResult(false) { n1, _ ->
                1f / tan(n1 * PI.toFloat() / 180f)
            }
        }
    }

    object ArcCos : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "arccos"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            if (number.value.toFloat() !in -1f..1f)
                return Errors.BigNumber

            return number.getResult(false) { n1, _ ->
                acos(n1) * 180 / PI.toFloat()
            }
        }
    }

    object ArcSin : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "arcsin"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            if (number.value.toFloat() !in -1f..1f)
                return Errors.BigNumber

            return number.getResult(false) { n1, _ ->
                asin(n1) * 180 / PI.toFloat()
            }
        }
    }

    object ArcTan : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "arctan"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            return number.getResult(false) { n1, _ ->
                atan(n1) * 180 / PI.toFloat()
            }
        }
    }

    object ArcCtg : CalcMathWith1PAfter, AddRuleTri, AddRuleAfterBaseMath, AddRuleAfterModArc, AddRuleAfterRoot {
        override val buttonText = "arcctg"
        override val expressionText = buttonText
        override val order = 4

        override fun calculate(number: CalcNumber): CalcNumber {
            return number.getResult(false) { n1, _ ->
                atan(1 / n1) * 180 / PI.toFloat()
            }
        }
    }

    object LeftParenthesis : CalcMath, AddRuleAfterFirst, AddRuleFirst, AddRuleAfterRoot, AddRuleAfterBaseMath {
        override val buttonText = "("
        override val expressionText = buttonText
        override val key2 = Key.NumPadLeftParenthesis
        override val order = 5
    }

    object RightParenthesis : CalcMath, AddRuleAfterNumber, AddRuleAfterConst, AddRuleRightParenthesis,
        AddRuleAfterRightParenthesis {
        override val buttonText = ")"
        override val expressionText = buttonText
        override val key2 = Key.NumPadRightParenthesis
        override val order = 5
    }
}

@OptIn(ExperimentalComposeUiApi::class)
object Extra {
    object Clean : CalcAction {
        override val buttonText: String = "C"
        override val key: Key = Key.Delete
    }

    object RemoveLast : CalcAction {
        override val buttonText: String = "<-"
        override val key: Key = Key.Backspace
    }

    object Equals : CalcAction {
        override val buttonText: String = "="
        override val key: Key = Key.Enter
        override val key2: Key = Key.NumPadEnter
    }

    // Модификатор в дробное число
    object Decimal : CalcAction, AddRuleAfterFirst, AddRuleDot, AddRuleAfterNumber, AddRuleAfterRoot,
        AddRuleAfterBaseMath {
        override val buttonText: String = "."
        override val key: Key = Key.NumPadDot
        override val key2: Key = Key.Period
    }

    object Square : CalcAction, AddRuleAfterNumber {
        override val buttonText = "^2"
        override val expressionText = "$buttonText "
        override val key = Key.Q
    }

    object Arc : CalcAction, AddRuleModArc, AddRuleAfterFirst, AddRuleAfterRoot, AddRuleAfterBaseMath {
        override val buttonText = "arc"
        override val expressionText = buttonText
    }
}

object Errors {
    object DivideZero : CalcNumber, CalcError {
        override val value = 0f
        override val buttonText = "Не делить на 0"
    }

    object RootMinus : CalcNumber, CalcError {
        override val value = 0f
        override val buttonText: String = "Нельзя извлечь"
    }

    object BigNumber : CalcNumber, CalcError {
        override val value = 0f
        override val buttonText: String = "Слишком большое число"
    }

    object WrongTan : CalcNumber, CalcError {
        override val value = 0f
        override val buttonText: String = "Бесконечное число"
    }
}