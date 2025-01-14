package com.practicum.playlistmaker.ui.mediateka.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlayListBinding
import com.practicum.playlistmaker.ui.mediateka.view_model.NewPlayListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel



class NewPlayListFragment : Fragment() {

    private var _binding : FragmentNewPlayListBinding? = null
    private val binding : FragmentNewPlayListBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val viewModel by viewModel<NewPlayListViewModel> ()
    private var playListName : String = ""
    private var playListDescription : String = ""
    private var selectedImageUri: String? = null
    val requester = PermissionRequester.instance()

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
                val imageName = viewModel.saveToStorage(it)
                val savedUri = viewModel.getTrackToStorage(imageName)
                binding.playListPicture.setImageURI(savedUri)
                selectedImageUri = savedUri.toString()
            }
        }

        binding.playListPicture.setOnClickListener {
            lifecycleScope.launch {
                requester.request(android.Manifest.permission.READ_MEDIA_IMAGES).collect {
                    result ->
                    when(result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                        is PermissionResult.Denied.DeniedPermanently -> {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data = Uri.fromParts("package", requireContext().packageName, null)
                            requireContext().startActivity(intent)
                        }
                        is PermissionResult.Cancelled -> {
                            return@collect
                        }

                        else -> {}
                    }
                }
            }

        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.alertTheme)
            .setTitle(R.string.finishCreatingPlaylist)
            .setMessage(R.string.unsavedLost)
            .setNeutralButton(R.string.cancel) {dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton(R.string.complete){dialog, which ->
                findNavController().navigateUp()
            }



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackNavigation()
        }

        binding.btnArrayBack.setOnClickListener {
            handleBackNavigation()
        }

        binding.playListName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnCreatePlayList.isEnabled = !s.isNullOrBlank()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnCreatePlayList.setOnClickListener {
            playListName = binding.playListName.text.toString()
            playListDescription = binding.playListDescription.text.toString()
            viewModel.savePlayList(
                name = playListName,
                description = playListDescription,
                imageUri = selectedImageUri
            )
            findNavController().navigateUp()
            Toast.makeText(requireContext(),"Плейлист ${playListName} создан", Toast.LENGTH_SHORT).show()
        }

    }

    private fun handleBackNavigation() {
        if (binding.playListName.text!!.isNotBlank() ||
            binding.playListDescription.text!!.isNotBlank() ||
            selectedImageUri != null) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

}