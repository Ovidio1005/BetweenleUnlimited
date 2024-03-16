package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import com.ovidiu.betweenleunlimited.R as Res

class Key(context : Context, attributeSet: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context, attributeSet) {
    companion object {
        val A = 1; val B = 2; val C = 3; val D = 4; val E = 5; val F = 6; val G = 7; val H = 8;
        val I = 9; val J = 10; val K = 11; val L = 12; val M = 13; val N = 14; val O = 15;
        val P = 16; val Q = 17; val R = 18; val S = 19; val T = 20; val U = 21; val V = 22;
        val W = 23; val X = 24; val Y = 25; val Z = 26; val ENTER = 27; val BACKSPACE = 28;

        fun getChar(key : Int) = if(key in A..Z) "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[key - 1] else '\u0000'
        fun getKey(letter : Char) = if(letter.uppercaseChar() in "ABCDEFGHIJKLMNOPQRSTUVWXYZ") "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(letter.uppercaseChar()) + A else -1
    }

    private var keyOnClickListener : ((Int) -> Unit)? = null

    var character : Int = A
        set(value) {
            if(value <= Z){
                text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[value - 1].toString()
            } else if(value == ENTER){
                text = "ENTER"
            } else {
                text = "BACK"
            }

            field = value
        }

    init {
        if(attributeSet != null){
            val typedArray = context.obtainStyledAttributes(attributeSet, com.ovidiu.betweenleunlimited.R.styleable.Key)

            character = typedArray.getInt(Res.styleable.Key_character, A)

            typedArray.recycle()
        }

        setOnClickListener {
            keyOnClickListener?.invoke(character)
        }
    }

    constructor(context: Context) : this(context, null)

    /**
     * Whenever this <code>Key</code> is clicked, the listener will be invoked. The listener will be
     * passed the <code>character</code> of this <code>Key</code>, which should be one of
     * <code>Key.A</code> ... <code>Key.Z</code>, <code>Key.ENTER</code>, <code>Key.BACKSPACE</code>
     */
    fun setOnKeyPressListener(listener : ((Int) -> Unit)?){
        keyOnClickListener = listener
    }
}