package com.example.madlevel2task2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madlevel2task2.ui.theme.MadLevel2Task2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadLevel2Task2Theme  {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TruthTableScreen()
                }
            }
        }
    }
}


data class Equation(
    val firstArg: String = "-",
    val secondArg: String = "-",
    val answer: String = "?"
)

@Composable
fun TruthTableScreen() {
    val equation = remember { mutableStateOf(Equation()) }

    val fabOnClick = {
        equation.value = Equation(randomTruthOrFalse(), randomTruthOrFalse(), randomTruthOrFalse())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        content = { padding ->
            ScreenContent(Modifier.padding(padding), equation)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.next)) },
                onClick = {
                    fabOnClick()
                },
                icon = { Icon(Icons.Filled.ArrowForward, "") }
            )
        }
    )
}

@Composable
private fun ScreenContent(
    modifier: Modifier,
    equation: MutableState<Equation>
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val checkAnswer = { givenAnswer: Boolean ->
            val message = if (verifyAnswer(equation) == givenAnswer) {
                R.string.correct_answer
            } else {
                R.string.wrong_answer
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        Text(
            text = stringResource(R.string.header),
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .wrapContentHeight()
                .padding(bottom = 32.dp, top = 48.dp),
        )

        EquationHeaders()
        EquationValues(equation)
        AnswerButtons(equation, checkAnswer)
    }
}

@Composable
private fun EquationHeaders() {
    //can't make reusable composable for Text because of the weight attribute
    Row(Modifier.padding(bottom = 32.dp)) {
        Text(
            text = stringResource(R.string.equation_a),
            style = MaterialTheme.typography.subtitle1,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.equation_b),
            style = MaterialTheme.typography.subtitle1,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.equation_ab),
            style = MaterialTheme.typography.subtitle1,
            fontSize = 24.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EquationValues(equation: MutableState<Equation>) {
    //can't make reusable composable for Text because of the weight attribute
    Row(Modifier.padding(bottom = 40.dp)) {
        Text(
            text = equation.value.firstArg,
            color = Color.Gray,
            style = MaterialTheme.typography.subtitle1,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = equation.value.secondArg,
            color = Color.Gray,
            style = MaterialTheme.typography.subtitle1,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = equation.value.answer,
            color = Color.Gray,
            style = MaterialTheme.typography.subtitle1,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AnswerButtons(equation: MutableState<Equation>, checkAnswer: (Boolean) -> Unit) {
    //show buttons only when user has clicked for the first time
    if (equation.value.answer != "?") {
        Row {
            AnswerButton(
                text = stringResource(id = R.string.is_true),
                checkAnswer = checkAnswer,
                answer = true
            )
            Spacer(Modifier.width(16.dp))
            AnswerButton(
                text = stringResource(id = R.string.is_false),
                checkAnswer = checkAnswer,
                answer = false
            )
        }
    }
}

@Composable
private fun AnswerButton(text: String, checkAnswer: (Boolean) -> Unit, answer: Boolean) {
    val backgroundColor = if (answer) Color(0xff4caf50) else Color(0xffe64a19)
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        onClick = { checkAnswer(answer) },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(100.dp),
    ) {
        Text(text = text)
    }
}


private fun verifyAnswer(equation: MutableState<Equation>): Boolean {
    return ((equation.value.firstArg == "T" && equation.value.secondArg == "T" && equation.value.answer == "T") ||
            (equation.value.firstArg == "T" && equation.value.secondArg == "F" && equation.value.answer == "F") ||
            (equation.value.firstArg == "F" && equation.value.secondArg == "T" && equation.value.answer == "F") ||
            (equation.value.firstArg == "F" && equation.value.secondArg == "F" && equation.value.answer == "F"))
}

private fun randomTruthOrFalse(): String {
    val trueOrFalse = listOf("T", "F")
    return trueOrFalse[(0..1).random()]
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MadLevel2Task2Theme() {
        TruthTableScreen()
    }
}