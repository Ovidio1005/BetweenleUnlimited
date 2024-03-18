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
import com.google.android.material.snackbar.Snackbar
import com.ovidiu.betweenleunlimited.Components.Alphabet
import com.ovidiu.betweenleunlimited.Components.BoxedCharacters
import com.ovidiu.betweenleunlimited.Components.Key
import com.ovidiu.betweenleunlimited.Components.Keyboard
import com.ovidiu.betweenleunlimited.Components.RangeIndicator
import com.ovidiu.betweenleunlimited.Components.Score
import com.ovidiu.betweenleunlimited.Components.StatsView
import com.ovidiu.betweenleunlimited.R
import com.ovidiu.betweenleunlimited.Utils.PreferenceHelper
import com.ovidiu.betweenleunlimited.Utils.WordList

open class GameFragment : Fragment() {
    lateinit var rootView : View

    lateinit var score : Score
    lateinit var keyboard : Keyboard
    lateinit var alphabet: Alphabet
    lateinit var rangeIndicator: RangeIndicator
    lateinit var topWordView : BoxedCharacters
    lateinit var centerWordView : BoxedCharacters
    lateinit var bottomWordView : BoxedCharacters

    lateinit var secretWord : String
    lateinit var earlyWord : String
    lateinit var lateWord : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view

        score = rootView.findViewById(R.id.score)
        keyboard = rootView.findViewById(R.id.keyboard)
        alphabet = rootView.findViewById(R.id.alphabet)
        rangeIndicator = rootView.findViewById(R.id.rangeIndicator)
        topWordView = rootView.findViewById(R.id.tvEarlyWord)
        centerWordView = rootView.findViewById(R.id.tvPlayerWord)
        bottomWordView = rootView.findViewById(R.id.tvLateWord)

        startGame()
    }

    open fun startGame(){
        secretWord = WordList.randomWord()
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

    fun handleBackspace() {
        with(centerWordView){
            if(text.isNotEmpty()) text = text.substring(0, text.length - 1)
        }
    }

    fun handleEnter() {
        val word = centerWordView.text

        if(word.length == 5){
            with(WordList){
                if(!isValidWord(word)){
                    Snackbar
                        .make(rootView, "Enter a valid english word!", Snackbar.LENGTH_SHORT)
                        .show()
                } else if(compare(word, earlyWord) <= 0){
                    Snackbar
                        .make(rootView, "Enter a word that comes after '$earlyWord' in the dictionary!", Snackbar.LENGTH_SHORT)
                        .show()
                } else if(compare(word, lateWord) >= 0){
                    Snackbar
                        .make(rootView, "Enter a word that comes before '$lateWord' in the dictionary!", Snackbar.LENGTH_SHORT)
                        .show()
                } else{
                    enterWord(word)
                }
            }
        } else{
            // TODO: add an animation or a snackbar or something
        }
    }

    fun handleKey(key: Int) {
        with(centerWordView) {
            if (text.length < 5) text += Key.getChar(key)
        }
    }

    fun enterWord(word : String){
        with(WordList) {
            if(compare(word, secretWord) == 0) win()
            else{
                if (compare(word, secretWord) < 0) earlyWord = word
                else lateWord = word

                topWordView.text = earlyWord
                bottomWordView.text = lateWord
                centerWordView.text = ""

                rangeIndicator.topFraction = getPositionFraction(earlyWord)
                rangeIndicator.bottomFraction = getPositionFraction(lateWord)

                score.currentGuess++

                if(score.currentGuess > 14) lose()
            }
        }
    }

    fun setAlphabet(){
        val nextLetterIndex = centerWordView.text.length // The index of the next letter the player will input

        if(nextLetterIndex < 5){
            val center = centerWordView.text
            val top = topWordView.text
            val bottom = bottomWordView.text

            val startOffset = if(nextLetterIndex == 4 && center == top.substring(0, 4)) 1 else 0
            val endOffset = if(nextLetterIndex == 4 && center == bottom.substring(0, 4)) 1 else 0

            if(center == top.substring(0, nextLetterIndex)) alphabet.start = Key.getKey(top[nextLetterIndex]) + startOffset
            else alphabet.start = Key.A

            if(center == bottom.substring(0, nextLetterIndex)) alphabet.end = Key.getKey(bottom[nextLetterIndex]) - endOffset
            else alphabet.end = Key.Z
        }
    }

    open fun win(){
        centerWordView.boxType = BoxedCharacters.GREEN
        keyboard.setOnKeyPressListener(null)

        with(PreferenceHelper){
            endlessCurrentStreak++ // also sets best streak if appropriate

            when(score.score){
                5 -> endless5point++
                4 -> endless4point++
                3 -> endless3point++
                2 -> endless2point++
                1 -> endless1point++
                else -> Log.wtf("GameFragment", "Called win() with score=${score.score}")
            }
        }

        showEndDialog()
    }

    open fun lose(){
        centerWordView.visibility = View.INVISIBLE
        keyboard.setOnKeyPressListener(null)

        with(PreferenceHelper){
            endlessCurrentStreak = 0

            if(score.score == 0) endlessLosses++
            else Log.wtf("GameFragment", "Called lose() with score=${score.score}")
        }

        showEndDialog()
    }

    open fun showEndDialog(){
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_end)
            .show()

        dialog.findViewById<StatsView>(R.id.stats)?.type = StatsView.ENDLESS
        dialog.findViewById<Button>(R.id.btnClose)?.setOnClickListener { dialog.dismiss() }
        dialog.findViewById<Button>(R.id.btnHome)?.setOnClickListener { dialog.dismiss(); findNavController().navigateUp() }

        dialog.findViewById<TextView>(R.id.tvTitle)?.text = if(score.score > 0) "WIN" else secretWord.uppercase()
        dialog.findViewById<TextView>(R.id.tvResult)?.text = if(score.score > 0) "${score.score} / 5" else "LOSS"
    }
}