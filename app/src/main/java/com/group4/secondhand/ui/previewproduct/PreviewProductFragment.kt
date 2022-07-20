@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.previewproduct

import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentPreviewProductBinding
import com.group4.secondhand.ui.*
import com.group4.secondhand.ui.home.HomeFragment
import com.group4.secondhand.ui.jual.JualFragment.Companion.ADDRESS_USER_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.DESKRIPSI_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.HARGA_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.IMAGE_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.IMAGE_USER_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.KATEGORI_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.NAMA_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.NAME_USER_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.TOKEN_USER_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewProductFragment : Fragment() {

    private var _binding: FragmentPreviewProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PreviewViewModel by viewModels()
    private lateinit var convertBasePrice: String

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
        _binding = FragmentPreviewProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.statusBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, HomeFragment.result
        )
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        val bundle = arguments
        val namaProduk = bundle?.getString(NAMA_PRODUK_KEY)
        val hargaProduk = bundle?.getString(HARGA_PRODUK_KEY)
        val deskripsiProduk = bundle?.getString(DESKRIPSI_PRODUK_KEY)
        val imageProduk = bundle?.getString(IMAGE_PRODUK_KEY)
        val kategoriProduk = bundle?.getString(KATEGORI_PRODUK_KEY)
        val userName = bundle?.getString(NAME_USER_KEY)
        val userAddress = bundle?.getString(ADDRESS_USER_KEY)
        val userImage = bundle?.getString(IMAGE_USER_KEY)
        val token = bundle?.getString(TOKEN_USER_KEY)

        binding.tvProdukName.text = namaProduk
        convertBasePrice = currency(hargaProduk.toString().toInt())
        binding.tvProdukHarga.text = convertBasePrice
        binding.tvDeskripsiProduk.text = deskripsiProduk
        binding.tvProdukKategori.text = kategoriProduk
        binding.tvNamaPenjual.text = userName
        binding.tvKotaPenjual.text = userAddress
        Glide.with(requireContext())
            .load(userImage)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivAvatarPenjual)
        Glide.with(requireContext())
            .load(imageProduk)
            .centerCrop()
            .into(binding.imageProduk)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnTerbitkan.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Pesan")
                .setMessage("Terbitkan Produk?")
                .setPositiveButton("Iya") { positiveButton, _ ->
                    if (token != null) {
                        viewModel.uploadProduk(
                            token,
                            namaProduk.toString(),
                            deskripsiProduk.toString(),
                            hargaProduk.toString(),
                            listCategoryId, //elektronik
                            userAddress.toString(),
                            uriToFile(Uri.parse(imageProduk), requireContext())
                        )
                    }
                    positiveButton.dismiss()
                }
                .setNegativeButton("Batal") { negativeButton, _ ->
                    negativeButton.dismiss()
                }
                .show()
        }
        viewModel.uploadResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    when(it.data?.code()){
                        201 -> {
                            listCategory.clear()
                            listCategoryId.clear()
                            Handler().postDelayed({
                                findNavController().navigate(R.id.action_previewProductFragment_to_daftarJualFragment)
                                showToastSuccess(
                                    binding.root,
                                    "Produk berhasil di terbitkan.",
                                    resources.getColor(R.color.success)
                                )
                            }, 1000)
                        }
                        400 -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Maksimal Upload hanya 5 Produk")
                                .setPositiveButton("Iya") { positiveButton, _ ->
                                    positiveButton.dismiss()
                                }
                                .show()
                        }
                    }
                    progressDialog.dismiss()
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage(it.data?.message() ?: "error")
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
}