package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.RequestBuyerOrder
import com.group4.secondhand.databinding.FragmentBottomSheetDetailBinding
import com.group4.secondhand.databinding.FragmentDetailBinding
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCTNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDetailFragment(
    productId: Int,
    namaProduk: String,
    hargaProduk: String,
    gambarProduk: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDetailBinding? = null
    private val binding get() = _binding!!
    private val namaProduk = namaProduk
    private val hargaProduk = hargaProduk
    private val gambarProduk = gambarProduk
    private val produkId = productId
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNamaProduk.text = namaProduk
        binding.tvHargaAwal.text = hargaProduk
        Glide.with(binding.root)
            .load(gambarProduk)
            .apply(RequestOptions.bitmapTransform( RoundedCorners(24)))
            .into(binding.ivProductImage)

        binding.btnSayaTertarikNego.setOnClickListener {
            detailViewModel.getToken()
        }
        detailViewModel.token.observe(viewLifecycleOwner) {
            val inputHargaTawar = binding.etHargaTawar.text
            val requestHargaTawar = RequestBuyerOrder(produkId, inputHargaTawar.toString().toInt())

            Toast.makeText(context, "Harga Tawarmu berhasil dikirim ke Penjual", Toast.LENGTH_SHORT).show()
            detailViewModel.buyerOrder(it, requestHargaTawar)
            dismiss()
            val coba  = FragmentDetailBinding.inflate(layoutInflater)
            coba.btnSayaTertarikNego.isEnabled = false
            binding.btnSayaTertarikNego.backgroundTintList = requireContext().getColorStateList(R.color.dark_grey)
        }
    }

 fun coba() {
 }
}