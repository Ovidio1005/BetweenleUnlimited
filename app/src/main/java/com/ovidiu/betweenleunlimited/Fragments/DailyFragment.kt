package com.ovidiu.betweenleunlimited.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ovidiu.betweenleunlimited.Components.BoxedCharacters
import com.ovidiu.betweenleunlimited.Components.Key
import com.ovidiu.betweenleunlimited.Components.StatsView
import com.ovidiu.betweenleunlimited.R
import com.ovidiu.betweenleunlimited.Utils.PreferenceHelper
import com.ovidiu.betweenleunlimited.Utils.WordList
import java.util.Calendar

class DailyFragment : GameFragment() {
    val date = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.findViewById<Button>(R.id.btnGiveUp).visibility = View.GONE
    }

    override fun startGame(){
        with(PreferenceHelper) {
            val yesterday = date.clone() as Calendar
            yesterday.add(Calendar.DAY_OF_MONTH, -1)

            // Player loses streak if they skip a day
            if(dailyDate == null || dailyDate!! < formatCalendar(yesterday)) dailyCurrentStreak = 0

            if (dailySecretWord != null && dailyDate == formatCalendar(
                    date
                )
            ) loadGame()
            else {
                secretWord = WordList.dailyWord()
                earlyWord = "AAAAA"
                lateWord = "ZZZZZ"

                rangeIndicator.secretFraction = WordList.getPositionFraction(secretWord)
            }
        }

        keyboard.setOnKeyPressListener { key ->
            if(key == Key.ENTER) handleEnter()
            else if(key == Key.BACKSPACE) handleBackspace()
            else handleKey(key)

            setAlphabet()
        }

        Log.i("GameFragment", "Secret word: ${secretWord.uppercase()}")
    }

    override fun loadGame(){
        secretWord = PreferenceHelper.dailySecretWord!!
        earlyWord = PreferenceHelper.dailyTopWord ?: "AAAAA"
        topWordView.text = earlyWord
        lateWord = PreferenceHelper.dailyBottomWord ?: "ZZZZZ"
        bottomWordView.text = lateWord

        rangeIndicator.secretFraction = WordList.getPositionFraction(secretWord)
        rangeIndicator.topFraction = WordList.getPositionFraction(earlyWord)
        rangeIndicator.bottomFraction = WordList.getPositionFraction(lateWord)

        score.currentGuess = PreferenceHelper.dailyCurrentGuess!!

        if(PreferenceHelper.dailyResult > 0){
            centerWordView.text = secretWord
            centerWordView.boxType = BoxedCharacters.GREEN
            keyboard.setOnKeyPressListener(null)

            showEndDialog(true)
        } else if(PreferenceHelper.dailyResult == 0){
            centerWordView.visibility = View.INVISIBLE
            keyboard.setOnKeyPressListener(null)

            showEndDialog(false)
        }

        setAlphabet()
    }

    override fun saveGame(){
        with(PreferenceHelper) {
            dailySecretWord = secretWord
            dailyTopWord = earlyWord
            dailyBottomWord = lateWord
            dailyCurrentGuess = score.currentGuess
            dailyDate = formatCalendar(date)
            dailyResult = -1
        }
    }

    override fun win(){
        centerWordView.boxType = BoxedCharacters.GREEN
        keyboard.setOnKeyPressListener(null)

        with(PreferenceHelper){
            dailyCurrentStreak++ // also sets best streak if appropriate

            when(score.score){
                5 -> daily5point++
                4 -> daily4point++
                3 -> daily3point++
                2 -> daily2point++
                1 -> daily1point++
                else -> Log.wtf("GameFragment", "Called win() with score=${score.score}")
            }

            dailyResult = score.score
        }

        showEndDialog(true)
    }

    override fun lose(){
        centerWordView.visibility = View.INVISIBLE
        keyboard.setOnKeyPressListener(null)

        with(PreferenceHelper){
            dailyCurrentStreak = 0

            if(score.score == 0) dailyLosses++
            else Log.wtf("GameFragment", "Called lose() in daily with score=${score.score}")

            dailyResult = 0
        }

        showEndDialog(false)
    }

    override fun showEndDialog(isWin : Boolean){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_end)
            .show()

        dialog.findViewById<StatsView>(R.id.stats)?.type = StatsView.DAILY
        dialog.findViewById<Button>(R.id.btnClose)?.setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnHome)?.setOnClickListener { dialog.dismiss(); findNavController().navigateUp() }

        dialog.findViewById<TextView>(R.id.tvTitle)?.text = if(isWin) "WIN" else secretWord.uppercase()
        dialog.findViewById<TextView>(R.id.tvResult)?.text = if(isWin) "${score.score} / 5" else "LOSS"
    }

    private fun formatCalendar(c : Calendar) : String {
        return String.format("%04d%02d%02d", c[Calendar.YEAR], c[Calendar.MONTH] + 1, c[Calendar.DAY_OF_MONTH])
    }
}