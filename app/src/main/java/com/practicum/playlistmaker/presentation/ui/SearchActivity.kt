package com.practicum.playlistmaker.presentation.ui

import com.practicum.playlistmaker.data.SearchHistoryManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {
    private var saveSearchText = ""
    private lateinit var inputTextSearch: EditText

    private val track = ArrayList<Track>()

    private val interactor: TracksInteractor
        get() = Creator.provideTracksInteractor()


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
        val notInternet = findViewById<LinearLayout>(R.id.errors)
        val updateButton = findViewById<Button>(R.id.search_update_btt)
        val notFound = findViewById<LinearLayout>(R.id.search_not_found)
        val clearButton = findViewById<ImageView>(R.id.search_close_button)
        val recyclerViewForHistory = findViewById<RecyclerView>(R.id.rv_search_history)
        val inputTextSearch = findViewById<EditText>(R.id.search_edit_text)
        val buttonSearchHistory = findViewById<Button>(R.id.bt_search_clear)
        val searchHistoryLayout = findViewById<ScrollView>(R.id.search_history)
        val progressBar = findViewById<ProgressBar>(R.id.pb_search_history)

        val handler = Handler(Looper.getMainLooper())


        recyclerView.layoutManager = LinearLayoutManager(this)
        val songsAdapter = SongsAdapter( track, this)
        recyclerView.adapter = songsAdapter


        val searchHistoryManager = SearchHistoryManager(this)
        val historyList  = searchHistoryManager.getSearchHistory().toMutableList()
        recyclerViewForHistory.layoutManager = LinearLayoutManager(this)
        val historyAdapter = SongsAdapter(historyList,this)
        recyclerViewForHistory.adapter = historyAdapter



        searchHistoryLayout.visibility = if(historyList.isEmpty()) View.GONE else View.VISIBLE

         fun updateRecyclerView() {
            val searchHistoryManagerUpdate = SearchHistoryManager(this)
            val searchHistory = searchHistoryManagerUpdate.getSearchHistory().toMutableList()
             historyAdapter.updateData(searchHistory)
        }





        buttonArrowBack.setOnClickListener{
            finish()
        }

        buttonSearchHistory.setOnClickListener {
            searchHistoryManager.clearHistory()
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.visibility = View.GONE
            notInternet.visibility = View.GONE
        }


        inputTextSearch.setSelectAllOnFocus(true)
        inputTextSearch.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility = if(hasFocus && inputTextSearch.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }




        clearButton.setOnClickListener{
            inputTextSearch.setText("")
            hideKeyboard(this, inputTextSearch)
            track.clear()
            updateRecyclerView()
            songsAdapter.notifyDataSetChanged()
            recyclerView.visibility = View.GONE
            notFound.visibility = View.GONE
            searchHistoryLayout.visibility = View.VISIBLE

        }






        fun searchRequest() {
            notFound.visibility = View.GONE
            if (inputTextSearch.text.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE

                interactor.searchTrack(inputTextSearch.text.toString(), object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        runOnUiThread {
                            progressBar.visibility = View.GONE
                            track.clear()
                            track.addAll(foundTracks)
                            songsAdapter.notifyDataSetChanged()

                            recyclerView.visibility = if (foundTracks.isNotEmpty()) View.VISIBLE else View.GONE
                            notFound.visibility = if (foundTracks.isEmpty()) View.VISIBLE else View.GONE
                        }

                    }
                })
            }
        }



        updateButton.setOnClickListener {
            if(inputTextSearch.text.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE

                interactor.searchTrack(inputTextSearch.text.toString(), object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        runOnUiThread {
                            progressBar.visibility = View.GONE
                            track.clear()
                            track.addAll(foundTracks)
                            songsAdapter.notifyDataSetChanged()

                            recyclerView.visibility = if (foundTracks.isNotEmpty()) View.VISIBLE else View.GONE
                            notFound.visibility = if (foundTracks.isEmpty()) View.VISIBLE else View.GONE
                        }
                    }
                })

            }

        }

        val searchRunnable = Runnable { searchRequest() }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

        }



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveSearchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                searchHistoryLayout.visibility = if(inputTextSearch.hasFocus() && s?.isEmpty() == true && historyList.isNotEmpty()) View.VISIBLE else View.GONE
                searchDebounce()
                if (s.isNullOrEmpty()) {
                    updateRecyclerView()
                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    notFound.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    updateRecyclerView()
                    searchHistoryLayout.visibility = if (historyList.isNotEmpty()) View.VISIBLE else View.GONE
                }
            }

        }

        inputTextSearch.addTextChangedListener(textWatcher)

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


    companion object {
        const val VALUE_KEY = "SearchText"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


}