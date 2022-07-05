package com.group4.secondhand.ui.editproduct

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.databinding.FragmentEditProductBinding
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_DESCRIPTION
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_ID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_IMAGE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_NAME
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_PRICE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_CITY
import com.group4.secondhand.ui.jual.BottomSheetPilihCategoryFragment
import com.group4.secondhand.ui.listCategory
import com.group4.secondhand.ui.listCategoryId
import com.group4.secondhand.ui.showToastSuccess
import com.group4.secondhand.ui.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!
    private val editProductViewModel: EditProductViewModel by viewModels()
    private var uri: String = ""
    private var token = ""
    private var lokasi = ""
    private var productId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteProduct()
        updateProduct()
        editProductViewModel.addCategory(listCategory)
        editProductViewModel.categoryList.observe(viewLifecycleOwner){ kat ->
            if (kat.isNotEmpty()){
                var kategori = ""
                for(element in kat){
                    kategori += ", $element"
                }
                binding.etKategori.setText(kategori.drop(2))
            }else{
                binding.etKategori.setText("Pilih Kategori")
            }
        }
        if (arguments != null) {
            lokasi = arguments?.getString(USER_CITY).toString()
            productId = arguments?.getInt(PRODUCT_ID)!!
            binding.apply {
                etNama.setText(arguments?.getString(PRODUCT_NAME))
                etHarga.setText(arguments?.getInt(PRODUCT_PRICE).toString())
                etDeskripsi.setText(arguments?.getString(PRODUCT_DESCRIPTION))
                Glide.with(requireContext())
                    .load(arguments?.getString(PRODUCT_IMAGE))
                    .transform(CenterCrop(), RoundedCorners(12))
                    .into(ivFoto)
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivFoto.setOnClickListener {
            openImagePicker()
        }
        binding.etKategori.setOnClickListener {
            val bottomFragment = BottomSheetPilihCategoryFragment(
                update = {
                    editProductViewModel.addCategory(listCategory)
                })
            bottomFragment.show(parentFragmentManager, "Tag")
        }

        editProductViewModel.getToken()
        editProductViewModel.token.observe(viewLifecycleOwner) {
            token = it
        }

    }

    private fun validation(
        namaProduk: String,
        hargaProduk: String,
        deskripsiProduk: String,
        listCategory: List<Int>
    ): String {
        when {
            namaProduk.isEmpty() -> {
                binding.namaContainer.error = "Nama Produk tidak boleh kosong"
                return "Nama Produk Kosong!"
            }
            hargaProduk.isEmpty() -> {
                binding.hargaContainer.error = "Harga Produk tidak boleh kosong"
                return "Harga Produk Kosong!"
            }
            hargaProduk.toLong() > 2000000000 -> {
                binding.hargaContainer.error = "Harga Produk tidak boleh lebih dari 2M"
                return "Harga Produk Melebihi Batas!"
            }
            deskripsiProduk.isEmpty() -> {
                binding.deskripsiContainer.error = "Nama Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            listCategory.isEmpty() -> {
                binding.kategoriContainer.error = "Kategori produk tidak boleh kosong"
                Toast.makeText(requireContext(), "Kategori Produk Kosong", Toast.LENGTH_SHORT)
                    .show()
                return "Kategori Produk Kosong!"
            }
            else -> {
                return "passed"
            }
        }
    }

    fun resetError() {
        binding.namaContainer.error = null
        binding.hargaContainer.error = null
        binding.kategoriContainer.error = null
        binding.deskripsiContainer.error = null
    }

    private fun updateProduct() {
        val progressDialog = ProgressDialog(requireContext())
        binding.btnUpdate.setOnClickListener {
            resetError()
            val namaProduk = binding.etNama.text.toString()
            val hargaProduk = binding.etHarga.text.toString()
            val deskripsiProduk = binding.etDeskripsi.text.toString()
            var file : File? = null
            val validation = validation(
                namaProduk,
                hargaProduk,
                deskripsiProduk,
                listCategoryId
            )
            if (validation == "passed") {
                if (uri.isNotEmpty()){
                    file = uriToFile(Uri.parse(uri), requireContext())
                }
                editProductViewModel.uploadProduk(
                    token,
                    productId,
                    namaProduk,
                    deskripsiProduk,
                    hargaProduk.toString(),
                    listCategoryId,
                    lokasi,
                    file
                )
            }
        }

        editProductViewModel.updateResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    progressDialog.dismiss()
                    showToastSuccess(binding.root, "Produk berhasil di terbitkan.", resources.getColor(R.color.success))
                    findNavController().popBackStack()
                    listCategory.clear()
                    listCategoryId.clear()
                }
                ERROR -> {
                    progressDialog.dismiss()
                    var message = ""
                    when (it.message) {
                        "HTTP 400 Bad Request" -> {
                            message = "${it.message}"
                        }
                    }
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage(message)
                        .setPositiveButton("Iya") { positiveButton, _ ->
                            positiveButton.dismiss()
                        }
                        .show()
                }
                LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun deleteProduct() {
        binding.btnHapus.setOnClickListener {
            if (token != DEFAULT_TOKEN) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Yakin ingin menghapus produk kamu?")
                    .setPositiveButton("Ya") { dialogP, _ ->
                        editProductViewModel.deleteSellerProduct(
                            token,
                            productId
                        )
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Batal") { dialogN, _ ->
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        val pd = ProgressDialog(requireContext())
        editProductViewModel.delete.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    when (it.data?.code()) {
                        200 -> {
                            Toast.makeText(
                                requireContext(),
                                it.data.body()?.msg,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().popBackStack()
                        }
                        400 -> {
                            Toast.makeText(
                                requireContext(),
                                "Product has been order",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().popBackStack()
                        }
                    }
                    pd.dismiss()
                }
                ERROR -> {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                    findNavController().popBackStack()
                }
                LOADING -> {
                    pd.setMessage("Please Wait...")
                    pd.show()
                }
            }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
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
            )
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }
}