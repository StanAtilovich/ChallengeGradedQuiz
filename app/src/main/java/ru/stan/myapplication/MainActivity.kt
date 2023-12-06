package ru.stan.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.stan.myapplication.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel : $quizViewModel")

        binding.trueButton.setOnClickListener {
            quizViewModel.checkAnswer(true, this)
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        }
        binding.falseButton.setOnClickListener {
            quizViewModel.checkAnswer(false, this)
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true

        }
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()

        }

        binding.backButton.setOnClickListener {
            quizViewModel.moveToBack()
            updateQuestion()
            updateBackButtonVisibility()
        }

        updateQuestion()

    }


    private fun updateQuestion() {
        val questionsTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionsTextResId)
        updateBackButtonVisibility()
    }

    private fun updateBackButtonVisibility() {
        if (quizViewModel.currentIndex == 0) {
            binding.backButton.visibility = View.INVISIBLE
        } else {
            binding.backButton.visibility = View.VISIBLE
        }
    }
}


