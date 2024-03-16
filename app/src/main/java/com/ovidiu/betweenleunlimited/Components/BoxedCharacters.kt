package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.ovidiu.betweenleunlimited.R

class BoxedCharacters(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    companion object {
        val OUTLINED = 1; val GREEN = 2; val BLUE = 3; val YELLOW = 4
    }

    private val textViews = mutableListOf<TextView>()

    var boxSizeDp : Float = 64f
        set(value) {
            val sizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

            for(tv in textViews){
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx * 0.75f)
                with(tv.layoutParams as LinearLayout.LayoutParams){
                    width = sizePx.toInt()
                    height = sizePx.toInt()
                    tv.layoutParams = this
                }
            }

            field = value
        }


    var boxType : Int = 1
        set(value) {
            val boxBackground = when(value) {
                1 -> R.drawable.box_outlined
                2 -> R.drawable.box_green
                3 -> R.drawable.box_blue
                4 -> R.drawable.box_yellow
                else -> R.drawable.box_outlined
            }

            for(tv in textViews){
                tv.setBackgroundResource(boxBackground)
            }

            field = value
        }

    var text : String = ""
        set(value) {
            for(i in textViews.indices){
                textViews[i].text = if(value.length > i) value[i].toString().uppercase() else ""
            }

            field = value
        }

    init {
        inflate(context, R.layout.component_boxed_characters, this)

        textViews.add(findViewById(R.id.tv1))
        textViews.add(findViewById(R.id.tv2))
        textViews.add(findViewById(R.id.tv3))
        textViews.add(findViewById(R.id.tv4))
        textViews.add(findViewById(R.id.tv5))

        if(attributeSet != null){
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BoxedCharacters)

            boxType = typedArray.getInt(R.styleable.BoxedCharacters_boxType, -1)
            text = typedArray.getString(R.styleable.BoxedCharacters_text) ?: ""
            boxSizeDp = typedArray.getFloat(R.styleable.BoxedCharacters_boxSizeDp, 64f)

            typedArray.recycle()
        }
    }

    constructor(context: Context) : this(context, null)
}