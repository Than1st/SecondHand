package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.RequestBuyerOrder
import com.group4.secondhand.databinding.FragmentBottomSheetDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDetailFragment(
    productId: Int,
    namaProduk: String,
    hargaProduk: String,
    gambarProduk: String,
    private val refreshButton: () -> Unit
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
            .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
            .into(binding.ivProductImage)

        binding.btnKirimHargaTawar.setOnClickListener {
            if (binding.etHargaTawar.text.isNullOrEmpty()) {
                binding.hargaTawarContainer.error = "Input tawar harga tidak boleh kosong"
            }else if(binding.etHargaTawar.text.toString().toInt() >= hargaProduk.toInt()){
                binding.hargaTawarContainer.error = "Tawaranmu lebih tinggi dari harga produk"
            }else {
                detailViewModel.getToken()
                detailViewModel.token.observe(viewLifecycleOwner) {
                    val inputHargaTawar = binding.etHargaTawar.text
                    val requestHargaTawar =
                        RequestBuyerOrder(produkId, inputHargaTawar.toString().toInt())
                    detailViewModel.buyerOrder(it.data.toString(), requestHargaTawar)
                }
            }
        }

        detailViewModel.buyerOrder.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    when (it.data?.code()) {
                        201 -> {
                            Toast.makeText(
                                context,
                                "Harga Tawarmu berhasil dikirim ke Penjual",
                                Toast.LENGTH_SHORT
                            ).show()
                            refreshButton.invoke()
                        }
                        400 -> {
                            Toast.makeText(
                                context,
                                "Produk sudah terlalu banyak yang menawar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    dismiss()
                }
            }
        }
    }
}