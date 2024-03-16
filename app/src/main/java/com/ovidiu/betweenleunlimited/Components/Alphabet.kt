package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.ovidiu.betweenleunlimited.R

class Alphabet(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    private val letters = mutableListOf<TextView>()

    var start = Key.A
        set(value) {
            field = value
            setViews()
        }

    var end = Key.Z
        set(value) {
            field = value
            setViews()
        }

    init {
        inflate(context, R.layout.component_alphabet, this)

        val row1 = findViewById<LinearLayout>(R.id.row1)
        val row2 = findViewById<LinearLayout>(R.id.row2)

        for(row in arrayOf(row1, row2)){
            for(letter in row.children){
                letters.add(letter as TextView)
            }
        }
    }

    constructor(context: Context) : this(context, null)

    fun setViews(){
        for(i in Key.A..Key.Z){
            if(i in start..end) letters[i - Key.A].setBackgroundResource(R.drawable.alphabet_on)
            else letters[i - Key.A].setBackgroundResource(R.drawable.alphabet_off)
        }
    }
}