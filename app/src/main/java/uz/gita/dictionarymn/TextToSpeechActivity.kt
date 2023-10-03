package uz.gita.dictionarymn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import java.util.*

class TextToSpeechActivity : AppCompatActivity() {
    private var textToSpeech: TextToSpeech? = null
    private lateinit var editText: EditText
    private lateinit var seekBarSpeed: SeekBar
    private lateinit var buttonSpeak: AppCompatButton
    private lateinit var buttonBack: AppCompatImageView
    private lateinit var buttonLanguage: AppCompatImageView
    private lateinit var textLanguage: TextView

    private val localStorage = LocalStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_speech)

        buttonSpeak = findViewById(R.id.button_speak)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {

                var result = if (localStorage?.getLanguage() == true){
                    textToSpeech?.setLanguage(Locale.UK)
                }else{
                    textToSpeech?.setLanguage(Locale.US)
                }

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language not supported!")
                } else {
                    buttonSpeak.isEnabled = true
                }
            } else {
                Log.e("TTS", "Instailization failed!")
            }
        }


        editText = findViewById(R.id.editText)
        seekBarSpeed = findViewById(R.id.seek_bar_speed)
        seekBarSpeed.progress = 50

        buttonBack = findViewById(R.id.backBtnSpeechHome)
        buttonBack.setOnClickListener{
            finish()
        }

        textLanguage = findViewById(R.id.buttonTextLanguage)

        buttonLanguage = findViewById(R.id.buttonLanguage)
        if (localStorage?.getLanguage() == true){
            textLanguage.text = "Language UK"
            buttonLanguage.setImageResource(R.drawable.united_kingdom)
        }else{
            textLanguage.text = "Language USA"
            buttonLanguage.setImageResource(R.drawable.united_america)
        }

        buttonLanguage.setOnClickListener{
            localStorage?.saveLanguage(!localStorage.getLanguage())

            if (localStorage?.getLanguage() == true){
                textLanguage.text = "Language UK"
                buttonLanguage.setImageResource(R.drawable.united_kingdom)
            }else{
                textLanguage.text = "Language USA"
                buttonLanguage.setImageResource(R.drawable.united_america)
            }

        }

        buttonSpeak.setOnClickListener{
            speak()
        }
    }

    private fun speak(){
        val text: String = editText.text.toString()
        var speed: Float = (seekBarSpeed.progress/50F)
        if (speed<0.1) speed = 0.1F
        Log.d("TTT", "speak: $speed")

        if (localStorage?.getLanguage() == true){
            textToSpeech?.language = Locale.UK
        }else{
            textToSpeech?.language = Locale.US
        }

        textToSpeech?.setSpeechRate(speed)
        textToSpeech?.speak(text,TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onDestroy() {
        if (textToSpeech != null){
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }

        super.onDestroy()
    }
}