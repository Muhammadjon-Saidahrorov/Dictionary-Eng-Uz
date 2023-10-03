package uz.gita.dictionarymn.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dictionarymn.R
import uz.gita.dictionarymn.model.DictionaryData
import uz.gita.dictionarymn.utils.spannable

class UzbekAdapter(var cursor: Cursor, var query: String = ""): RecyclerView.Adapter<UzbekAdapter.CursorViewHolder>() {

    private var clickButtonTranslate: ((Cursor) -> Unit)? = null

    @SuppressLint("Range")
    inner class CursorViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val textTitle = view.findViewById<TextView>(R.id.titleUz)

        init {
            textTitle.setOnClickListener{
                cursor.moveToPosition(adapterPosition)
                clickButtonTranslate?.invoke(cursor)
            }

        }

        fun bind(data: DictionaryData){
            if(query == "") textTitle.text = data.uzbek
            else textTitle.text = data.uzbek.spannable(query)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursorViewHolder {
        return CursorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dictionary_uz,parent,false))
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: CursorViewHolder, position: Int) {
        cursor.moveToPosition(position)
        val data = DictionaryData(
            cursor.getString(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getString(5),
            cursor.getInt(6)
        )
        holder.bind(data)
    }

    fun setClickButtonTranslate(block: (Cursor) -> Unit){
        clickButtonTranslate = block
    }
}