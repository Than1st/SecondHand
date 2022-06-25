package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentDetailBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.home.HomeFragment.Companion.BASEPRICE
import com.group4.secondhand.ui.home.HomeFragment.Companion.DESCRIPTION
import com.group4.secondhand.ui.home.HomeFragment.Companion.IMAGEURL
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCTNAME
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCT_ID
import com.group4.secondhand.ui.home.HomeFragment.Companion.result
import com.group4.secondhand.ui.jual.BottomSheetPilihCategoryFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var convertBasePrice : String

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

        binding.tvDeskripsi.setOnClickListener {
//            var bottomFragment = BottomSheetDetailFragment()
//            bottomFragment.show(getParentFragmentManager() ,"Tag")

//            val bottomFragment = BottomSheetInfoPenawarFragment()
//            bottomFragment.show(getParentFragmentManager() ,"Tag")
//
//            val bottomFragment = BottomSheetStatusProdukFragment()
//            bottomFragment.show(getParentFragmentManager(), "Tag")

            val bottomFragment = BottomSheetPilihCategoryFragment()
            bottomFragment.show(getParentFragmentManager(), "Tag")


        }
        val bundle = arguments
        val productId = bundle?.getInt(PRODUCT_ID)
        val productName = bundle?.getString(PRODUCTNAME)
        val basePrice = bundle?.getInt(BASEPRICE)
        val productDescription = bundle?.getString(DESCRIPTION)
        val imageURL = bundle?.getString(IMAGEURL)
        if (basePrice != null) {
            convertBasePrice = currency(basePrice)
            binding.tvProdukHarga.text = convertBasePrice
        }


        Glide.with(binding.imageView)
            .load(imageURL)
            .into(binding.imageView)
        binding.tvProdukName.text = productName
        binding.tvDeskripsiProduk.text = productDescription


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSayaTertarikNego.setOnClickListener {
            val bottomFragment = BottomSheetDetailFragment(productId!!,productName.toString(),convertBasePrice,imageURL.toString())
            bottomFragment.show(parentFragmentManager, "Tag")
        }

    }

}