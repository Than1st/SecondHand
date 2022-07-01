@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.previewproduct

import android.app.ActionBar
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentPreviewProductBinding
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
import com.group4.secondhand.ui.listCategoryId
import com.group4.secondhand.ui.uriToFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewProductFragment : Fragment() {

    private var _binding: FragmentPreviewProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PreviewViewModel by viewModels()

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
//        viewModel.getToken()
//        viewModel.token.observe(viewLifecycleOwner){
//            viewModel.getDataUser(it)
//        }
//        val progressBar = ProgressDialog(requireContext())
//        viewModel.user.observe(viewLifecycleOwner){
//            when(it.status){
//                SUCCESS -> {
//                    if (it.data != null){
//                        binding.tvNamaPenjual.text = it.data.fullName
//                        binding.tvKotaPenjual.text = it.data.city ?: "Bandung"
//                        Glide.with(requireContext())
//                            .load(it.data.imageUrl ?: R.drawable.default_image)
//                            .transform(CenterCrop(), RoundedCorners(12))
//                            .into(binding.ivAvatarPenjual)
//                        progressBar.dismiss()
//                    }
//                }
//                ERROR -> {
//                    AlertDialog.Builder(requireContext())
//                        .setTitle("Pesan")
//                        .setMessage(it.message)
//                        .show()
//                    progressBar.dismiss()
//                }
//                LOADING -> {
//                    progressBar.setMessage("Please Wait...")
//                    progressBar.show()
//                }
//            }
//        }
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
        binding.tvProdukHarga.text = hargaProduk
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
                    progressDialog.dismiss()
                    showToastSuccess()
                    findNavController().navigate(R.id.action_previewProductFragment_to_daftarJualFragment)
                    Toast.makeText(requireContext(), "Sukses Upload Produk!", Toast.LENGTH_SHORT)
                        .show()
                }
                ERROR -> {
                    progressDialog.dismiss()
                    var message = ""
                    when (it.message) {
                        "HTTP 400 Bad Request" -> {
                            message = "Anda Hanya Bisa Upload Maksimal 5 Produk"
                        }
                    }
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage(message)
                        .setPositiveButton("Iya"){ positiveButton, _ ->
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

    private fun showToastSuccess(){
        val snackBarView = Snackbar.make(binding.root, "Produk berhasil di terbitkan.", Snackbar.LENGTH_INDEFINITE)
        val layoutParams = ActionBar.LayoutParams(snackBarView.view.layoutParams)
        snackBarView.setAction(" ") {
            snackBarView.dismiss()
        }
        val textView = snackBarView.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        textView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_baseline_close, 0)
        textView.compoundDrawablePadding = 16
        layoutParams.gravity = Gravity.TOP
        layoutParams.setMargins(32,150,32,0)
        snackBarView.view.setPadding(24, 16, 0, 16)
        snackBarView.view.setBackgroundColor(resources.getColor(R.color.success))
        snackBarView.view.layoutParams = layoutParams
        snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBarView.show()
    }

}