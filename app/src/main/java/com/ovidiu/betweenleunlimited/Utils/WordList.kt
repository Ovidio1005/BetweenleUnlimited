package com.ovidiu.betweenleunlimited.Utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Duration
import java.util.Calendar
import kotlin.random.Random

// TODO: use two lists, one for generating random words (contains only "common" words) and one for checking guesses (longer, contains more "uncommon" words)

object WordList {
    val firstDailyDate = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2024)
        set(Calendar.MONTH, Calendar.JANUARY)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    private lateinit var validWords : MutableList<String>
    private lateinit var guessWords : MutableList<String>
    private lateinit var dailyWords : MutableList<String>

    /**
     * Loads the words from a file. May take a bit on older devices. This method must be called before
     * any of the others or an [UninitializedPropertyAccessException] will be thrown. Words with a
     * length that isn't 5 or that contain characters other that letters will be ignored
     * @param resId a resource identifier from [R.raw]
     */
    fun init(context : Context, validWordsResId : Int, guessWordsResId : Int, dailyWordsResId : Int){
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

        // --------

        guessWords = mutableListOf()
        val guessWordsFile = BufferedReader(InputStreamReader(context.resources.openRawResource(guessWordsResId)))

        for(word in guessWordsFile.lines()){
            val lower = word.lowercase()
            if(checkWord(lower)) guessWords.add(lower)
        }

        Log.i("WordList", "Loaded ${guessWords.size} words from guess words file")

        guessWords.sort()

        if(!guessWords.sortedIsSubsetOfSorted(validWords)) Log.wtf("WordList", "Guess words are not a subset of valid words!")

        // --------

        dailyWords = mutableListOf()
        val dailyWordsFile = BufferedReader(InputStreamReader(context.resources.openRawResource(guessWordsResId)))

        for(word in dailyWordsFile.lines()){
            val lower = word.lowercase()
            if(checkWord(lower)) dailyWords.add(lower)
        }

        if(!dailyWords.unsortedIsSubsetOfSorted(validWords)) Log.wtf("WordList", "Daily words are not a subset of valid words!")

        Log.i("WordList", "Loaded ${dailyWords.size} words from daily words file")

        Log.i("WordList", "Finished init, took ${System.currentTimeMillis() - t}ms")
    }

    private fun checkWord(word : String) : Boolean {
        if(word.length != 5) return false

        for(letter in word){
            if(letter !in "qwertyuiopasdfghjklzxcvbnm") return false
        }

        return true
    }

    fun randomWord() : String {
        // steak steal steam
        // return "steal"
        return guessWords[Random.nextInt(0, guessWords.size)]
    }

    fun dailyWord() : String {
        val dailyDay = daysDifference(firstDailyDate, Calendar.getInstance())
        val dailyIndex = dailyDay % dailyWords.size

        return dailyWords[dailyIndex]
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

/**
 * Assumes that both lists are sorted
 * @return <code>true</code> if <code>this</code> is a subset of <code>other</code>, <code>false</code> otherwise
 */
fun List<String>.sortedIsSubsetOfSorted(other : List<String>) : Boolean {
    if(size == 0) return true
    if(other.size == 0) return false

    var ia = 0; var ib = 0

    while(ia < size && ib < other.size){
        if(this[ia] == other[ib]) ia++
        else ib++
    }

    return ia >= size
}

/**
 * Assumes that <code>other</code> is sorted
 * @return <code>true</code> if <code>this</code> is a subset of <code>other</code>, <code>false</code> otherwise
 */
fun List<String>.unsortedIsSubsetOfSorted(other : List<String>) : Boolean {
    if(size == 0) return true
    if(other.size == 0) return false

    for(word in this){
        for(i in other.indices){
            if(word == other[i]) break
            else if(word > other[i]) return false.also { println("$word not in other. Check: ${word !in other}") }
        }
    }

    return true
}

// Implemented myself to keep compatibility with older devices
fun daysDifference(start : Calendar, end : Calendar) : Int {
    if(start.after(end)) return -daysDifference(end, start)

    val s = (start.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val e = (end.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    var diff = 0
    while(s.before(e)){
        s.add(Calendar.DAY_OF_MONTH, 1)
        diff++
    }

    return diff
}