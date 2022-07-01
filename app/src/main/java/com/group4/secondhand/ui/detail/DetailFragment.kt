package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.ResponseBuyerProductById
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.databinding.FragmentDetailBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.home.HomeFragment.Companion.BASEPRICE
import com.group4.secondhand.ui.home.HomeFragment.Companion.DESCRIPTION
import com.group4.secondhand.ui.home.HomeFragment.Companion.IMAGEURL
import com.group4.secondhand.ui.home.HomeFragment.Companion.KATEGORI
import com.group4.secondhand.ui.home.HomeFragment.Companion.LOCATION
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCTNAME
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCT_ID
import com.group4.secondhand.ui.home.HomeFragment.Companion.result
import com.group4.secondhand.ui.home.HomeViewModel
import com.group4.secondhand.ui.jual.BottomSheetPilihCategoryFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var convertBasePrice: String

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.statusBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, result
        )

//        binding.tvDeskripsi.setOnClickListener {
//            var bottomFragment = BottomSheetDetailFragment()
//            bottomFragment.show(getParentFragmentManager() ,"Tag")

//            val bottomFragment = BottomSheetInfoPenawarFragment()
//            bottomFragment.show(getParentFragmentManager() ,"Tag")
//
//            val bottomFragment = BottomSheetStatusProdukFragment()
//            bottomFragment.show(getParentFragmentManager(), "Tag")

//            val bottomFragment = BottomSheetPilihCategoryFragment({})
//            bottomFragment.show(getParentFragmentManager(), "Tag")
//        }
        val bundle = arguments
        val productId = bundle?.getInt(PRODUCT_ID)
        val productName = bundle?.getString(PRODUCTNAME)
        val basePrice = bundle?.getInt(BASEPRICE)
        val productDescription = bundle?.getString(DESCRIPTION)
        val imageURL = bundle?.getString(IMAGEURL)

        if (basePrice != null) {
            convertBasePrice = currency(basePrice)
        }

        if (productId != null) {
            detailViewModel.getProdukById(productId)
        }

        detailViewModel.detailProduk.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    when (it.data?.code()) {
                        200 -> if (it.data.body() != null) {

                            binding.tvNamaPenjual.text = it.data.body()?.user?.fullName
                            Glide.with(binding.ivAvatarPenjual)
                                .load(it.data.body()?.user?.imageUrl)
                                .apply(RequestOptions.bitmapTransform( RoundedCorners(24)))
                                .into(binding.ivAvatarPenjual)

                            Glide.with(binding.imageView)
                                .load(it.data?.body()?.imageUrl)
                                .into(binding.imageView)
                            binding.tvProdukName.text = it.data?.body()?.name
                            binding.tvDeskripsiProduk.text = it.data.body()?.description
                            binding.tvKotaPenjual.text = it.data.body()?.location

                            if (it.data.body()?.basePrice != null) {
                                convertBasePrice = currency(it.data.body()?.basePrice!!)
                                binding.tvProdukHarga.text = convertBasePrice
                            }

                            if (it.data.body()?.categories!!.isNotEmpty()) {
                                when {
                                    it.data.body()?.categories!!.size > 2 -> {
                                        binding.tvProdukKategori.text =
                                            "${it.data.body()?.categories!![0].name}, ${it.data.body()?.categories!![1].name}, ${it.data.body()?.categories!![2].name} "
                                    }
                                    it.data.body()?.categories!!.size > 1 -> {
                                        binding.tvProdukKategori.text =
                                            "${it.data.body()?.categories!![0].name}, ${it.data.body()?.categories!![1].name} "
                                    }
                                    else -> {
                                        binding.tvProdukKategori.text =
                                            "${it.data.body()?.categories!![0].name} "
                                    }
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            binding.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            binding.btnSayaTertarikNego.setOnClickListener {
                val bottomFragment = BottomSheetDetailFragment(
                    productId!!,
                    productName.toString(),
                    convertBasePrice,
                    imageURL.toString()
                )
                bottomFragment.coba()
                bottomFragment.show(parentFragmentManager, "Tag")
            }

        }

        detailViewModel.buyerOrder.observe(viewLifecycleOwner){
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.status == "pending"){
                        Toast.makeText(context, "${it.data.status}", Toast.LENGTH_SHORT).show()
                        binding.btnSayaTertarikNego.isEnabled = false
                        binding.btnSayaTertarikNego.backgroundTintList = requireContext().getColorStateList(R.color.dark_grey)
                    }

                }
            }
        }

    }
}