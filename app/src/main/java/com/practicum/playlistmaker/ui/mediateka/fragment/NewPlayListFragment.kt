package com.practicum.playlistmaker.ui.mediateka.fragment

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.databinding.FragmentNewPlayListBinding
import com.practicum.playlistmaker.ui.mediateka.view_model.NewPlayListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


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
                selectedImageUri = it.toString()
                saveToStorage(it)
                binding.playListPicture.setImageURI(it)
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

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") {dialog, which -> }
                .setNegativeButton("Завершить"){dialog, which ->
                    findNavController().navigateUp()
            }

        binding.btnArrayBack.setOnClickListener {
            binding.btnArrayBack.setOnClickListener {
                if (binding.playListName.text!!.isNotBlank() ||
                    binding.playListDescription.text!!.isNotBlank() ||
                    selectedImageUri != null) {

                    confirmDialog.show()
                } else {

                    findNavController().navigateUp()
                }
            }
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

    private fun saveToStorage(uri: Uri) {

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        if(!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, "first_cover.jpg")

        val inputStream = requireContext().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

}