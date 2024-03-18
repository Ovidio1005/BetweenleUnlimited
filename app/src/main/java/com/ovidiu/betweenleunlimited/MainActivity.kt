package com.ovidiu.betweenleunlimited

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ovidiu.betweenleunlimited.Utils.PreferenceHelper
import com.ovidiu.betweenleunlimited.Utils.WordList

// TODO yet to implement:
//  "How to play" screen
//  Tutorial
//  Top app bar (note: needs to change between menu and playing)
//  "Hard mode" (picks a random word from all valid words)
//  Better looking UI
//  Automatic UI resizing based on screen size
//  Lock screen rotation on small devices but not large ones

// TODO bug fixes:
//  Game resets on activity restart (eg. screen rotation)

class MainActivity : AppCompatActivity() {
    lateinit var wordsLoader : Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceHelper.init(applicationContext)

        wordsLoader = object : Thread() {
            override fun run() {
                WordList.init(this@MainActivity, R.raw.valid_words, R.raw.guess_words, R.raw.daily_words)
            }
        }

        wordsLoader.start()
    }
}