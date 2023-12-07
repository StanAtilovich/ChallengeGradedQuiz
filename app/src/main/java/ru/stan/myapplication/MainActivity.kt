package ru.stan.myapplication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.stan.myapplication.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel : $quizViewModel")
        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            // startActivity(intent)Challenge: Closing Loopholes for Cheaters
            cheatLauncher.launch(intent)
        }
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