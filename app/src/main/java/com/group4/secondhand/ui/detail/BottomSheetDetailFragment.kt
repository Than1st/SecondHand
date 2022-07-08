package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.RequestBuyerOrder
import com.group4.secondhand.databinding.FragmentBottomSheetDetailBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.showToastSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDetailFragment(
    productId: Int,
    namaProduk: String,
    hargaProduk: Int,
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
        binding.tvHargaAwal.text = currency(hargaProduk)
        Glide.with(binding.root)
            .load(gambarProduk)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
            .into(binding.ivProductImage)

        binding.btnKirimHargaTawar.setOnClickListener {
            val inputHargaTawar = binding.etHargaTawar.getNumericValue().toInt()
            if (binding.etHargaTawar.text.isNullOrEmpty()) {
                binding.hargaTawarContainer.error = "Input tawar harga tidak boleh kosong"
            }else if(inputHargaTawar >= hargaProduk){
                binding.hargaTawarContainer.error = "Tawaranmu lebih tinggi dari harga produk"
            }else {
                detailViewModel.getToken()
                detailViewModel.token.observe(viewLifecycleOwner) {
                    val inputHargaTawar = binding.etHargaTawar.getNumericValue().toInt()
                    val requestHargaTawar =
                        RequestBuyerOrder(produkId, inputHargaTawar)
                    detailViewModel.buyerOrder(it.data.toString(), requestHargaTawar)
                }
            }
        }

        detailViewModel.buyerOrder.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    when (it.data?.code()) {
                        201 -> {
                            showToastSuccess(binding.root, "Harga tawarmu berhasil dikirim ke penjual", resources.getColor(
                                R.color.success))
                            binding.btnKirimHargaTawar.isEnabled = false
                            binding.btnKirimHargaTawar.backgroundTintList =
                                requireContext().getColorStateList(R.color.dark_grey)

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
                    Handler().postDelayed({
                        dismiss()
                    }, 1500)
                }
            }
        }
    }
}