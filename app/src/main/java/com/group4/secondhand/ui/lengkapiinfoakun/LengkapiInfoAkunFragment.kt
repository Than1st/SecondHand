@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.lengkapiinfoakun

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentLengkapiInfoAkunBinding
import com.group4.secondhand.ui.jual.JualFragment.Companion.NAME_USER_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.TOKEN_USER_KEY
import com.group4.secondhand.ui.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class LengkapiInfoAkunFragment : Fragment() {

    private var _binding : FragmentLengkapiInfoAkunBinding? = null
    private val binding get() = _binding!!
    private var uri: String = ""
    private var fileImage: File? = null
    private val viewModel: LengkapiInfoAkunViewModel by viewModels()

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
        _binding = FragmentLengkapiInfoAkunBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        val nama = bundle?.getString(NAME_USER_KEY)
        val token = bundle?.getString(TOKEN_USER_KEY)
        token?.let { viewModel.getUser(it) }
        binding.etNama.setText(nama)
        viewModel.getUser.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    progressDialog.dismiss()
                    val data = it.data
                    binding.apply {
                        if (data != null) {
                            etNama.setText(data.fullName)
                            etKota.setText(data.city)
                            etAlamat.setText(data.address)
                            etNoHp.setText(data.phoneNumber)
                        }
                    }
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .setPositiveButton("Ok"){ dialog, _ ->
                            dialog.dismiss()
                            findNavController().popBackStack()
                        }
                        .show()
                }
                LOADING -> {
                    progressDialog.show()
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSimpan.setOnClickListener {
            resetErrors()
            val name = binding.etNama.text.toString()
            val kota = binding.etKota.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val isValid = validation(name, kota, alamat, noHp, uri)
            if (isValid) {
                if (token != null) {
                    viewModel.updateDataUser(
                        token,
                        fileImage,
                        name,
                        noHp,
                        alamat,
                        kota
                    )
                    progressDialog.show()
//                    Handler().postDelayed({
//                        progressDialog.dismiss()
//                        findNavController().popBackStack()
//                        Toast.makeText(requireContext(), "Berhasil Update Data!", Toast.LENGTH_SHORT)
//                            .show()
//                    }, 1500)
                }
            }
        }
        binding.ivProfile.setOnClickListener {
            openImagePicker()
        }
        viewModel.responseUpdate.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    Handler().postDelayed({
                        progressDialog.dismiss()
                        findNavController().navigate(R.id.action_lengkapiInfoAkunFragment_to_jualFragment)
                    }, 1000)
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .setPositiveButton("Ok"){ dialog, _ ->
                            dialog.dismiss()
                            findNavController().popBackStack()
                        }
                        .show()
                }
                LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun validation(
        nama: String,
        kota: String,
        alamat: String,
        nohp: String,
        uriFoto: String
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
            uriFoto.isEmpty() -> {
                AlertDialog.Builder(requireContext())
                    .setMessage("Foto Profil masih kosong!")
                    .show()
                false
            }
            else -> {
                true
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