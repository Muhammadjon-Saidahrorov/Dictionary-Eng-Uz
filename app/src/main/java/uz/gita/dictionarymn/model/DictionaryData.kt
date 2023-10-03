package uz.gita.dictionarymn.model

data class DictionaryData(
    val id: String,
    val english: String,
    val type: String,
    val transcript: String,
    val uzbek: String,
    val countable: String,
    val favourite: Int
)