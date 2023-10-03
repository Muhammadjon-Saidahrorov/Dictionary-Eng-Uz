package uz.gita.dictionarymn.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import uz.gita.dictionarymn.LocalStorage
import uz.gita.dictionarymn.database.MyDatabase

class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        LocalStorage.init(this)
        MyDatabase.init(this)
        context = this
    }
}