package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlayListBinding
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.ui.mediateka.view_model.NewPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlayListFragment : Fragment() {

    private var _binding : FragmentNewPlayListBinding? = null
    private val binding : FragmentNewPlayListBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val viewModel by viewModel<NewPlayListViewModel> ()
    private var playListName : String = ""
    private var playListDescription : String = ""
    private var selectedImageUri: String? = null

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

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if(it != null) {
                selectedImageUri = it.toString()
                binding.playListPicture.setImageURI(it)
            }
        }

        binding.playListPicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") {dialog, which -> }
                .setNegativeButton("Завершить"){dialog, which ->
                requireActivity().supportFragmentManager.popBackStack()
            }

        binding.btnArrayBack.setOnClickListener {
            confirmDialog.show()
        }


        binding.btnCreatePlayList.setOnClickListener {

            playListName = binding.playListName.text.toString()
            playListDescription = binding.playListDescription.text.toString()

            val playList = PlayList(
                id = null,
                playListName,
                playListDescription,
                selectedImageUri,
                tracksIds = null,
                tracksCount = null,
            )


            viewModel.createPlayList(playList)
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(),"Плейлист ${playListName} успешно создан", Toast.LENGTH_SHORT).show()
        }


    }


}