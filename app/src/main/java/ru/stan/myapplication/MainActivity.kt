package ru.stan.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel : $quizViewModel")

        binding.cheatButton.setOnClickListener {
            if (quizViewModel.remainingTokens > 0){
                quizViewModel.remainingTokens --
                updateTokenDisplay()
            }
            if (quizViewModel.remainingTokens == 0){
                binding.cheatButton.isEnabled = false
            }

            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        binding.trueButton.setOnClickListener {
            quizViewModel.checkAnswer(true, this)
            buttonsAreGone()
        }

        binding.falseButton.setOnClickListener {
            quizViewModel.checkAnswer(false, this)
            buttonsAreGone()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            quizViewModel.isCheater = false
            buttonsAreVisible()
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

        binding.buttonApiLevel.setOnClickListener {
            apiLevel()
        }
        binding.levelTextView?.setOnClickListener {
            apiLevel()
        }

        updateQuestion()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }
        if (quizViewModel.remainingTokens == 0){
            binding.cheatButton.isEnabled = false
        }
        if (savedInstanceState != null){
            binding.trueButton.isEnabled = savedInstanceState.getBoolean("trueButtonState")
        }

        if (savedInstanceState != null){
            binding.falseButton.isEnabled = savedInstanceState.getBoolean("falseButtonState")
        }
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

    private fun buttonsAreGone() {
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
    }

    private fun buttonsAreVisible() {
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }

    @SuppressLint("SetTextI18n")
    private fun apiLevel() {
        val apiLevel = Build.VERSION.SDK_INT
        val apiLevelMessage = "Уровень API телефона: $apiLevel"
        Toast.makeText(this, apiLevelMessage, Toast.LENGTH_SHORT).show()
        binding.levelTextView?.setText("Уровень API телефона: $apiLevel")
    }

    private fun updateTokenDisplay(){
        binding.cheatButton.text = "Show Answer (${quizViewModel.remainingTokens} left)"
    }
}