package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentBottomSheetDetailBinding
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCTNAME

class BottomSheetDetailFragment(namaProduk : String, hargaProduk:String, gambarProduk:String) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDetailBinding? =null
    private val binding get() = _binding!!
    private val namaProduk = namaProduk
    private val hargaProduk = hargaProduk
    private val gambarProduk = gambarProduk
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNamaProduk.text = namaProduk
        binding.tvHargaAwal.text = hargaProduk
        Glide.with(binding.root)
            .load(gambarProduk)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.ivProductImage)


    }
}