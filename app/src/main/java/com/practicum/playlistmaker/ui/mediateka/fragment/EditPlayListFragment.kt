package com.practicum.playlistmaker.ui.mediateka.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.mediateka.view_model.EditPlayListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlayListFragment : NewPlayListFragment() {

    override val viewModel by viewModel<EditPlayListViewModel>()
    private var playListName: String = ""
    private var playListDescription: String = ""
    private var selectedImageUri: String? = null
    private val args: EditPlayListFragmentArgs by navArgs()
    private val requester = PermissionRequester.instance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playList = args.playList

        if (playList != null) {

            viewModel.loadPlaylist(playList.id!!)
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                val imageName = viewModel.saveToStorage(it)
                val savedUri = viewModel.getTrackToStorage(imageName)
                binding.playListPicture.setImageURI(savedUri)
                selectedImageUri = savedUri.toString()
            }
        }


        binding.playListPicture.setOnClickListener {
            lifecycleScope.launch {
                requester.request(android.Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                        is PermissionResult.Denied.DeniedPermanently -> {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.data =
                                Uri.fromParts("package", requireContext().packageName, null)
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

        binding.btnArrayBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.playlistData.observe(viewLifecycleOwner) { playlist ->
            binding.playListName.setText(playlist.playListName)
            binding.playListDescription.setText(playlist.playListDescription)
            playlist.urlImager?.let {
                binding.playListPicture.setImageURI(Uri.parse(it))
                selectedImageUri = it
            }
            binding.btnCreatePlayList.text = getString(R.string.save)
        }

        binding.btnCreatePlayList.setOnClickListener {
            playListName = binding.playListName.text.toString()
            playListDescription = binding.playListDescription.text.toString()
            viewModel.savePlayList(
                name = playListName,
                description = playListDescription,
                imageUri = selectedImageUri
            )
            findNavController().navigateUp()
        }

    }

}


