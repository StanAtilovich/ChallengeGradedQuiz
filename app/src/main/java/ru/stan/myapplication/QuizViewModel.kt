package ru.stan.myapplication

import android.content.Context
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val questionBank = listOf(
        Questions(R.string.question_australia, true, null),
        Questions(R.string.question_oceans, true, null),
        Questions(R.string.question_mideast, false, null),
        Questions(R.string.question_africa, false, null),
        Questions(R.string.question_americas, true, null),
        Questions(R.string.question_asia, true, null)
    )


    private var currentIndex: Int = 0


    private var currentScore = 0
    private var currentAnswers = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textRes

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
        if (currentIndex == 0) {
            resetScore()
        }
    }

    fun moveToBack() {
        currentIndex = (currentIndex - 1) % questionBank.size
        if (currentIndex == 0) {
            resetScore()
        }
    }

    fun checkAnswer(userAnswer: Boolean, context: Context) {
        val correctAnswer = currentQuestionAnswer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()


        questionBank[currentIndex].userAnswer = userAnswer
        currentAnswers++
        if (userAnswer == correctAnswer) {
            currentScore++
        }
        showScore(context)
    }

    private fun showScore(context: Context) {
        if (currentAnswers == questionBank.size) {
            val score =
                currentScore.toFloat().div(currentAnswers).times(100).roundToInt()
            Toast.makeText(context, "Your score $score %", Toast.LENGTH_SHORT).show()
        }

    }

    private fun resetScore() {
        currentScore = 0
        currentAnswers = 0
    }


}

