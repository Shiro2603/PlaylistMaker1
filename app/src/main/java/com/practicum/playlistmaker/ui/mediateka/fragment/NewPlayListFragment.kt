package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlayListBinding
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding


class NewPlayListFragment : Fragment() {

    private var _binding : FragmentNewPlayListBinding? = null
    private val binding : FragmentNewPlayListBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста? Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") {dialog, which -> }
                .setNegativeButton("Нет"){dialog, which ->
                requireActivity().supportFragmentManager.popBackStack()
            }

        binding.btnArrayBack.setOnClickListener {
            confirmDialog.show()
        }



    }


}