@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.previewproduct

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentPreviewProductBinding
import com.group4.secondhand.ui.jual.JualFragment.Companion.DESKRIPSI_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.HARGA_PRODUK_KEY
import com.group4.secondhand.ui.jual.JualFragment.Companion.NAMA_PRODUK_KEY
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
        viewModel.getToken()
        viewModel.token.observe(viewLifecycleOwner){
            viewModel.getDataUser(it)
        }
        val progressBar = ProgressDialog(requireContext())
        viewModel.user.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    if (it.data != null){
                        binding.tvNamaPenjual.text = it.data.fullName
                        binding.tvKotaPenjual.text = it.data.city ?: "Bandung"
                        Glide.with(requireContext())
                            .load(it.data.imageUrl ?: R.drawable.default_image)
                            .into(binding.ivAvatarPenjual)
                        progressBar.dismiss()
                    }
                }
                ERROR -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage(it.message)
                        .show()
                    progressBar.dismiss()
                }
                LOADING -> {
                    progressBar.setMessage("Please Wait...")
                    progressBar.show()
                }
            }
        }

        val bundle = arguments
        val namaProduk = bundle?.getString(NAMA_PRODUK_KEY)
        val hargaProduk = bundle?.getString(HARGA_PRODUK_KEY)
        val deskripsiProduk = bundle?.getString(DESKRIPSI_PRODUK_KEY)

        binding.tvProdukName.text = namaProduk
        binding.tvProdukHarga.text = hargaProduk
        binding.tvDeskripsiProduk.text = deskripsiProduk

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}