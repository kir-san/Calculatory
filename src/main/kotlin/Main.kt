// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@OptIn(ExperimentalFoundationApi::class)
fun main() = application {
    val state = rememberWindowState(size = Constants.windowSize)
    // список введенных действий
    val enteredActions = remember<SnapshotStateList<CalcAction>> { mutableStateListOf() }
    // веденные действия с преобразованием цифр
    val enteredExpression = enteredActions.transformNumbers()
    // текст отображаемый в поле ввывода
    val calculationResult = enteredExpression.calculate().buildString()
    // текст отображаемый в поле ввода
    val enteredText = enteredExpression.buildString()
    // хранение данных для журнала
    val history = remember<SnapshotStateList<Pair<String, String>>> { mutableStateListOf() }

    // вспомогательная функция обработки ввода данных
    fun addAction(action: CalcAction) {
        // Сохранения результата при нажатии на кнопку "="
        if (action is Extra.Equals) {
            if (calculationResult != Constants.defaultValue || enteredText != Constants.defaultValue)
                history.add(calculationResult to enteredText)
            enteredActions.addAction(Extra.Clean)
        } else {
            enteredActions.addAction(action)
        }
    }

    // Окно приложения
    Window(
        state = state,
        title = "Calculator",
        resizable = false,
        onCloseRequest = ::exitApplication,
        onKeyEvent = { event -> event.onPressKey(::addAction) }
    ) {

        MaterialTheme {
            Row(
                modifier = Modifier
                    .background(Constants.windowColor)
                    .padding(Constants.mainPadding)
            ) {
                // Основное место калькулятора
                Column(
                    modifier = Modifier
                        .padding(Constants.mainPadding)
                        .width(Constants.calcWidth)
                ) {
                    Text( // поле вывода информации
                        modifier = Modifier.fillMaxWidth().padding(vertical = Constants.bigTextVertPad),
                        maxLines = 1,
                        fontSize = Constants.resultTextSize,
                        textAlign = TextAlign.End,
                        text = calculationResult,
                        color = Constants.textColor
                    )
                    Text( // поле ввода информации
                        modifier = Modifier.fillMaxWidth().padding(vertical = Constants.smallTextVerPad),
                        maxLines = 1,
                        fontSize = Constants.expressionTextSize,
                        textAlign = TextAlign.End,
                        text = enteredText,
                        color = Constants.textColor
                    )

                    // Сетка из кнопок
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(Constants.buttonCols),
                    ) {
                        items(Constants.calcButtons) { action ->
                            Button(
                                modifier = Modifier
                                    .padding(Constants.buttonPadding)
                                    .size(Constants.buttonWidth, Constants.buttonHeight),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Constants.btnColor,
                                    contentColor = Constants.textColor
                                ),
                                shape = RoundedCornerShape(0),
                                onClick = { addAction(action) }) {
                                Text(
                                    action.buttonText,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }

                    Text(
                        text = "Автор приложения Деревяновский К.В.",
                        modifier = Modifier.fillMaxWidth().padding(Constants.aboutHeight / 2 - 8.dp),
                        textAlign = TextAlign.Center,
                        color = Constants.textColor
                    )
                }

                // Журнал действий
                Column(
                    modifier = Modifier
                        .width(Constants.historyWidth)
                        .height(Constants.windowSize.height)
                        .padding(7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( // Заголовок
                        "Журнал",
                        fontSize = 20.sp,
                        color = Constants.textColor
                    )
                    LazyColumn( // список
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(history) { (calc, enter) ->
                            Column(
                                modifier = Modifier.padding(bottom = 6.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    "$enter =",
                                    fontSize = 13.sp,
                                    color = Constants.textColor
                                )
                                Text(
                                    calc,
                                    fontSize = 20.sp,
                                    color = Constants.textColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
