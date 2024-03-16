package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.ovidiu.betweenleunlimited.R

class Score(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    private val dots = mutableListOf<View>()
    private lateinit var tvCurrentGuess : TextView
    private lateinit var tvScore : TextView

    var currentGuess = 1
        set(value) {
            field = if(value in 1..14) value else if(value < 1) 1 else 14
            setViews()
        }

    val score : Int
        get() = when(currentGuess){
            1, 2, 3, 4, 5 -> 5
            6, 7, 8 -> 4
            9, 10, 11 -> 3
            12, 13 -> 2
            14 -> 1
            else -> 0
        }

    init {
        inflate(context, R.layout.component_score, this)

        for(dot in findViewById<LinearLayout>(R.id.layoutDots).children){
            dots.add(dot)
        }

        tvCurrentGuess = findViewById(R.id.tvCurrentGuess)
        tvScore = findViewById(R.id.tvScore)
    }

    constructor(context: Context) : this(context, null)

    fun setViews(){
        tvCurrentGuess.text = "$currentGuess / 14"
        tvScore.text = "$score / 5"

        for(i in 1..<currentGuess){
            dots[i-1].setBackgroundResource(R.drawable.circle_blue)
        }

        dots[currentGuess-1].setBackgroundResource(R.drawable.circle_yellow)

        for(i in currentGuess+1..14){
            dots[i-1].setBackgroundResource(R.drawable.circle_grey)
        }
    }
}