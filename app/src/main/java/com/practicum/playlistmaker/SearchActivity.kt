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

class SearchActivity : AppCompatActivity() {
    var saveSearchText = ""
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
        buttonArrowBack.setOnClickListener{
            finish()
        }

        val inputTextSearch = findViewById<EditText>(R.id.search_edit_text)
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
        savedInstanceState?.let {
            saveSearchText = it.getString("SearchText", "")
            inputTextSearch.setText(saveSearchText)
        }



    }
   override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SearchText", saveSearchText)
    }


    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun clearButtonVisibility(s: CharSequence?):Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}