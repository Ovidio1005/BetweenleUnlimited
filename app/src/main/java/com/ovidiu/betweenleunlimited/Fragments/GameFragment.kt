package com.ovidiu.betweenleunlimited.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.ovidiu.betweenleunlimited.Components.Alphabet
import com.ovidiu.betweenleunlimited.Components.BoxedCharacters
import com.ovidiu.betweenleunlimited.Components.Key
import com.ovidiu.betweenleunlimited.Components.Keyboard
import com.ovidiu.betweenleunlimited.Components.RangeIndicator
import com.ovidiu.betweenleunlimited.Components.Score
import com.ovidiu.betweenleunlimited.R
import com.ovidiu.betweenleunlimited.Utils.WordList

class GameFragment : Fragment() {
    private lateinit var rootView : View

    private lateinit var score : Score
    private lateinit var keyboard : Keyboard
    private lateinit var alphabet: Alphabet
    private lateinit var rangeIndicator: RangeIndicator
    private lateinit var topWordView : BoxedCharacters
    private lateinit var centerWordView : BoxedCharacters
    private lateinit var bottomWordView : BoxedCharacters

    private lateinit var secretWord : String
    private lateinit var earlyWord : String
    private lateinit var lateWord : String

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

    fun startGame(){
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

        println("Secret word: ${secretWord.uppercase()}")
    }

    private fun handleBackspace() {
        with(centerWordView){
            if(text.isNotEmpty()) text = text.substring(0, text.length - 1)
        }
    }

    private fun handleEnter() {
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

    private fun handleKey(key: Int) {
        with(centerWordView) {
            if (text.length < 5) text += Key.getChar(key)
        }
    }

    private fun enterWord(word : String){
        with(WordList) {
            if(compare(word, secretWord) == 0) win()
            else{
                if (compare(word, secretWord) < 0) earlyWord = word
                else lateWord = word

                if(score.currentGuess < 14){
                    topWordView.text = earlyWord
                    bottomWordView.text = lateWord
                    centerWordView.text = ""

                    score.currentGuess++

                    rangeIndicator.topFraction = getPositionFraction(earlyWord)
                    rangeIndicator.bottomFraction = getPositionFraction(lateWord)
                } else lose()
            }
        }
    }

    private fun setAlphabet(){
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

    private fun win(){
        centerWordView.boxType = BoxedCharacters.GREEN
        keyboard.setOnKeyPressListener(null)
    }

    private fun lose(){
        // TODO
    }
}