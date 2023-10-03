package uz.gita.dictionarymn

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dictionarymn.adapter.CursorAdapter
import uz.gita.dictionarymn.adapter.UzbekAdapter
import uz.gita.dictionarymn.database.MyDatabase
import uz.gita.dictionarymn.model.DictionaryData
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var database: MyDatabase

    private lateinit var adapter: CursorAdapter
    private lateinit var adapterUz: UzbekAdapter
    private lateinit var backBtn: AppCompatImageView
    private lateinit var favourBtn: AppCompatImageView
    private lateinit var replaceBtn: AppCompatImageView
    private lateinit var searchView: SearchView
    private lateinit var textTitle: TextView
    private var _query = ""
    private var tts: TextToSpeech? = null

    private val localStorage = LocalStorage.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView = findViewById(R.id.searchView)

        tts = TextToSpeech(this@MainActivity, this@MainActivity)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {

                    if (localStorage?.getLogic() == true) {
                        adapter.cursor = database.getWordByQueryEnglish(it.trim())
                        adapter.query = it.trim()
                        _query = it.trim()
                        adapter.notifyDataSetChanged()
                    } else {
                        adapterUz.cursor = database.getWordByQueryUzbek(it.trim())
                        adapterUz.query = it.trim()
                        _query = it.trim()
                        adapterUz.notifyDataSetChanged()
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {

                    if (localStorage?.getLogic() == true) {
                        adapter.cursor = database.getWordByQueryEnglish(it.trim())
                        adapter.query = it.trim()
                        _query = it.trim()
                        adapter.notifyDataSetChanged()
                    } else {
                        adapterUz.cursor = database.getWordByQueryUzbek(it.trim())
                        adapterUz.query = it.trim()
                        _query = it.trim()
                        adapterUz.notifyDataSetChanged()
                    }

                }
                return true
            }
        })

        backBtn = findViewById(R.id.buttonHome)

        backBtn.setOnClickListener {
            finish()
        }

        favourBtn = findViewById(R.id.favouritesBtn)

        favourBtn.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }

        replaceBtn = findViewById(R.id.replaceBtn)

        replaceBtn.setOnClickListener {
            localStorage?.saveLogic(!localStorage.getLogic())
            des()
        }

        textTitle = findViewById(R.id.textTitle)
    }

    @SuppressLint("Range")
    private fun des() {
        database = MyDatabase.getDatabase()
        var cursor = database.getAllWords()
        val rv = findViewById<RecyclerView>(R.id.dictionaryList)

        if (localStorage?.getLogic() == true) {

            adapter = CursorAdapter(cursor)
            adapter.notifyDataSetChanged()
            rv.layoutManager = LinearLayoutManager(this)
            rv.adapter = adapter

            textTitle.text = "English-Uzbek"

            adapter.setChangeSpeakListener {
                tts!!.language = Locale.ENGLISH
                tts!!.setSpeechRate(0.8F)
                tts!!.speak(it, TextToSpeech.QUEUE_FLUSH, null, "")
            }

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
                        adapter.cursor = database.getAllWords()
                        adapter.notifyDataSetChanged()
                        0
                    } else {
                        favourite.setImageResource(R.drawable.ic_bookmark_select)
                        database.updateWord(1, id)
                        adapter.cursor = database.getAllWords()
                        adapter.notifyDataSetChanged()
                        1
                    }

                }

            }

        } else {
            adapterUz = UzbekAdapter(cursor)
            adapterUz.notifyDataSetChanged()
            rv.layoutManager = LinearLayoutManager(this)
            rv.adapter = adapterUz

            textTitle.text = "Uzbek-English"

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
                        adapterUz.cursor = database.getAllWords()
                        adapterUz.notifyDataSetChanged()
                        0
                    } else {
                        favourite.setImageResource(R.drawable.ic_bookmark_select)
                        database.updateWord(1, id)
                        adapterUz.cursor = database.getAllWords()
                        adapterUz.notifyDataSetChanged()
                        1
                    }

                }

            }
        }

    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        des()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
            }
        }
    }
}