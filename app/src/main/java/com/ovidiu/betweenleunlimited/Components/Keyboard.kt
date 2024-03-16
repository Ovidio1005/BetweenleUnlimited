package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children
import com.ovidiu.betweenleunlimited.R

class Keyboard(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    private val keys : MutableList<Key> = mutableListOf()

    init {
        inflate(context, R.layout.component_keyboard, this)

        val row1 = findViewById<LinearLayout>(R.id.row1)
        val row2 = findViewById<LinearLayout>(R.id.row2)
        val row3 = findViewById<LinearLayout>(R.id.row3)

        for(row in arrayOf(row1, row2, row3)){
            for(key in row.children){
                keys.add(key as Key)
            }
        }
    }

    constructor(context: Context) : this(context, null)

    /**
     * Whenever a <code>Key</code> is clicked, the listener will be invoked. The listener will be
     * passed the <code>character</code> of the <code>Key</code>, which should be one of
     * <code>Key.A</code> ... <code>Key.Z</code>, <code>Key.ENTER</code>, <code>Key.BACKSPACE</code>
     */
    fun setOnKeyPressListener(listener : ((Int) -> Unit)?){
        for(key in keys){
            key.setOnKeyPressListener(listener)
        }
    }
}