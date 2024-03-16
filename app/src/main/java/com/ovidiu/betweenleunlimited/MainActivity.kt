package com.ovidiu.betweenleunlimited

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ovidiu.betweenleunlimited.Utils.WordList

// TODO yet to implement:
//  Win and loss dialog (shows stats like current streak, best streak and score distribution)
//  Daily (just like normal game but with word based on date and separate stats record)
//  "How to play" screen
//  Tutorial
//  Top app bar (note: needs to change between menu and playing)
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

        wordsLoader = object : Thread() {
            override fun run() {
                WordList.init(this@MainActivity, R.raw.valid_words, R.raw.guess_words)
            }
        }

        wordsLoader.start()
    }
}