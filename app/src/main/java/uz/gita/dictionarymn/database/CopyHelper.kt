package uz.gita.dictionarymn.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream

open class CopyHelper(private val context: Context, private val mDataBaseName: String): SQLiteOpenHelper(context, mDataBaseName, null, 1) {
    private var myDataBase: SQLiteDatabase? = null

    init {
        if (!isExist()){
            readableDatabase
            isCopyDatabase()
        }
        openDatabase()
    }

    private fun isExist(): Boolean{
        return context.applicationContext.getDatabasePath(mDataBaseName).exists()
    }

    fun getDatabase(): SQLiteDatabase{
        return myDataBase!!
    }

    fun openDatabase(){
        if(myDataBase != null && myDataBase?.isOpen == true) return

        val path = context.applicationContext.getDatabasePath(mDataBaseName).path
        myDataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun isCopyDatabase(){
        val inputStream = context.applicationContext.assets.open(mDataBaseName)
        inputStream.use {
            val outputFile = context.applicationContext.getDatabasePath(mDataBaseName).absolutePath
            val outputStream = FileOutputStream(outputFile)
            val buff = ByteArray(1024)
            var length = inputStream.read(buff)

            while (length > 0){
                outputStream.write(buff, 0, length)
                length = inputStream.read(buff)
            }

            outputStream.flush()
            outputStream.close()
        }
    }


    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db?.disableWriteAheadLogging()
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun close() {
        myDataBase?.let {
            it.close()
            SQLiteDatabase.releaseMemory()
        }
        super.close()
    }
}