package com.practicum.playlistmaker.presentation.ui

import com.practicum.playlistmaker.data.SearchHistoryRepositoryImpl
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
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {
    private var saveSearchText = ""
    private lateinit var inputTextSearch: EditText

    private val track = ArrayList<Track>()

    private val trackInteractor: TracksInteractor
        get() = Creator.provideTracksInteractor(this)

    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var historyAdapter: SongsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)

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
        val songsAdapter = SongsAdapter(track, this)
        recyclerView.adapter = songsAdapter


        val historyList = searchHistoryInteractor.getSearchHistory().toMutableList()
        historyAdapter = SongsAdapter(historyList, this)
        recyclerViewForHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewForHistory.adapter = historyAdapter



        searchHistoryLayout.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE

        fun updateRecyclerView() {
            val searchHistoryRepositoryImplUpdate = SearchHistoryRepositoryImpl(this)
            val searchHistory = searchHistoryRepositoryImplUpdate.getSearchHistory().toMutableList()
            historyAdapter.updateData(searchHistory)
        }



        buttonArrowBack.setOnClickListener {
            finish()
        }

        buttonSearchHistory.setOnClickListener {
            searchHistoryInteractor.clearSearchHistory()
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            searchHistoryLayout.visibility = View.GONE
            notInternet.visibility = View.GONE
        }


        inputTextSearch.setSelectAllOnFocus(true)
        inputTextSearch.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility =
                if (hasFocus && inputTextSearch.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }




        clearButton.setOnClickListener {
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
                trackInteractor.searchTrack(
                    inputTextSearch.text.toString(),
                    object : TracksInteractor.TracksConsumer {
                        override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                            runOnUiThread {
                                progressBar.visibility = View.GONE
                                if (foundTracks != null) {
                                    track.clear()
                                    track.addAll(foundTracks)
                                    songsAdapter.notifyDataSetChanged()
                                    recyclerView.visibility = View.VISIBLE
                                }
                                if (errorMessage != null) {
                                    notInternet.visibility = View.VISIBLE
                                } else if (track.isEmpty()) {
                                    recyclerView.visibility = View.GONE
                                    searchHistoryLayout.visibility = View.GONE
                                    notFound.visibility = View.VISIBLE
                                }

                            }

                        }
                    })

            }
            if (inputTextSearch.text.isEmpty()) {
                updateRecyclerView()
                searchHistoryLayout.visibility = View.VISIBLE
            }
        }




        updateButton.setOnClickListener {
            if (inputTextSearch.text.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE

                trackInteractor.searchTrack(
                    inputTextSearch.text.toString(),
                    object : TracksInteractor.TracksConsumer {
                        override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                            runOnUiThread {
                                progressBar.visibility = View.GONE
                                if (foundTracks != null) {
                                    track.clear()
                                    track.addAll(foundTracks)
                                    songsAdapter.notifyDataSetChanged()
                                    recyclerView.visibility = View.VISIBLE
                                }
                                if (errorMessage != null) {
                                    notInternet.visibility = View.VISIBLE
                                } else if (foundTracks != null) {
                                    if (foundTracks.isEmpty()) {
                                        notFound.visibility = View.VISIBLE
                                    }
                                }

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
                searchHistoryLayout.visibility =
                    if (inputTextSearch.hasFocus() && s?.isEmpty() == true && historyList.isNotEmpty()) View.VISIBLE else View.GONE
                searchDebounce()
                if (s.isNullOrEmpty()) {
                    updateRecyclerView()
                }
                updateRecyclerView()

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    notFound.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    searchHistoryLayout.visibility =
                        if (historyList.isNotEmpty()) View.VISIBLE else View.GONE
                    updateRecyclerView()
                }
            }

        }

        inputTextSearch.addTextChangedListener(textWatcher)



    }

    fun updateHistoryAdapter() {
        val updatedHistory = searchHistoryInteractor.getSearchHistory().toMutableList()
        historyAdapter.updateData(updatedHistory)
        historyAdapter.notifyDataSetChanged()
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
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