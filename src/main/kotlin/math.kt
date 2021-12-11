fun List<CalcAction>.calculate(): List<CalcAction> {
    var tempList = this.toMutableList()

    fun prevAction(i: Int): CalcAction {
        return if (i - 1 < 0) {
            Numbers.ZERO
        } else {
            tempList[i - 1]
        }
    }

    fun nextAction(i: Int, offset: Int): CalcAction? {
        return if (i + offset < tempList.size) {
            tempList[i + offset]
        } else {
            null
        }
    }

    fun CalcMathWith2P.simpleCalculate(i: Int, prevNumber: CalcNumber, nextNumber: CalcNumber) {
        val newNumber = calculate(prevNumber, nextNumber)
        tempList[i] = newNumber
        tempList.removeAt(i + 1)
        if (i - 1 >= 0)
            tempList.removeAt(i - 1)
    }

    fun CalcMathWith1PAfter.simpleCalculate(i: Int, number: CalcNumber) {
        val newNumber = calculate(number)
        tempList[i] = newNumber
        tempList.removeAt(i + 1)
    }

    fun CalcMathWith1PBefore.simpleCalculate(i: Int, number: CalcNumber) {
        val newNumber = calculate(number)
        tempList[i] = newNumber
        tempList.removeAt(i - 1)
    }

    while (tempList.size > 1) {

        // 1 находим первое с самых высоким приоритетом
        val filterIsInstance = tempList.filterIsInstance<CalcMath>()
        val maxOrder =
            if (filterIsInstance.size == 1) filterIsInstance.first().order
            else filterIsInstance.maxOf { action -> action.order }

        val actionIndex = tempList.indexOfFirst { a -> a is CalcMath && a.order == maxOrder }
        val action = tempList[actionIndex]
        // 2 извлекаем для него спереди стоящее значение и после
        val prevAction = prevAction(actionIndex)
        // 3 вычисляем и заменяем выражение числом
        when (action) {
            // Если найдена левая скобка, то разделить список в этом месте и правую часть решать отдельно
            is Math.LeftParenthesis -> {
                if (tempList.size > actionIndex + 1) {
                    tempList.removeAt(actionIndex)
                    val subList = tempList.subList(actionIndex, tempList.size)
                    tempList = tempList.subList(0, actionIndex)
                    tempList += subList.calculate()
                } else break
            }

            // Если найдена правая скобка, то разделить список в этом месте и левую часть решить отдельно
            is Math.RightParenthesis -> {
                tempList = tempList.subList(0, actionIndex).calculate().toMutableList()
            }

            is CalcMathWith2PSimple -> {
                val nextAction = nextAction(actionIndex, 1) ?: break
                // для простых операций
                if (prevAction is CalcNumber && nextAction is CalcNumber)
                    action.simpleCalculate(actionIndex, prevAction, nextAction)
            }
            // для сложных операций
            is CalcMathWith2PAdvance -> {
                val nextAction = nextAction(actionIndex, 1) ?: break
                if (prevAction is CalcNumber && nextAction is CalcNumber) {
                    action.simpleCalculate(actionIndex, prevAction, nextAction)
                } else if (prevAction is CalcNumber && nextAction is Math.Minus) {
                    val nextNextNumber = nextAction(actionIndex, 2) ?: break
                    if (nextNextNumber is CalcNumber) {
                        val newNumber = nextAction.calculate(Numbers.ZERO, nextNextNumber)
                        tempList.removeAt(actionIndex + 2)
                        action.simpleCalculate(actionIndex, prevAction, newNumber)
                    }
                }
            }
            // для однопараметровых с числом до
            is CalcMathWith1PBefore -> {
                if (prevAction is CalcNumber) {
                    action.simpleCalculate(actionIndex, prevAction)
                }
            }
            // для однопараметровых с числом после
            is CalcMathWith1PAfter -> {
                val nextAction = nextAction(actionIndex, 1) ?: break
                if (nextAction is CalcNumber) {
                    action.simpleCalculate(actionIndex, nextAction)
                } else if (nextAction is Math.Minus) {
                    val nextNextNumber = nextAction(actionIndex, 2) ?: break
                    if (nextNextNumber is CalcNumber) {
                        val newNumber = nextAction.calculate(Numbers.ZERO, nextNextNumber)
                        tempList.removeAt(actionIndex + 2)
                        action.simpleCalculate(actionIndex, newNumber)
                    }
                }
            }
            else -> break
        }
        // 4 возвращаемся к шагу 1
    }

    return tempList
}