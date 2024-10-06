package com.practicum.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.SongsAdapter
import com.practicum.playlistmaker.ui.media.activity.MediaActivity
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel.Companion.getViewModelFactoryForSearch




class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private var saveSearchText = ""
    private val track = ArrayList<Track>()
    private val trackInteractor: TracksInteractor
        get() = Creator.provideTracksInteractor(this)
    private val searchHistoryInteractor: SearchHistoryInteractor
        get() = Creator.provideSearchHistoryInteractor(this)
    private lateinit var historyAdapter: SongsAdapter
    private lateinit var trackAdapter: SongsAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var saveTrack: SaveTrackInteractor


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

        sharedPreferences = getSharedPreferences("SAVE_TRACK", Context.MODE_PRIVATE)
        saveTrack = Creator.provideSaveTrackInteractor(sharedPreferences)



        searchViewModel = ViewModelProvider(
            this,
            getViewModelFactoryForSearch(trackInteractor, searchHistoryInteractor))[SearchViewModel::class.java]


        val handler = Handler(Looper.getMainLooper())


        binding.recycleView.layoutManager = LinearLayoutManager(this)
        trackAdapter = SongsAdapter(track)
        binding.recycleView.adapter = trackAdapter


        val historyList = searchHistoryInteractor.getSearchHistory().toMutableList()
        historyAdapter = SongsAdapter(historyList)
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(this)
        binding.rvSearchHistory.adapter = historyAdapter

        trackAdapter.onClickedTrack = { track -> handlerTrackClick(track) }

        historyAdapter.onClickedTrack = { track -> handlerTrackClick(track) }


        binding.searchHistory.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE



        binding.searchButtonArrowBack.setOnClickListener {
            finish()
        }

        binding.btSearchClear.setOnClickListener {
            searchViewModel.clearHistory()
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistory.visibility = View.GONE
            binding.errors.visibility = View.GONE
        }


        binding.searchEditText.setSelectAllOnFocus(true)
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.visibility =
                if(hasFocus && binding.searchEditText.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }


        binding.searchCloseButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard(this, binding.searchEditText)
            track.clear()
            searchViewModel.updateHistoryAdapter()
            trackAdapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()
            binding.recycleView.visibility = View.GONE
            binding.searchNotFound.visibility = View.GONE
            binding.searchHistory.visibility = View.VISIBLE

        }

        searchViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.pbSearchHistory.visibility = View.VISIBLE
                binding.recycleView.visibility = View.GONE
                binding.searchHistory.visibility = View.GONE
            } else {
                binding.pbSearchHistory.visibility = View.GONE
                binding.recycleView.visibility = View.VISIBLE
            }
        }


        searchViewModel.tracks.observe(this) { tracks ->
            binding.recycleView.visibility = View.VISIBLE
            track.clear()
            track.addAll(tracks)
            trackAdapter.notifyDataSetChanged()
            searchViewModel.updateHistoryAdapter()
        }

        searchViewModel.history.observe(this) { updateHistory ->
            historyAdapter.updateData(updateHistory)
            historyAdapter.notifyDataSetChanged()
        }

        searchViewModel.errorMessageLiveData.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.recycleView.visibility = View.GONE
                binding.searchHistory.visibility = View.GONE
                binding.errors.visibility = View.VISIBLE
            }
        }

        searchViewModel.isNotFoundVisible.observe(this) { isNotFoundVisible ->
            binding.recycleView.visibility = View.GONE
            binding.searchHistory.visibility = View.GONE
            binding.searchNotFound.visibility = if (isNotFoundVisible) View.VISIBLE else View.GONE
        }


        binding.searchUpdateBtt.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchViewModel.search(query)
            }
        }


        val searchRunnable = Runnable { searchViewModel.search(saveSearchText) }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

        }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveSearchText = s.toString()
                if (s?.isEmpty() == true) {
                    binding.searchHistory.visibility = View.VISIBLE
                    binding.recycleView.visibility = View.GONE
                    binding.searchCloseButton.visibility = View.GONE
                } else {
                    binding.searchHistory.visibility = View.GONE
                    binding.searchCloseButton.visibility = View.VISIBLE
                }
                searchDebounce()
                searchViewModel.updateHistoryAdapter()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        binding.searchEditText.addTextChangedListener(textWatcher)



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


    private fun handlerTrackClick (track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
        searchViewModel.updateHistoryAdapter()
        saveTrack.saveTrack(track)
        val intent = Intent(this,MediaActivity::class.java).apply {
            putExtra(SAVE_TRACK, track)
        }
        startActivity(intent)

    }



    companion object {
        const val SAVE_TRACK = "track"
        const val SAVE_ACTIVITY = "saveActivity"
        const val REQUEST_CODE = 1
        const val VALUE_KEY = "SearchText"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


}