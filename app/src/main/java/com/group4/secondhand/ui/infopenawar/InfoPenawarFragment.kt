package com.group4.secondhand.ui.infopenawar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentInfoPenawarBinding
import com.group4.secondhand.ui.convertDate
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.ORDER_ID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_BID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_BID_DATE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_IMAGE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_NAME
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_PRICE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_CITY
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_IMAGE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_NAME
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_TOKEN

class InfoPenawarFragment : Fragment() {

    private var _binding : FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoPenawarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundlePenawar = arguments
        val idOrder = bundlePenawar?.getInt(ORDER_ID)
        val token = bundlePenawar?.getString(USER_TOKEN)
        val status = "accepted"
        binding.apply {
            tvNamaPenawar.text = bundlePenawar?.getString(USER_NAME)
            tvKotaPenawar.text = bundlePenawar?.getString(USER_CITY)
            Glide.with(requireContext())
                .load(bundlePenawar?.getString(USER_IMAGE))
                .placeholder(R.drawable.default_image)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivAvatarPenawar)
            tvNamaProduk.text = bundlePenawar?.getString(PRODUCT_NAME)
            tvHargaAwalProduk.text = currency(bundlePenawar?.getString(PRODUCT_PRICE).toString().toInt())
            tvHargaDitawarProduk.text = getString(R.string.ditawar, currency(bundlePenawar?.getString(PRODUCT_BID).toString().toInt()))
            tvTanggal.text = convertDate(bundlePenawar?.getString(PRODUCT_BID_DATE).toString())
            Glide.with(requireContext())
                .load(bundlePenawar?.getString(PRODUCT_IMAGE))
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivProductImage)

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}