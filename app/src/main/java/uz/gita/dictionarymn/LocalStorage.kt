package uz.gita.dictionarymn

import android.content.Context
import android.content.SharedPreferences

class LocalStorage(context: Context) {

    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    init {
        preferences = context.getSharedPreferences("EXAM3", Context.MODE_PRIVATE)
        editor = preferences?.edit()
    }


    companion object {
        private var localStorge: LocalStorage? = null

        fun getInstance(): LocalStorage? {
            return localStorge
        }

        fun init(context: Context) {
            if (localStorge == null) localStorge = LocalStorage(context)
        }

    }


    fun saveLogic(b: Boolean) {
        editor!!.putBoolean("LOGIC", b).apply()
    }

    fun getLogic(): Boolean {
        return preferences!!.getBoolean("LOGIC", true)
    }

    fun saveLanguage(b: Boolean) {
        editor!!.putBoolean("LANGUAGE", b).apply()
    }

    fun getLanguage(): Boolean {
        return preferences!!.getBoolean("LANGUAGE", true)
    }


}