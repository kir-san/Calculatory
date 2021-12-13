// Правила добавления элементов

// Деление всех элементов калькулятора
// на группы с которыми работают правила
// 
// может быть первым
const val first = 1
// .
const val dot = 2
// цифры
const val number = 3
// тригонометрические операции
const val tri = 4
// модификатор Арк
const val arc = 5
// корень
const val root = 6
// константы
const val constant = 7
// арифметические операции
const val baseMath = 8
// закрывающая правия скобка
const val rightParenthesis = 9

// Подготовленные шаблоны для операций которые имеют повторения
// Шаблон с правилами для цифр
interface NumberWithRules : CalcNumber {
    override val ruleItems: Set<Int>
        get() = setOf(number)
    override val rulesAfter: Set<Int>
        get() = setOf(first, dot, tri, baseMath, root, number)
}

// Шаблон с правилами для констант
interface ConstNumberWithRules : CalcNumber {
    override val ruleItems: Set<Int>
        get() = setOf(constant)
    override val rulesAfter: Set<Int>
        get() = setOf(first, tri, baseMath, root)
}

// Шаблон с правилами для арфметических операций
interface BaseMathWithRules : CalcMathWith2P {
    override val ruleItems: Set<Int>
        get() = setOf(baseMath)
    override val rulesAfter: Set<Int>
        get() = setOf(number, constant, rightParenthesis)
}

// Шаблон с правилами для тригонометрических операций
interface TrigonometryWithRules : CalcMathWith1P {
    override val ruleItems: Set<Int>
        get() = setOf(tri)
    override val rulesAfter: Set<Int>
        get() = setOf(first, baseMath, arc)
}
