package com.ovidiu.betweenleunlimited.Components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ovidiu.betweenleunlimited.R
import com.ovidiu.betweenleunlimited.Utils.PreferenceHelper

class StatsView(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {
    companion object {
        val DAILY = 1; val ENDLESS = 2
    }

    private lateinit var tvPlayed : TextView
    private lateinit var tvWon : TextView
    private lateinit var tvWinPercentage : TextView
    private lateinit var tvCurrentStreak : TextView
    private lateinit var tvBestStreak : TextView

    private lateinit var tv5Percentage : TextView
    private lateinit var tv4Percentage : TextView
    private lateinit var tv3Percentage : TextView
    private lateinit var tv2Percentage : TextView
    private lateinit var tv1Percentage : TextView
    private lateinit var tvLossPercentage : TextView

    private lateinit var pb5 : ProgressBar
    private lateinit var pb4 : ProgressBar
    private lateinit var pb3 : ProgressBar
    private lateinit var pb2 : ProgressBar
    private lateinit var pb1 : ProgressBar
    private lateinit var pbLoss : ProgressBar

    var type = DAILY
        set(value) {
            field = value
            setViews()
        }

    init {
        inflate(context, R.layout.component_stats_view, this)

        tvPlayed = findViewById(R.id.tvPlayed)
        tvWon = findViewById(R.id.tvWon)
        tvWinPercentage = findViewById(R.id.tvWinPercentage)
        tvCurrentStreak = findViewById(R.id.tvCurrentStreak)
        tvBestStreak = findViewById(R.id.tvBestStreak)

        tv5Percentage = findViewById(R.id.tv5Percentage)
        tv4Percentage = findViewById(R.id.tv4Percentage)
        tv3Percentage = findViewById(R.id.tv3Percentage)
        tv2Percentage = findViewById(R.id.tv2Percentage)
        tv1Percentage = findViewById(R.id.tv1Percentage)
        tvLossPercentage = findViewById(R.id.tvLossesPercentage)

        pb5 = findViewById(R.id.pb5)
        pb4 = findViewById(R.id.pb4)
        pb3 = findViewById(R.id.pb3)
        pb2 = findViewById(R.id.pb2)
        pb1 = findViewById(R.id.pb1)
        pbLoss = findViewById(R.id.pbLosses)

        if(attributeSet != null){
            val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.StatsView)

            type = typedArray.getInt(R.styleable.StatsView_type, DAILY) // also calls setViews()

            typedArray.recycle()
        } else setViews()
    }

    constructor(context: Context) : this(context, null)

    fun setViews(){
        if(type == DAILY){
            tvPlayed.text = PreferenceHelper.dailyTotalGames.toString()
            tvWon.text = PreferenceHelper.dailyTotalWins.toString()
            tvWinPercentage.text = PreferenceHelper.dailyWinPercentage.toInt().toString()
            tvCurrentStreak.text = PreferenceHelper.dailyCurrentStreak.toString()
            tvBestStreak.text = PreferenceHelper.dailyBestStreak.toString()

            val percent5 = ((PreferenceHelper.daily5point.toFloat() / PreferenceHelper.dailyTotalGames.toFloat()) * 100).toInt()
            val percent4 = ((PreferenceHelper.daily4point.toFloat() / PreferenceHelper.dailyTotalGames.toFloat()) * 100).toInt()
            val percent3 = ((PreferenceHelper.daily3point.toFloat() / PreferenceHelper.dailyTotalGames.toFloat()) * 100).toInt()
            val percent2 = ((PreferenceHelper.daily2point.toFloat() / PreferenceHelper.dailyTotalGames.toFloat()) * 100).toInt()
            val percent1 = ((PreferenceHelper.daily1point.toFloat() / PreferenceHelper.dailyTotalGames.toFloat()) * 100).toInt()
            val percentLoss = ((PreferenceHelper.dailyLosses.toFloat() / PreferenceHelper.dailyTotalGames.toFloat()) * 100).toInt()

            tv5Percentage.text = "$percent5 %"
            tv4Percentage.text = "$percent4 %"
            tv3Percentage.text = "$percent3 %"
            tv2Percentage.text = "$percent2 %"
            tv1Percentage.text = "$percent1 %"
            tvLossPercentage.text = "$percentLoss %"

            pb5.progress = percent5
            pb4.progress = percent4
            pb3.progress = percent3
            pb2.progress = percent2
            pb1.progress = percent1
            pbLoss.progress = percentLoss
        } else{
            tvPlayed.text = PreferenceHelper.endlessTotalGames.toString()
            tvWon.text = PreferenceHelper.endlessTotalWins.toString()
            tvWinPercentage.text = PreferenceHelper.endlessWinPercentage.toInt().toString()
            tvCurrentStreak.text = PreferenceHelper.endlessCurrentStreak.toString()
            tvBestStreak.text = PreferenceHelper.endlessBestStreak.toString()

            val percent5 = ((PreferenceHelper.endless5point.toFloat() / PreferenceHelper.endlessTotalGames.toFloat()) * 100).toInt()
            val percent4 = ((PreferenceHelper.endless4point.toFloat() / PreferenceHelper.endlessTotalGames.toFloat()) * 100).toInt()
            val percent3 = ((PreferenceHelper.endless3point.toFloat() / PreferenceHelper.endlessTotalGames.toFloat()) * 100).toInt()
            val percent2 = ((PreferenceHelper.endless2point.toFloat() / PreferenceHelper.endlessTotalGames.toFloat()) * 100).toInt()
            val percent1 = ((PreferenceHelper.endless1point.toFloat() / PreferenceHelper.endlessTotalGames.toFloat()) * 100).toInt()
            val percentLoss = ((PreferenceHelper.endlessLosses.toFloat() / PreferenceHelper.endlessTotalGames.toFloat()) * 100).toInt()

            tv5Percentage.text = "$percent5 %"
            tv4Percentage.text = "$percent4 %"
            tv3Percentage.text = "$percent3 %"
            tv2Percentage.text = "$percent2 %"
            tv1Percentage.text = "$percent1 %"
            tvLossPercentage.text = "$percentLoss %"

            pb5.progress = percent5
            pb4.progress = percent4
            pb3.progress = percent3
            pb2.progress = percent2
            pb1.progress = percent1
            pbLoss.progress = percentLoss
        }
    }
}