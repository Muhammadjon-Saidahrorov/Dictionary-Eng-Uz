package uz.gita.dictionarymn.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class MyDatabase private constructor(context: Context) : CopyHelper(context, "Dictionary.db") {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var dataBase: MyDatabase

        fun init(context: Context) {
            dataBase = MyDatabase(context)
        }

        fun getDatabase(): MyDatabase = dataBase
    }

    fun getAllWords(): Cursor {
        return getDatabase().rawQuery("select * from dictionary", null)
    }

//    fun getWordByQuery(query: String): Cursor {
//        return getDatabase().rawQuery("select * from dictionary where dictionary.english like '%$query%'", null)
//    }

    fun getWordByQueryEnglish(query: String): Cursor {
        val sanitizedQuery = query.replace("'", "''")
        val sqlQuery = "SELECT * FROM dictionary WHERE dictionary.english LIKE '%$sanitizedQuery%' AND dictionary.english GLOB '[a-zA-Z]*' ORDER BY english ASC"
        return getDatabase().rawQuery(sqlQuery, null)
    }

//    fun getWordByQueryUz(query: String): Cursor {
//        return getDatabase().rawQuery("select * from dictionary where dictionary.uzbek like \"$query%\"", null)
//    }

    fun getWordByQueryUzbek(query: String): Cursor {
        val sanitizedQuery = query.replace("'", "''")
        val sqlQuery = "SELECT * FROM dictionary WHERE dictionary.uzbek LIKE '%$sanitizedQuery%' AND dictionary.uzbek GLOB '[a-zA-Z]*' ORDER BY uzbek ASC"
        return getDatabase().rawQuery(sqlQuery, null)
    }

    fun updateWord(remember: Int, id: String) {
        val cv = ContentValues()
        cv.put("favourite", remember)
        getDatabase().update("dictionary", cv, "dictionary.id == $id", null)
    }

     fun getFavourite(): Cursor {
        return getDatabase().rawQuery("SELECT * FROM dictionary WHERE favourite = 1",null)
    }


//    fun getById(id: String): Cursor {
//        return getDatabase().rawQuery("SELECT * FROM dictionary WHERE id = $id",null)
//    }
}