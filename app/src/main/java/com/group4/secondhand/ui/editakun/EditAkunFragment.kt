@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.editakun

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.group4.secondhand.databinding.FragmentEditAkunBinding
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_ADDRESS
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_CITY
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_IMAGE
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_NAME
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_PHONE_NUMBER
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_TOKEN
import com.group4.secondhand.ui.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class EditAkunFragment : Fragment() {
    private var _binding: FragmentEditAkunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditAkunViewModel by viewModels()
    private var uri: String = ""
    private var fileImage: File? = null

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
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCancelable(false)
        val bundle = arguments
        binding.apply {
            etNama.setText(
                checkNull(bundle?.getString(USER_NAME).toString())
            )
            etKota.setText(
                checkNull(bundle?.getString(USER_CITY).toString())
            )
            etAlamat.setText(
                checkNull(bundle?.getString(USER_ADDRESS).toString())
            )
            etNoHp.setText(
                checkNull(bundle?.getString(USER_PHONE_NUMBER).toString())
            )
            Glide.with(requireContext())
                .load(bundle?.getString(USER_IMAGE) ?: R.drawable.default_image)
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
            val kota = binding.etKota.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val isValid = validation(nama, kota, alamat, noHp)
            if (isValid) {
                val token = bundle?.getString(USER_TOKEN)
                if (token != null) {

                    viewModel.updateDataUser(
                        token,
                        fileImage,
                        nama,
                        noHp,
                        alamat,
                        kota
                    )
                    progressDialog.show()
                    Handler().postDelayed({
                        progressDialog.dismiss()

                        findNavController().popBackStack()
                        Toast.makeText(requireContext(), "Berhasil Update Data!", Toast.LENGTH_SHORT)
                            .show()
                    }, 1500)
                }
            }
        }
    }

    private fun resetErrors() {
        binding.apply {
            namaContainer.error = ""
            kotaContainer.error = ""
            alamatContainer.error = ""
            noHpContainer.error = ""
        }
    }

    private fun checkNull(value: String?): String {
        return when {
            value.isNullOrEmpty() -> {
                ""
            }
            value == "null" -> {
                ""
            }
            else -> {
                value
            }
        }
    }

    private fun validation(
        nama: String,
        kota: String,
        alamat: String,
        nohp: String
    ): Boolean {
        return when {
            nama.isEmpty() -> {
                binding.namaContainer.error = "Nama tidak boleh kosong!"
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

//    private fun cekNull(value: String): String {
//        return when (value) {
//            "no_image" -> {
//                ""
//            }
//            "no_city" -> {
//                ""
//            }
//            "no_address" -> {
//                ""
//            }
//            "no_number" -> {
//                ""
//            }
//            "null" -> {
//                ""
//            }
//            else -> {
//                value
//            }
//        }
//    }

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