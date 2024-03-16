package com.ovidiu.betweenleunlimited.Utils

import android.content.Context
import android.util.Log
import com.ovidiu.betweenleunlimited.Components.Key
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

// TODO: use two lists, one for generating random words (contains only "common" words) and one for checking guesses (longer, contains more "uncommon" words)

object WordList {
    private lateinit var validWords : MutableList<String>
    private lateinit var guessWords : MutableList<String>

    /**
     * Loads the words from a file. May take a bit on older devices. This method must be called before
     * any of the others or an [UninitializedPropertyAccessException] will be thrown. Words with a
     * length that isn't 5 or that contain characters other that letters will be ignored
     * @param resId a resource identifier from [R.raw]
     */
    fun init(context : Context, validWordsResId : Int, guessWordsResId : Int){
        Log.i("WordList", "Starting init...")
        val t = System.currentTimeMillis()

        validWords = mutableListOf()
        val validWordsFile = BufferedReader(InputStreamReader(context.resources.openRawResource(validWordsResId)))

        for(word in validWordsFile.lines()){
            val lower = word.lowercase()
            if(checkWord(lower)) validWords.add(lower)
        }

        Log.i("WordList", "Loaded ${validWords.size} words from valid words file")

        validWords.add("aaaaa")
        validWords.add("zzzzz")
        validWords.sort()

        guessWords = mutableListOf()
        val guessWordsFile = BufferedReader(InputStreamReader(context.resources.openRawResource(guessWordsResId)))

        for(word in guessWordsFile.lines()){
            val lower = word.lowercase()
            if(checkWord(lower)) guessWords.add(lower)
        }

        Log.i("WordList", "Loaded ${guessWords.size} words from guess words file")

        guessWords.sort()

        Log.i("WordList", "Finished init, took ${System.currentTimeMillis() - t}ms")
    }

    private fun checkWord(word : String) : Boolean {
        if(word.length != 5) return false

        for(letter in word){
            if(letter !in "qwertyuiopasdfghjklzxcvbnm") return false
        }

        return true
    }

    fun randomWord(): String {
        // steak steal steam
        // return "steal"
        return guessWords[Random.nextInt(0, guessWords.size)]
    }

    fun isValidWord(word : String) = validWords.contains(word.lowercase())

    fun getPositionFraction(word : String) : Float {
        val i = validWords.indexOf(word.lowercase())

        if(i < 0) return -1f
        return i.toFloat() / (validWords.size - 1)
    }

    /**
     * @return -1 if <code>a</code> comes before <code>b</code>, 1 if <code>a</code> comes after
     * <code>b</code>, 0 if <code>a</code> and <code>b</code> are equal, <code>-2</code>
     * if either <code>a</code> or <code>b</code> are not in the list
     */
    fun compare(a : String, b : String) : Int {
        val ia = validWords.indexOf(a.lowercase())
        val ib = validWords.indexOf(b.lowercase())

        if(ia < 0 || ib < 0) return -2

        return if(ia < ib) -1 else if(ia == ib) 0 else 1
    }
}