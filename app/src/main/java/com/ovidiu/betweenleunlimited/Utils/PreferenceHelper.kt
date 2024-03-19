package com.ovidiu.betweenleunlimited.Utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val sharedPrefsName = "BetweenleUnlimitedPrefs"
    private lateinit var prefs : SharedPreferences

    fun init(context : Context){
        prefs = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)
    }

    // --- ENDLESS ---

    var endless5point : Int
        get() = prefs.getInt("endless5point", 0)
        set(value) = prefs.edit().putInt("endless5point", value).apply()

    var endless4point : Int
        get() = prefs.getInt("endless4point", 0)
        set(value) = prefs.edit().putInt("endless4point", value).apply()

    var endless3point : Int
        get() = prefs.getInt("endless3point", 0)
        set(value) = prefs.edit().putInt("endless3point", value).apply()

    var endless2point : Int
        get() = prefs.getInt("endless2point", 0)
        set(value) = prefs.edit().putInt("endless2point", value).apply()

    var endless1point : Int
        get() = prefs.getInt("endless1point", 0)
        set(value) = prefs.edit().putInt("endless1point", value).apply()

    var endlessLosses : Int
        get() = prefs.getInt("endlessLosses", 0)
        set(value) = prefs.edit().putInt("endlessLosses", value).apply()

    var endlessCurrentStreak : Int
        get() = prefs.getInt("endlessCurrentStreak", 0)
        set(value) {
            prefs.edit().putInt("endlessCurrentStreak", value).apply()

            if(value > endlessBestStreak) prefs.edit().putInt("endlessBestStreak", value).apply()
        }

    var endlessBestStreak : Int
        get() = prefs.getInt("endlessBestStreak", 0)
        set(value) = prefs.edit().putInt("endlessBestStreak", value).apply()

    val endlessTotalGames : Int
        get() = endless5point + endless4point + endless3point + endless2point + endless1point + endlessLosses

    val endlessTotalWins : Int
        get() = endless5point + endless4point + endless3point + endless2point + endless1point

    val endlessWinPercentage : Float
        get() = (endlessTotalWins.toFloat() / endlessTotalGames.toFloat()) * 100

    // --- Saved game state ---

    /**
     * This value should be set to null if there's no save data.
     * If it isn't null, all other fields shouldn't be null either
     */
    var endlessSecretWord : String?
        get() = prefs.getString("endlessSecretWord", null)
        set(value) = prefs.edit().putString("endlessSecretWord", value).apply()

    var endlessTopWord : String?
        get() = prefs.getString("endlessTopWord", null)
        set(value) = prefs.edit().putString("endlessTopWord", value).apply()

    var endlessBottomWord : String?
        get() = prefs.getString("endlessBottomWord", null)
        set(value) = prefs.edit().putString("endlessBottomWord", value).apply()

    var endlessCurrentGuess : Int?
        get() = prefs.getInt("endlessCurrentGuess", -1).takeIf { g -> g > 0 }
        set(value) = if(value == null) prefs.edit().remove("endlessCurrentGuess").apply() else prefs.edit().putInt("endlessCurrentGuess", value).apply()

    // --- DAILY ---

    var daily5point : Int
        get() = prefs.getInt("daily5point", 0)
        set(value) = prefs.edit().putInt("daily5point", value).apply()

    var daily4point : Int
        get() = prefs.getInt("daily4point", 0)
        set(value) = prefs.edit().putInt("daily4point", value).apply()

    var daily3point : Int
        get() = prefs.getInt("daily3point", 0)
        set(value) = prefs.edit().putInt("daily3point", value).apply()

    var daily2point : Int
        get() = prefs.getInt("daily2point", 0)
        set(value) = prefs.edit().putInt("daily2point", value).apply()

    var daily1point : Int
        get() = prefs.getInt("daily1point", 0)
        set(value) = prefs.edit().putInt("daily1point", value).apply()

    var dailyLosses : Int
        get() = prefs.getInt("dailyLosses", 0)
        set(value) = prefs.edit().putInt("dailyLosses", value).apply()

    var dailyCurrentStreak : Int
        get() = prefs.getInt("dailyCurrentStreak", 0)
        set(value) {
            prefs.edit().putInt("dailyCurrentStreak", value).apply()

            if(value > dailyBestStreak) prefs.edit().putInt("dailyBestStreak", value).apply()
        }

    var dailyBestStreak : Int
        get() = prefs.getInt("dailyBestStreak", 0)
        set(value) = prefs.edit().putInt("dailyBestStreak", value).apply()

    val dailyTotalGames : Int
        get() = daily5point + daily4point + daily3point + daily2point + daily1point + dailyLosses

    val dailyTotalWins : Int
        get() = daily5point + daily4point + daily3point + daily2point + daily1point

    val dailyWinPercentage : Float
        get() = (dailyTotalWins.toFloat() / dailyTotalGames.toFloat()) * 100

    // --- Saved game state ---

    /**
     * This value should be set to null if there's no save data.
     * If it isn't null, all other fields shouldn't be null either
     */
    var dailySecretWord : String?
        get() = prefs.getString("dailySecretWord", null)
        set(value) = prefs.edit().putString("dailySecretWord", value).apply()

    var dailyTopWord : String?
        get() = prefs.getString("dailyTopWord", null)
        set(value) = prefs.edit().putString("dailyTopWord", value).apply()

    var dailyBottomWord : String?
        get() = prefs.getString("dailyBottomWord", null)
        set(value) = prefs.edit().putString("dailyBottomWord", value).apply()

    var dailyCurrentGuess : Int?
        get() = prefs.getInt("dailyCurrentGuess", -1).takeIf { g -> g > 0 }
        set(value) = if(value == null) prefs.edit().remove("dailyCurrentGuess").apply() else prefs.edit().putInt("dailyCurrentGuess", value).apply()

    var dailyDate : String?
        get() = prefs.getString("dailyDate", null)
        set(value) = prefs.edit().putString("dailyDate", value).apply()

    /**
     * Score for the saved daily game (0 = loss) or -1 if the game wasn't finished yet
     */
    var dailyResult : Int
        get() = prefs.getInt("dailyResult", -1)
        set(value) = prefs.edit().putInt("dailyResult", value).apply()
}