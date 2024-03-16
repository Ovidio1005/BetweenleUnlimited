package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.ovidiu.betweenleunlimited.R

/**
 * <code>secretFraction</code> should be the <code>WordList.getPositionFraction()</code> of the secret word,
 * <code>topFraction</code> that of the top word and <code>bottomFraction</code> that of the bottom word.
 * It should always hold that <code>topFraction</code> <= <code>secretFraction</code> <= <code>bottomFraction</code>
 *
 * The top <code>TextView</code> will show the percentage of words that are between the top word and
 * the secret word, while the bottom <code>TextView</code> will show the percentage of words that are
 * between the secret word and the bottom word. The vertical position of the dot represents how close
 * the secret word is to the top and bottom words.
 *
 * If any of the fractions are <code><0</code> both <code>TextView</code>s will show '?' and the
 * dot will be invisible
 */
class RangeIndicator(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    private val tvTop : TextView
    private val tvBottom : TextView
    private val bar : View
    private val dot : View

    var widthDp : Float = 48f
        set(value) {
            val sizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)

            tvTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx * (18f/48f))
            with(tvTop.layoutParams as LinearLayout.LayoutParams){
                width = sizePx.toInt()
                height = (sizePx * (2f/3f)).toInt()
                tvTop.layoutParams = this
            }

            tvBottom.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx * (18f/48f))
            with(tvBottom.layoutParams as LinearLayout.LayoutParams){
                width = sizePx.toInt()
                height = (sizePx * (2f/3f)).toInt()
                tvBottom.layoutParams = this
            }

            with(bar.layoutParams as ConstraintLayout.LayoutParams){
                width = (sizePx * (3f/48f)).toInt()
                height = (sizePx * (96f/48f)).toInt()
                bar.layoutParams = this
            }

            with(dot.layoutParams as ConstraintLayout.LayoutParams){
                width = (sizePx * (14f/48f)).toInt()
                height = (sizePx * (14f/48f)).toInt()
                dot.layoutParams = this
            }

            field = value
        }

    var secretFraction : Float = -1f
        set(value){
            field = value
            setViews()
        }

    var topFraction : Float = -1f
        set(value){
            field = value
            setViews()
        }

    var bottomFraction : Float = -1f
        set(value){
            field = value
            setViews()
        }

    init {
        inflate(context, R.layout.component_range_indicator, this)

        tvTop = findViewById(R.id.tvTop)
        tvBottom = findViewById(R.id.tvBottom)
        bar = findViewById(R.id.divBar)
        dot = findViewById(R.id.dot)

        if(attributeSet != null){
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RangeIndicator)

            widthDp = typedArray.getFloat(R.styleable.RangeIndicator_widthDp, 48f)
            secretFraction = typedArray.getFloat(R.styleable.RangeIndicator_secretFraction, -1f)
            topFraction = typedArray.getFloat(R.styleable.RangeIndicator_topFraction, -1f)
            bottomFraction = typedArray.getFloat(R.styleable.RangeIndicator_bottomFraction, -1f)

            typedArray.recycle()
        }
    }

    constructor(context: Context) : this(context, null)

    private fun setViews(){
        if(secretFraction < 0 || topFraction < 0 || bottomFraction < 0){
            tvTop.text = "?"
            tvBottom.text = "?"
            dot.isVisible = false
        } else {
            val topNumber = (secretFraction - topFraction) * 100
            val bottomNumber = (bottomFraction - secretFraction) * 100
            val dotPosition = (secretFraction - topFraction) / (bottomFraction - topFraction) // 0 = at the top, 1 = at the bottom

            tvTop.text = getString(topNumber)
            tvBottom.text = getString(bottomNumber)
            dot.isVisible = true

            with(dot.layoutParams as ConstraintLayout.LayoutParams){
                verticalBias = dotPosition
                dot.layoutParams = this
            }
        }
    }

    private fun getString(number : Float) : String {
        if(number >= 10){
            return "${number.toInt()}"
        } else if(number >= 1){
            val firstDigit = number.toInt()
            val secondDigit = (number * 10).toInt() % 10
            return "$firstDigit.$secondDigit"
        } else{
            return "0.${String.format("%02d", (number * 100).toInt())}"
        }
    }
}