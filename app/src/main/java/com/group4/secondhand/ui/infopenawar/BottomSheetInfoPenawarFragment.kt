package com.group4.secondhand.ui.infopenawar

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentBottomSheetInfoPenawarBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.striketroughtText

class BottomSheetInfoPenawarFragment(
    private val namaPembeli: String,
    private val kotaPembeli: String,
    private val nomorPembeli: String,
    private val gambarPembeli: String,
    private val namaProduk: String,
    private val hargaProduk: Int,
    private val hargaDitawarProduk: Int,
    private val gambarProduk: String,
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetInfoPenawarBinding? = null
    private val binding: FragmentBottomSheetInfoPenawarBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetInfoPenawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvNamaPembeli.text = namaPembeli
            tvKotaPembeli.text = kotaPembeli
            Glide.with(requireContext())
                .load(gambarPembeli)
                .placeholder(R.drawable.image_profile)
                .centerCrop()
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivAvatarPembeli)
            tvNamaProduk.text = namaProduk
            tvHargaSeller.apply {
                text = striketroughtText(this, currency(hargaProduk))
            }
            tvHargaTawar.text = "Ditawar ${currency(hargaDitawarProduk)}"
            Glide.with(requireContext())
                .load(gambarProduk)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivFotoProduk)

            btnHubungi.setOnClickListener {
                val nomor = number(nomorPembeli)
                val text = "Halo $namaPembeli, Penawaran anda pada produk $namaProduk telah di setujui, mari kita bertemu untuk melihat barang!"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$nomor&text=$text"))
                startActivity(intent)
            }
        }
    }

    private fun number(nomorPembeli:String): String {
        return when {
            nomorPembeli.take(1) == "0" -> {
                var number = nomorPembeli.drop(1)
                number = "62$number"
                number
            }
            nomorPembeli.take(2) == "62" -> {
                nomorPembeli
            }
            else -> {
                "62$nomorPembeli"
            }
        }
    }
}