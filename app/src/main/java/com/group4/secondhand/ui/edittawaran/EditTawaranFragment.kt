@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.edittawaran

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentEditTawaranBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.showToastSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTawaranFragment : Fragment() {

    private var _binding: FragmentEditTawaranBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditTawaranViewModel by viewModels()

    companion object {
        const val ID_PRODUK = "idProduk"
        const val FOTO_PRODUK = "fotoProduk"
        const val NAMA_PRODUK = "namaProduk"
        const val HARGA_AWAL_PRODUK = "hargaAwalProduk"
        const val HARGA_TAWAR_PRODUK = "hargaTawarProduk"
        const val FOTO_PENJUAL = "fotoPenjual"
        const val NAMA_PENJUAL = "namaPenjual"
        const val KOTA_PENJUAL = "kotaPenjual"
        const val ACCESS_TOKEN = "accessToken"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditTawaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val idOrder = bundle?.getInt(ID_PRODUK)
        val fotoProduk = bundle?.getString(FOTO_PRODUK)
        val namaProduk = bundle?.getString(NAMA_PRODUK)
        val hargaAwal = bundle?.getString(HARGA_AWAL_PRODUK)
        val hargaTawar = bundle?.getInt(HARGA_TAWAR_PRODUK)
        val fotoPenjual = bundle?.getString(FOTO_PENJUAL)
        val namaPenjual = bundle?.getString(NAMA_PENJUAL)
        val kotaPenjual = bundle?.getString(KOTA_PENJUAL)
        val token = bundle?.getString(ACCESS_TOKEN)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCancelable(false)

        binding.apply {
            if (fotoProduk != null) {
                Glide.with(requireContext())
                    .load(fotoProduk)
                    .centerCrop()
                    .transform(CenterCrop(), RoundedCorners(12))
                    .into(ivProductImage)
            }
            tvNamaProduk.text = namaProduk
            if (hargaAwal != null) {
                tvHargaAwal.text = currency(hargaAwal.toInt())
            }
            if (hargaTawar != null) {
                etHargaTawar.setText(hargaTawar.toString())
            }
            Glide.with(requireContext())
                .load(fotoPenjual)
                .centerCrop()
                .transform(CenterCrop(), RoundedCorners(12))
                .placeholder(R.drawable.default_image)
                .into(ivAvatarPenjual)
            tvNamaPenjual.text = namaPenjual
            tvKotaPenjual.text = kotaPenjual
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.btnEditTawaran.setOnClickListener {
            val newPrice = binding.etHargaTawar.getNumericValue().toInt()
            if (hargaAwal != null) {
                when {
                    newPrice == 0 -> {
                        Toast.makeText(
                            requireContext(),
                            "Harga tawar tidak boleh Kosong!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    newPrice > hargaAwal.toInt() -> {
                        Toast.makeText(requireContext(), "Harga tawar tidak boleh lebih dari harga awal!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        if (token != null) {
                            if (idOrder != null) {
                                viewModel.updateBuyerOrder(token, idOrder, newPrice)
                            }
                        }
                    }
                }
            }
        }

        viewModel.responseUpdate.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    progressDialog.dismiss()
                    when(it.data?.code()){
                        200 -> {
                            showToastSuccess(binding.root, "Berhasih ubah harga tawaran!", resources.getColor(R.color.success))
                            findNavController().popBackStack()
                        }
                        500 -> {
                            AlertDialog.Builder(requireContext())
                                .setMessage(it.message)
                                .setPositiveButton("Ok") { dialog, _ ->
                                    dialog.dismiss()
                                    findNavController().popBackStack()
                                }
                                .show()
                        }
                    }
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .setPositiveButton("Ok") { dialog, _ ->
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

}