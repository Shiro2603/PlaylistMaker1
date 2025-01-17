package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import android.view.View
import com.practicum.playlistmaker.ui.mediateka.view_model.EditPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlayListFragment : NewPlayListFragment() {

    private val viewModel by viewModel<EditPlayListViewModel> ()
    private var playListName : String = ""
    private var playListDescription : String = ""
    private var selectedImageUri: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}