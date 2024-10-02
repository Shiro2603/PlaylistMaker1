package com.practicum.playlistmaker.ui.search.activity

import com.practicum.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.SongsAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding

    private var saveSearchText = ""


    private val track = ArrayList<Track>()

    private val trackInteractor: TracksInteractor
        get() = Creator.provideTracksInteractor(this)

    private lateinit var searchHistoryInteractor: SearchHistoryInteractor
    private lateinit var historyAdapter: SongsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)


        val handler = Handler(Looper.getMainLooper())


        binding.recycleView.layoutManager = LinearLayoutManager(this)
        val songsAdapter = SongsAdapter(track, this)
        binding.recycleView.adapter = songsAdapter


        val historyList = searchHistoryInteractor.getSearchHistory().toMutableList()
        historyAdapter = SongsAdapter(historyList, this)
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(this)
        binding.rvSearchHistory.adapter = historyAdapter



        binding.rvSearchHistory.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE

        fun updateRecyclerView() {
            val searchHistoryRepositoryImplUpdate = SearchHistoryRepositoryImpl(this)
            val searchHistory = searchHistoryRepositoryImplUpdate.getSearchHistory().toMutableList()
            historyAdapter.updateData(searchHistory)
        }



        binding.searchButtonArrowBack.setOnClickListener {
            finish()
        }

        binding.btSearchClear.setOnClickListener {
            searchHistoryInteractor.clearSearchHistory()
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            binding.rvSearchHistory.visibility = View.GONE
            binding.errors.visibility = View.GONE
        }


        binding.searchEditText.setSelectAllOnFocus(true)
        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            binding.rvSearchHistory.visibility =
                if (hasFocus && binding.searchEditText.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }




        binding.searchCloseButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard(this, binding.searchEditText)
            track.clear()
            updateRecyclerView()
            songsAdapter.notifyDataSetChanged()
            binding.recycleView.visibility = View.GONE
            binding.searchNotFound.visibility = View.GONE
            binding.rvSearchHistory.visibility = View.VISIBLE

        }




        fun searchRequest() {
            binding.searchNotFound.visibility = View.GONE
            if (binding.searchEditText.text.isNotEmpty()) {
                binding.pbSearchHistory.visibility = View.VISIBLE
                trackInteractor.searchTrack(
                    binding.searchEditText.text.toString(),
                    object : TracksInteractor.TracksConsumer {
                        override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                            runOnUiThread {
                                binding.pbSearchHistory.visibility = View.GONE
                                if (foundTracks != null) {
                                    track.clear()
                                    track.addAll(foundTracks)
                                    songsAdapter.notifyDataSetChanged()
                                    binding.recycleView.visibility = View.VISIBLE
                                }
                                if (errorMessage != null) {
                                    binding.errors.visibility = View.VISIBLE
                                } else if (track.isEmpty()) {
                                    binding.recycleView.visibility = View.GONE
                                    binding.rvSearchHistory.visibility = View.GONE
                                    binding.searchNotFound.visibility = View.VISIBLE
                                }

                            }

                        }
                    })

            }
            if (binding.searchEditText.text.isEmpty()) {
                updateRecyclerView()
                binding.rvSearchHistory.visibility = View.VISIBLE
            }
        }




        binding.searchUpdateBtt.setOnClickListener {
            if (binding.searchEditText.text.isNotEmpty()) {
                binding.pbSearchHistory.visibility = View.VISIBLE

                trackInteractor.searchTrack(
                    binding.searchEditText.text.toString(),
                    object : TracksInteractor.TracksConsumer {
                        override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                            runOnUiThread {
                                binding.pbSearchHistory.visibility = View.GONE
                                if (foundTracks != null) {
                                    track.clear()
                                    track.addAll(foundTracks)
                                    songsAdapter.notifyDataSetChanged()
                                    binding.recycleView.visibility = View.VISIBLE
                                }
                                if (errorMessage != null) {
                                    binding.errors.visibility = View.VISIBLE
                                } else if (foundTracks != null) {
                                    if (foundTracks.isEmpty()) {
                                        binding.searchNotFound.visibility = View.VISIBLE
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
                binding.searchCloseButton.visibility = clearButtonVisibility(s)
                binding.searchHistory.visibility =
                    if (binding.searchEditText.hasFocus() && s?.isEmpty() == true && historyList.isNotEmpty()) View.VISIBLE else View.GONE
                searchDebounce()
                if (s.isNullOrEmpty()) {
                    updateRecyclerView()
                }
                updateRecyclerView()

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.searchNotFound.visibility = View.GONE
                    binding.recycleView.visibility = View.GONE
                    binding.searchHistory.visibility =
                        if (historyList.isNotEmpty()) View.VISIBLE else View.GONE
                    updateRecyclerView()
                }
            }

        }

        binding.searchEditText.addTextChangedListener(textWatcher)



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
        binding.searchEditText.setText(saveSearchText)


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