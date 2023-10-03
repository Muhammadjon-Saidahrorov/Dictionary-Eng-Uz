package uz.gita.dictionarymn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView

class EnterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        val localStorage = LocalStorage.getInstance()

        findViewById<AppCompatButton>(R.id.engUz).setOnClickListener{
            localStorage?.saveLogic(true)
            startActivity(Intent(this,MainActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.uzEng).setOnClickListener{
            localStorage?.saveLogic(false)
            startActivity(Intent(this,MainActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.textSpeech).setOnClickListener{
            startActivity(Intent(this,TextToSpeechActivity::class.java))
        }

        findViewById<AppCompatImageView>(R.id.infoBtn).setOnClickListener{
            val view: View = LayoutInflater.from(this).inflate(R.layout.info_dialog, null)
            val alertDialog: AlertDialog = AlertDialog.Builder(this)
                .setView(view)
                .create()

            alertDialog.show()
            alertDialog.window?.setBackgroundDrawable(null)
        }
    }
}