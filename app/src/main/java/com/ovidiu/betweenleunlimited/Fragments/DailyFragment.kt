package com.ovidiu.betweenleunlimited.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DailyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DailyFragment : GameFragment() {
    // TODO: prevent player from retrying daily (resume from last guess / immediately show end screen)
    override fun startGame(){
        secretWord = WordList.dailyWord()
        earlyWord = "AAAAA"
        lateWord = "ZZZZZ"

        rangeIndicator.secretFraction = WordList.getPositionFraction(secretWord)

        keyboard.setOnKeyPressListener { key ->
            if(key == Key.ENTER) handleEnter()
            else if(key == Key.BACKSPACE) handleBackspace()
            else handleKey(key)

            setAlphabet()
        }

        Log.i("GameFragment", "Secret word: ${secretWord.uppercase()}")
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
        }

        showEndDialog()
    }

    override fun lose(){
        centerWordView.visibility = View.INVISIBLE
        keyboard.setOnKeyPressListener(null)

        with(PreferenceHelper){
            dailyCurrentStreak = 0

            if(score.score == 0) dailyLosses++
            else Log.wtf("GameFragment", "Called lose() with score=${score.score}")
        }

        showEndDialog()
    }

    override fun showEndDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_end)
            .show()

        dialog.findViewById<StatsView>(R.id.stats)?.type = StatsView.DAILY
        dialog.findViewById<Button>(R.id.btnClose)?.setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnHome)?.setOnClickListener { dialog.dismiss(); findNavController().navigateUp() }

        dialog.findViewById<TextView>(R.id.tvTitle)?.text = if(score.score > 0) "WIN" else secretWord.uppercase()
        dialog.findViewById<TextView>(R.id.tvResult)?.text = if(score.score > 0) "${score.score} / 5" else "LOSS"
    }
}