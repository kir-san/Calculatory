// функция нахождения результата
fun List<CalcAction>.calculate(): List<CalcAction> {
    var tempList = this.toMutableList()

    // вспомогательная функция нахождения предыдущего действия
    fun prevAction(i: Int): CalcAction {
        return if (i - 1 < 0) { // проверка на выход за границы списка
            Numbers.ZERO
        } else {
            tempList[i - 1]
        }
    }

    // вспомогательная функция нахождения следующего действия
    fun nextAction(i: Int, offset: Int): CalcAction? {
        return if (i + offset < tempList.size) { // проверка на выход за границы списка
            tempList[i + offset]
        } else {
            null
        }
    }

    // нахождение результата и замена вычисляемого выражения на результат
    fun CalcMathWith2P.simpleCalculate(i: Int, prevNumber: CalcNumber, nextNumber: CalcNumber) {
        val newNumber = calculate(prevNumber, nextNumber)
        tempList[i] = newNumber
        tempList.removeAt(i + 1)
        if (i - 1 >= 0)
            tempList.removeAt(i - 1)
    }

    // нахождение результата и замена вычисляемого выражения на результат
    fun CalcMathWith1P.simpleCalculate(i: Int, number: CalcNumber) {
        val newNumber = calculate(number)
        tempList[i] = newNumber
        tempList.removeAt(i + 1)
    }

    while (tempList.size > 1) {
        // находим какой самый высокий приоритет в выражении
        val filterIsInstance = tempList.filterIsInstance<CalcMath>()
        val maxOrder =
            if (filterIsInstance.size == 1) filterIsInstance.first().order
            else filterIsInstance.maxOf { action -> action.order }

        // Находим его место в списке
        val actionIndex = tempList.indexOfFirst { a -> a is CalcMath && a.order == maxOrder }
        // Находим самое действие
        val action = tempList[actionIndex]

        when (action) {
            // Если найдена левая скобка, то разделить список в этом месте и правую часть решать отдельно
            is Math.LeftParenthesis -> {
                if (tempList.size > actionIndex + 1) { // проверка, что после скобки есть еще значения
                    tempList.removeAt(actionIndex) // очищаем список от самой скобки
                    val subList = tempList.subList(actionIndex, tempList.size) // отделяем правую часть
                    tempList = tempList.subList(0, actionIndex) // оставляем только левую
                    tempList += subList.calculate() // результат добавляем к левой части
                } else break
            }

            // Если найдена правая скобка, то разделить список в этом месте и левую часть решить отдельно
            is Math.RightParenthesis -> {
                val leftPart = tempList.subList(0, actionIndex) // отделяем левую часть
                val rightPart = tempList.subList(actionIndex + 1, tempList.size) // отделяем правую
                val calculate = leftPart.calculate() // для левой части производим вычисления
                return calculate + rightPart // объединяем результат и правую часть
            }

            // для операций с двумя параметрами
            is CalcMathWith2P -> {
                // находим следующее значение, если его нет прекращаем вычисления
                val nextAction = nextAction(actionIndex, 1) ?: break
                // находим предыдущее
                val prevAction = prevAction(actionIndex)
                // Если оба значения являются цифрами, то находим результат
                if (prevAction is CalcNumber && nextAction is CalcNumber)
                    action.simpleCalculate(actionIndex, prevAction, nextAction)
                else break
            }
            // для операций с одним параметром
            is CalcMathWith1P -> {
                // находим следующее значение, если его нет прекращаем вычисления
                val nextAction = nextAction(actionIndex, 1) ?: break
                if (nextAction is CalcNumber) {
                    action.simpleCalculate(actionIndex, nextAction)
                // Если следущее число является знаком минус
                } else if (nextAction is Math.Minus) {
                    // то находим значение после знака минус
                    val nextNextNumber = nextAction(actionIndex, 2) ?: break
                    if (nextNextNumber is CalcNumber) {
                        // сперва находим противоположное число
                        val newNumber = nextAction.calculate(Numbers.ZERO, nextNextNumber)
                        tempList.removeAt(actionIndex + 2)
                        // затем находим результат операции
                        action.simpleCalculate(actionIndex, newNumber)
                    }
                } else break
            }
            else -> break
        }
    }

    return tempList
}