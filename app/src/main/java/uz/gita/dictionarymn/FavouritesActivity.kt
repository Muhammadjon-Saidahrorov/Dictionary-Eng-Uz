package uz.gita.dictionarymn

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dictionarymn.adapter.CursorAdapter
import uz.gita.dictionarymn.adapter.UzbekAdapter
import uz.gita.dictionarymn.database.MyDatabase
import java.util.*

class FavouritesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var database: MyDatabase
    private lateinit var adapter: CursorAdapter
    private lateinit var adapterUz: UzbekAdapter
    private lateinit var backBtn: AppCompatImageView
    private var tts: TextToSpeech? = null
    private val localStorage = LocalStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        backBtn = findViewById(R.id.backBtn)
        tts = TextToSpeech(this, this)

        des()
        click()
    }

    @SuppressLint("Range")
    private fun des() {
        database = MyDatabase.getDatabase()
        val cursor = database.getFavourite()
        adapter = CursorAdapter(cursor)
        adapterUz = UzbekAdapter(cursor)
        adapter.notifyDataSetChanged()

        val rv = findViewById<RecyclerView>(R.id.favouritsList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapterUz

        if (localStorage?.getLogic() == true) {

            rv.layoutManager = LinearLayoutManager(this)
            rv.adapter = adapter

            adapter.setClickButtonTranslate {
                val view: View = LayoutInflater.from(this).inflate(R.layout.translate_dialog, null)
                val alertDialog: AlertDialog = AlertDialog.Builder(this)
                    .setView(view)
                    .create()

                alertDialog.show()
                alertDialog.window?.setBackgroundDrawable(null)

                val englishText: TextView = view.findViewById(R.id.english)
                val typeText: TextView = view.findViewById(R.id.type)
                val transcriptText: TextView = view.findViewById(R.id.transcript)
                val uzbekText: TextView = view.findViewById(R.id.uzbek)
                val countableText: TextView = view.findViewById(R.id.countable)

                val english = it.getString(1)
                val type = it.getString(2)
                val transcript = it.getString(3)
                val uzbek = it.getString(4)
                val countable = it.getString(5)


                englishText.text = english
                typeText.text = type
                transcriptText.text = transcript
                uzbekText.text = uzbek
                countableText.text = countable

                val speakBtn: ImageView = view.findViewById(R.id.speakBtn)

                speakBtn.setOnClickListener {
                    tts!!.language = Locale.ENGLISH
                    tts!!.setSpeechRate(0.8F)
                    tts!!.speak(english, TextToSpeech.QUEUE_FLUSH, null, "")
                }

                val favourite: ImageView = view.findViewById(R.id.favourite)

                var old = it.getInt(6)
                val id = it.getString(0)

                if (old == 0) {
                    favourite.setImageResource(R.drawable.ic_bookmark)
                } else {
                    favourite.setImageResource(R.drawable.ic_bookmark_select)
                }

                favourite.setOnClickListener {

                    old = if (old == 1) {
                        favourite.setImageResource(R.drawable.ic_bookmark)
                        database.updateWord(0, id)
                        adapter.cursor = database.getFavourite()
                        adapter.notifyDataSetChanged()
                        alertDialog.dismiss()
                        0
                    } else {
                        favourite.setImageResource(R.drawable.ic_bookmark_select)
                        database.updateWord(1, id)
                        adapter.cursor = database.getFavourite()
                        adapter.notifyDataSetChanged()
                        alertDialog.dismiss()
                        1
                    }
                }



            }
        } else {

            adapterUz.setClickButtonTranslate {
                val view: View =
                    LayoutInflater.from(this).inflate(R.layout.translate_dialog_uz, null)
                val alertDialog: AlertDialog = AlertDialog.Builder(this)
                    .setView(view)
                    .create()

                alertDialog.show()
                alertDialog.window?.setBackgroundDrawable(null)

                val englishText: TextView = view.findViewById(R.id.english)
                val typeText: TextView = view.findViewById(R.id.type)
                val uzbekText: TextView = view.findViewById(R.id.uzbek)
                val countableText: TextView = view.findViewById(R.id.countable)

                val english = it.getString(1)
                val type = it.getString(2)
                val uzbek = it.getString(4)
                val countable = it.getString(5)

                englishText.text = english
                typeText.text = type
                uzbekText.text = uzbek
                countableText.text = countable

                val favourite: ImageView = view.findViewById(R.id.favourite)

                var old = it.getInt(6)
                val id = it.getString(0)

                if (old == 0) {
                    favourite.setImageResource(R.drawable.ic_bookmark)
                } else {
                    favourite.setImageResource(R.drawable.ic_bookmark_select)
                }

                favourite.setOnClickListener {

                    old = if (old == 1) {
                        favourite.setImageResource(R.drawable.ic_bookmark)
                        database.updateWord(0, id)
                        adapterUz.cursor = database.getFavourite()
                        adapterUz.notifyDataSetChanged()
                        alertDialog.dismiss()
                        0
                    } else {
                        favourite.setImageResource(R.drawable.ic_bookmark_select)
                        database.updateWord(1, id)
                        adapterUz.cursor = database.getFavourite()
                        adapterUz.notifyDataSetChanged()
                        alertDialog.dismiss()
                        1
                    }

                }

            }
        }

    }

    private fun click() {
        adapter.setChangeSpeakListener {
            tts!!.language = Locale.ENGLISH
            tts!!.setSpeechRate(0.8F)
            tts!!.speak(it, TextToSpeech.QUEUE_FLUSH, null,"")
        }

        backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
            }
        }
    }
}