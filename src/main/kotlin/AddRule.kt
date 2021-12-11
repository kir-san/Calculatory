// Правила добавления элементов

interface AddRule
interface AddRuleAfter

sealed interface AddRuleItem

val AddRuleItem.getValue: Int
    get() = when (this) {
        is Dot -> 1
        is First -> 2
        is ModArc -> 3
        is Number -> 4
        is Root -> 5
        is Tri -> 6
        is Const -> 7
        is BaseMath -> 8
        is RightParenthesis -> 9
    }

// может быть первым
interface AddRuleAfterFirst : AddRuleAfter, First
interface AddRuleFirst : AddRule, First
interface First : AddRuleItem

//  после  .
interface AddRuleAfterDot : AddRuleAfter, Dot
interface AddRuleDot : AddRule, Dot
interface Dot : AddRuleItem

// после цифр
interface AddRuleAfterNumber : AddRuleAfter, Number
interface AddRuleNumber : AddRule, Number
interface Number : AddRuleItem

//  после тригонометрии
interface AddRuleAfterTri : AddRuleAfter, Tri
interface AddRuleTri : AddRule, Tri
interface Tri : AddRuleItem

// после Арк
interface AddRuleAfterModArc : AddRuleAfter, ModArc
interface AddRuleModArc : AddRule, ModArc
interface ModArc : AddRuleItem

// После корня
interface AddRuleAfterRoot : AddRuleAfter, Root
interface AddRuleRoot : AddRule, Root
interface Root : AddRuleItem

// После констант
interface AddRuleAfterConst : AddRuleAfter, Const
interface AddRuleConst : AddRule, Const
interface Const : AddRuleItem

// После основных базовых операций
interface AddRuleAfterBaseMath : AddRuleAfter, BaseMath
interface AddRuleBaseMath : AddRule, BaseMath
interface BaseMath : AddRuleItem

// После основных базовых операций
interface AddRuleAfterRightParenthesis : AddRuleAfter, RightParenthesis
interface AddRuleRightParenthesis : AddRule, RightParenthesis
interface RightParenthesis : AddRuleItem
