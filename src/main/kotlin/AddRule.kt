// Правила добавления элементов

// может быть первым
const val first = 1
//  после  .
const val dot = 2
// после цифр
const val number = 3
//  после тригонометрии
const val tri = 4
// после Арк
const val arc = 5
// После корня
const val root = 6
// После констант
const val constant = 7
// После основных базовых операций
const val baseMath = 8
// После основных базовых операций
const val rightParenthesis = 9


interface NumberWithRules : CalcNumber {
    override val ruleItems: Set<Int>
        get() = setOf(number)
    override val rulesAfter: Set<Int>
        get() = setOf(first, dot, tri, baseMath, root, number)
}

interface ConstNumberWithRules : CalcNumber {
    override val ruleItems: Set<Int>
        get() = setOf(constant)
    override val rulesAfter: Set<Int>
        get() = setOf(first, tri, baseMath, root)
}

interface BaseMathWithRules : CalcMathWith2PSimple {
    override val ruleItems: Set<Int>
        get() = setOf(baseMath)
    override val rulesAfter: Set<Int>
        get() = setOf(number, constant, rightParenthesis)
}

interface TrigonometryWithRules : CalcMathWith1PAfter {
    override val ruleItems: Set<Int>
        get() = setOf(tri)
    override val rulesAfter: Set<Int>
        get() = setOf(first, baseMath, arc)
}
