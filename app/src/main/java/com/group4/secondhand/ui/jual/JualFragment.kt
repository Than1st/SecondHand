@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.jual

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
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
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.databinding.FragmentJualBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class JualFragment : Fragment() {

    private var _binding: FragmentJualBinding? = null
    private val binding get() = _binding!!
    private var uri : String = ""
    private val viewModel: JualViewModel by viewModels()

    companion object{
        const val NAMA_PRODUK_KEY = "namaProduk"
        const val HARGA_PRODUK_KEY = "hargaProduk"
        const val DESKRIPSI_PRODUK_KEY = "deskripsiProduk"
        const val IMAGE_PRODUK_KEY = "imageProduk"
        const val NAME_USER_KEY = "userName"
        const val ADDRESS_USER_KEY = "userAlamat"
        const val IMAGE_USER_KEY = "userImage"
        const val TOKEN_USER_KEY = "userToken"
    }

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
        _binding = FragmentJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        viewModel.getToken()
        viewModel.alreadyLogin.observe(viewLifecycleOwner){
            if(it == DEFAULT_TOKEN){
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Anda Belom Masuk")
                    .setPositiveButton("Login") { dialogP, _ ->
                        findNavController().navigate(R.id.action_jualFragment_to_loginCompose)
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogN, _ ->
                        findNavController().popBackStack()
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
                viewModel.alreadyLogin.removeObservers(viewLifecycleOwner)
            } else {
                bundle.putString(TOKEN_USER_KEY, it)
                viewModel.getUserData(it)
            }
        }

        viewModel.user.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data != null){
                        val kota = it.data.city
                        val alamat = it.data.address
                        val gambar = it.data.imageUrl ?: "noImage"
                        val noHp = it.data.phoneNumber
                        if (kota.isEmpty() || alamat.isEmpty() || gambar == "noImage" || noHp.isEmpty()){
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Lengkapi data terlebih dahulu sebelum Jual Barang")
                                .setPositiveButton("Iya"){ positiveButton, _ ->
                                    val bundleNama = Bundle()
                                    bundleNama.putString(NAME_USER_KEY, it.data.fullName)
                                    findNavController().navigate(R.id.action_jualFragment_to_lengkapiInfoAkunFragment, bundleNama)
                                    positiveButton.dismiss()
                                }
                                .setNegativeButton("Tidak"){ negativeButton, _ ->
                                    findNavController().popBackStack()
                                    negativeButton.dismiss()
                                }
                                .show()
                        } else {
                            bundle.putString(NAME_USER_KEY, it.data.fullName)
                            bundle.putString(ADDRESS_USER_KEY, it.data.address)
                            bundle.putString(IMAGE_USER_KEY, it.data.imageUrl.toString())
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

        binding.btnTerbitkan.setOnClickListener {

        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.ivFoto.setOnClickListener {
            openImagePicker()
        }

        binding.btnPreview.setOnClickListener {
            resetError()
            val namaProduk = binding.etNama.text.toString()
            val hargaProduk = binding.etHarga.text.toString()
            val deskripsiProduk = binding.etDeskripsi.text.toString()
            val validation = validation(
                namaProduk,
                hargaProduk,
                deskripsiProduk,
                uri
            )
            if (validation == "passed"){
                bundle.putString(NAMA_PRODUK_KEY, namaProduk)
                bundle.putString(HARGA_PRODUK_KEY, hargaProduk)
                bundle.putString(DESKRIPSI_PRODUK_KEY, deskripsiProduk)
                bundle.putString(IMAGE_PRODUK_KEY, uri)
                findNavController().navigate(R.id.action_jualFragment_to_previewProductFragment, bundle)
            }
        }
    }

    private fun resetError() {
        binding.namaContainer.error = null
        binding.hargaContainer.error = null
        binding.deskripsiContainer.error = null
    }

    private fun validation(namaProduk: String, hargaProduk: String, deskripsiProduk: String, uriFoto: String): String {
        when {
            namaProduk.isEmpty() -> {
                binding.namaContainer.error = "Nama Produk tidak boleh kosong"
                return "Nama Produk Kosong!"
            }
            hargaProduk.isEmpty() -> {
                binding.hargaContainer.error = "Harga Produk tidak boleh kosong"
                return "Harga Produk Kosong!"
            }
            deskripsiProduk.isEmpty() -> {
                binding.deskripsiContainer.error = "Nama Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            uriFoto.isEmpty() -> {
                Toast.makeText(requireContext(), "Foto Produk Kosong", Toast.LENGTH_SHORT).show()
                return "Foto Produk Kosong!"
            }
            else -> {
                return "passed"
            }
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
                .into(ivFoto)

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