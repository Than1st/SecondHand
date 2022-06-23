package com.group4.secondhand.ui.editakun

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.dhaval2404.imagepicker.ImagePicker
import com.group4.secondhand.R
import com.group4.secondhand.data.model.RequestUpdateUser
import com.group4.secondhand.databinding.FragmentEditAkunBinding
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_ADDRESS
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_CITY
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_EMAIL
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_IMAGE
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_NAME
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_PHONE_NUMBER
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_TOKEN
import com.group4.secondhand.ui.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@AndroidEntryPoint
class EditAkunFragment : Fragment() {
    private var _binding : FragmentEditAkunBinding? = null
    private val binding get() = _binding!!
    private val viewModel : EditAkunViewModel by viewModels()
    private var uri : String = ""
    private var fileImage : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        binding.apply {
            etNama.setText(
                cekNull(bundle?.getString(USER_NAME).toString())
            )
            etEmail.setText(
                cekNull(bundle?.getString(USER_EMAIL).toString())
            )
            etKota.setText(
                cekNull(bundle?.getString(USER_CITY).toString())
            )
            etAlamat.setText(
                cekNull(bundle?.getString(USER_ADDRESS).toString())
            )
            etNoHp.setText(
                cekNull(bundle?.getString(USER_PHONE_NUMBER).toString())
            )
            Glide.with(requireContext())
                .load(bundle?.getString(USER_IMAGE)?:R.drawable.default_image)
                .transform(CenterCrop(), RoundedCorners(8))
                .into(ivProfile)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivProfile.setOnClickListener {
            openImagePicker()
        }
        binding.btnSimpan.setOnClickListener {
            Log.d("cek path", fileImage?.path ?: "gada")
            resetErrors()
            val nama = binding.etNama.text.toString()
            val email = binding.etEmail.text.toString()
            val kota = binding.etKota.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val isValid = validation(nama, email, kota, alamat, noHp)
            if (isValid){
//                val request = RequestUpdateUser(
//                    nama,
//                    email,
//                    noHp,
//                    alamat,
//                    uri,
//                    kota
//                )
                val token = bundle?.getString(USER_TOKEN)
                if (token != null) {
                    val requestFile = fileImage?.asRequestBody("image/*".toMediaTypeOrNull())
                    val image = requestFile?.let { it1 ->
                        MultipartBody.Part.createFormData("image", fileImage?.name ?: "avatar.jpg",
                            it1
                        )
                    }
                    val name = nama.toRequestBody("text/plain".toMediaType())
                    viewModel.updateDataUser(token, image, name)
//                    if(uri != ""){
//                        val file = File(uri)
//                        val filename = file.name
//                        val multipartBody = MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("image", filename, file.asRequestBody())
//                            .build()
//                        viewModel.updateDataUser(token, request, multipartBody)
//                    } else {
//                        val multipartBody = MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .build()
//                        viewModel.updateDataUser(token, request, multipartBody)
//                    }

                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "Berhasil Update Data!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun resetErrors() {
        binding.apply {
            namaContainer.error = ""
            emailContainer.error = ""
            kotaContainer.error = ""
            alamatContainer.error = ""
            noHpContainer.error = ""
        }
    }

    private fun validation(nama: String, email: String, kota: String, alamat: String, nohp: String): Boolean {
        return when {
            nama.isEmpty() -> {
                binding.namaContainer.error = "Nama tidak boleh kosong!"
                false
            }
            email.isEmpty() -> {
                binding.emailContainer.error = "Email tidak boleh kosong!"
                false
            }
            kota.isEmpty() -> {
                binding.kotaContainer.error = "Kota tidak boleh kosong!"
                false
            }
            alamat.isEmpty() -> {
                binding.alamatContainer.error = "Alamat tidak boleh kosong!"
                false
            }
            nohp.isEmpty() -> {
                binding.noHpContainer.error = "Nomor Hp tidak boleh kosong!"
                false
            }
            else -> {
                true
            }
        }
    }

    private fun cekNull(value: String): String{
        return when (value) {
            "no_image" -> {
                ""
            }
            "no_city" -> {
                ""
            }
            "no_address" -> {
                ""
            }
            "no_number" -> {
                ""
            }
            "null" -> {
                ""
            }
            else -> {
                value
            }
        }
    }

    private fun openGallery() {
        activity?.intent?.type = "image/*"
        galleryResult.launch(arrayOf("image/*"))
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
            result?.let {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                fileImage = uriToFile(it, requireContext())
                uri = it.toString()
            }
            if (result != null) {
                loadImage(result)
            }
        }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data
                    uri = fileUri.toString()
                    if (fileUri != null) {
                        fileImage = uriToFile(fileUri, requireContext())
                        loadImage(fileUri)
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

    private fun loadImage(uri: Uri) {
        binding.apply {
            Glide.with(binding.root)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivProfile)

        }
    }

    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop()
            .saveDir(
                File(
                    requireContext().externalCacheDir,
                    "ImagePicker"
                )
            ) //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

}