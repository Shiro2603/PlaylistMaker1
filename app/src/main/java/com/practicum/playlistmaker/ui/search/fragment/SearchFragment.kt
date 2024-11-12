package com.practicum.playlistmaker.ui.search.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreenState
import com.practicum.playlistmaker.ui.media.activity.MediaActivity
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private var saveSearchText = ""
    private val track = ArrayList<Track>()
    private val searchViewModel by viewModel<SearchViewModel>()
    private lateinit var historyAdapter: SongsAdapter
    private lateinit var trackAdapter: SongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        trackAdapter = SongsAdapter(track)
        binding.recycleView.adapter = trackAdapter

        val historyList = searchViewModel.getSearchHistory().toMutableList()
        historyAdapter = SongsAdapter(historyList)
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchHistory.adapter = historyAdapter

        trackAdapter.onClickedTrack = { track -> handlerTrackClick(track) }

        historyAdapter.onClickedTrack = { track -> handlerTrackClick(track) }

        binding.searchHistory.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE


        binding.btHistoryListClear.setOnClickListener {
            searchViewModel.clearHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistory.visibility = View.GONE

        }

        binding.searchEditText.setSelectAllOnFocus(true)
        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.searchHistory.visibility =
                if(hasFocus && binding.searchEditText.text.isEmpty() && historyList.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.searchCloseButton.setOnClickListener {
            binding.searchEditText.setText("")
            hideKeyboard(requireContext(), binding.searchEditText)
            track.clear()
            binding.recycleView.visibility = View.GONE
            binding.searchNotFound.visibility = View.GONE
            binding.searchHistory.visibility = if (historyList.isNotEmpty()) View.VISIBLE else View.GONE

        }

        searchViewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchScreenState.Loading -> {
                    binding.pbSearchHistory.visibility = View.VISIBLE
                    binding.recycleView.visibility = View.GONE
                    binding.searchNotFound.visibility = View.GONE
                    binding.errors.visibility = View.GONE
                }
                is SearchScreenState.Content -> {
                    binding.pbSearchHistory.visibility = View.GONE
                    binding.recycleView.visibility = View.VISIBLE
                    binding.searchNotFound.visibility = View.GONE
                    binding.errors.visibility = View.GONE
                    trackAdapter.notifyDataSetChanged()
                    trackAdapter.updateData(state.tracks)
                }
                is SearchScreenState.NotFound -> {
                    binding.pbSearchHistory.visibility = View.GONE
                    binding.recycleView.visibility = View.GONE
                    binding.searchNotFound.visibility = View.VISIBLE
                    binding.errors.visibility = View.GONE
                }
                is SearchScreenState.Error -> {
                    binding.pbSearchHistory.visibility = View.GONE
                    binding.recycleView.visibility = View.GONE
                    binding.searchNotFound.visibility = View.GONE
                    binding.errors.visibility = View.VISIBLE
                }
                is SearchScreenState.History -> {
                    historyAdapter.notifyDataSetChanged()
                    historyAdapter.updateData(state.historyTracks)
                    binding.searchHistory.visibility = if (state.historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
                }
            }
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
                searchDebounce()
                historyAdapter.notifyDataSetChanged()
                searchViewModel.loadSearchHistory()
                if (s?.isEmpty() == true) {
                    binding.searchHistory.visibility = if (historyList.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.recycleView.visibility = View.GONE
                    binding.searchCloseButton.visibility = View.GONE
                    binding.searchNotFound.visibility = View.GONE
                    binding.errors.visibility = View.GONE
                } else {
                    binding.searchHistory.visibility = View.GONE
                    binding.searchCloseButton.visibility = View.VISIBLE
                }
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


    private fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun handlerTrackClick (track: Track) {
        searchViewModel.addTrackToHistory(track)
        searchViewModel.saveTrack(track)
        val intent = Intent(requireContext(), MediaActivity::class.java).apply {
            putExtra(SAVE_TRACK, track)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SAVE_TRACK = "track"
        const val VALUE_KEY = "SearchText"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}