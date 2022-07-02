package com.group4.secondhand.ui.detail

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.databinding.FragmentDetailBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.home.HomeFragment.Companion.BASEPRICE
import com.group4.secondhand.ui.home.HomeFragment.Companion.DESCRIPTION
import com.group4.secondhand.ui.home.HomeFragment.Companion.IMAGEURL
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCTNAME
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCT_ID
import com.group4.secondhand.ui.home.HomeFragment.Companion.result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment() : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var convertBasePrice: String
    private var isBid = false
    private var token = ""

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
        val pd = ProgressDialog(requireContext())
        detailViewModel.getToken()
        detailViewModel.token.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data != DEFAULT_TOKEN && it.data != null) {
                        token = it.data
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Pesan")
                            .setMessage("Anda Belom Masuk")
                            .setPositiveButton("Login") { dialogP, _ ->
                                findNavController().navigate(R.id.action_detailFragment_to_loginCompose)
                                dialogP.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialogN, _ ->
                                findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                                dialogN.dismiss()
                            }
                            .setCancelable(false)
                            .show()
                    }
                }
                ERROR -> {
                    pd.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
                LOADING -> {
                    pd.setMessage("Please Wait...")
                    pd.show()
                }
            }
        }
        detailViewModel.getBuyerOrder.observe(viewLifecycleOwner) {
            val bundle = arguments
            val productId = bundle?.getInt(PRODUCT_ID)
            for (data in 0 until (it.data?.size ?: 0)) {
                if (it.data?.get(data)?.productId == productId) {
                    isBid = true
                }
            }
            if (isBid) {
                binding.btnSayaTertarikNego.isEnabled = false
                binding.btnSayaTertarikNego.backgroundTintList =
                    requireContext().getColorStateList(R.color.dark_grey)
            }
        }

        val bundle = arguments
        val productId = bundle?.getInt(PRODUCT_ID)
        var productName =""
        var imageURL = ""
        if (productId != null) {
            detailViewModel.getProdukById(productId)
        }

        detailViewModel.detailProduk.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    when (it.data?.code()) {
                        200 -> if (it.data.body() != null) {
                            productName = it.data.body()?.name.toString()
                            imageURL = it.data?.body()?.imageUrl.toString()

                            binding.tvNamaPenjual.text = it.data.body()?.user?.fullName
                            Glide.with(binding.ivAvatarPenjual)
                                .load(it.data.body()?.user?.imageUrl)
                                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
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
                ERROR -> {
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
                    productName,
                    convertBasePrice,
                    imageURL,
                    refreshButton = { detailViewModel.getBuyerOrder(token) }
                )
                bottomFragment.show(parentFragmentManager, "Tag")
            }
        }
    }
}