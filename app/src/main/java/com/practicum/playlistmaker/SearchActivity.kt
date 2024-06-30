package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var saveSearchText = ""
    private lateinit var inputTextSearch: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonArrowBack = findViewById<ImageView>(R.id.search_button_arrow_back)
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tracks = listOf(
            Track(
                trackName = "Smells Like Teen Spirit",
                artisName = "Nirvana",
                trackTime = "5:01",
                artworkUrl100 = getString(R.string.nirvanaUrlPicture)),
            Track(
                trackName = "Billie Jean",
                artisName = "Michael Jackson",
                trackTime = "4:35",
                artworkUrl100 = getString(R.string.jacksonUrlPicture)
            ),
            Track(
                trackName = "Stayin' Alive",
                artisName = "Bee Gees",
                trackTime = "4:10",
                artworkUrl100 = getString(R.string.beeGeesUrlPicture)
            ),
            Track(
                trackName = "Whole Lotta Love",
                artisName = "Led Zeppelin",
                trackTime = "5:33",
                artworkUrl100 = getString(R.string.ledZeppelinUrlPicture)
            ),
            Track(
                trackName = "Sweet Child O'Mine",
                artisName = "Guns N' Roses",
                trackTime = "5:03",
                artworkUrl100 = getString(R.string.gunsUrlPitcture)
            )
        )

        val songsAdapter = SongsAdapter(tracks)
        recyclerView.adapter = songsAdapter

        buttonArrowBack.setOnClickListener{
            finish()
        }

        inputTextSearch = findViewById<EditText>(R.id.search_edit_text)
        inputTextSearch.setSelectAllOnFocus(true)
        val clearButton = findViewById<ImageView>(R.id.search_close_button)


        clearButton.setOnClickListener{
            inputTextSearch.setText("")
            hideKeyboard(this, inputTextSearch)

        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveSearchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)


            }

            override fun afterTextChanged(s: Editable?) {

            }

        }


        inputTextSearch.addTextChangedListener(textWatcher)








    }

    companion object {
        const val VALUE_KEY = "SearchText"
    }

   override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VALUE_KEY, saveSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveSearchText = savedInstanceState.getString(VALUE_KEY, "")
        inputTextSearch.setText(saveSearchText)


    }


    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clearButtonVisibility(s: CharSequence?):Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}