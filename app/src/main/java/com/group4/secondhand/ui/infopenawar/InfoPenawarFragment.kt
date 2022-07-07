@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.infopenawar

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
import com.group4.secondhand.data.model.RequestApproveOrder
import com.group4.secondhand.databinding.FragmentInfoPenawarBinding
import com.group4.secondhand.ui.convertDate
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.ORDER_ID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.ORDER_STATUS
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_BID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_BID_DATE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_IMAGE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_NAME
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_PRICE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_STATUS
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_CITY
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_IMAGE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_NAME
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_TOKEN
import com.group4.secondhand.ui.showToastSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoPenawarFragment : Fragment() {

    private var _binding : FragmentInfoPenawarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoPenawarViewModel by viewModels()

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
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        val bundlePenawar = arguments
        val idOrder = bundlePenawar?.getInt(ORDER_ID)
        val statusOrder = bundlePenawar?.getString(ORDER_STATUS)
        val statusProduct = bundlePenawar?.getString(PRODUCT_STATUS)
        val token = bundlePenawar?.getString(USER_TOKEN)
        val namaPenawar = bundlePenawar?.getString(USER_NAME)
        val kotaPenawar = bundlePenawar?.getString(USER_CITY)
        val gambarPenawar = bundlePenawar?.getString(USER_IMAGE)
        val namaProduk = bundlePenawar?.getString(PRODUCT_NAME)
        val hargaAwalProduk = bundlePenawar?.getString(PRODUCT_PRICE)
        val hargaDitawarProduk = bundlePenawar?.getString(PRODUCT_BID)
        val gambarProduk = bundlePenawar?.getString(PRODUCT_IMAGE)
        var status: String
        binding.apply {
            tvNamaPenawar.text = namaPenawar
            tvKotaPenawar.text = kotaPenawar
            Glide.with(requireContext())
                .load(gambarPenawar)
                .placeholder(R.drawable.default_image)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivAvatarPenawar)
            tvNamaProduk.text = namaProduk
            tvHargaAwalProduk.text = currency(hargaAwalProduk.toString().toInt())
            tvHargaDitawarProduk.text = getString(R.string.ditawar, currency(hargaDitawarProduk.toString().toInt()))
            tvTanggal.text = convertDate(bundlePenawar?.getString(PRODUCT_BID_DATE).toString())
            Glide.with(requireContext())
                .load(gambarProduk)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivProductImage)

            if(statusOrder == "accepted"){
                btnGroup.visibility = View.GONE
                btnGroupAccepted.visibility = View.VISIBLE
            }

            btnTolak.setOnClickListener {
                if (statusOrder == "pending" && statusProduct == "sold") {
                    Toast.makeText(requireContext(), "Product in transaction process", Toast.LENGTH_SHORT).show()
                }else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage("Tolak Tawaran?")
                        .setPositiveButton("Iya") { positive, _ ->
                            status = "declined"
                            val body = RequestApproveOrder(
                                status
                            )
                            if (token != null && idOrder != null) {
                                viewModel.declineOrder(token, idOrder, body)
                                positive.dismiss()
                            }
                        }
                        .setNegativeButton("Tidak") { negative, _ ->
                            negative.dismiss()
                        }
                        .show()
                }
            }

            btnTerima.setOnClickListener {
                if (statusOrder == "pending" && statusProduct == "sold") {
                    Toast.makeText(requireContext(), "Product in transaction process", Toast.LENGTH_SHORT).show()
                }else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage("Terima Tawaran?")
                        .setPositiveButton("Iya") { positive, _ ->
                            status = "accepted"
                            val body = RequestApproveOrder(
                                status
                            )
                            if (token != null && idOrder != null) {
                                viewModel.declineOrder(token, idOrder, body)
                                positive.dismiss()
                            }
                        }
                        .setNegativeButton("Tidak") { negative, _ ->
                            negative.dismiss()
                        }
                        .show()
                }
            }

            btnHubungi.setOnClickListener {
                val bottomFragment = BottomSheetInfoPenawarFragment(
                    namaPenawar.toString(),
                    kotaPenawar.toString(),
                    gambarPenawar.toString(),
                    namaProduk.toString(),
                    hargaAwalProduk.toString().toInt(),
                    hargaDitawarProduk.toString().toInt(),
                    gambarProduk.toString()
                )
                bottomFragment.show(parentFragmentManager, "Tag")
            }

            btnStatus.setOnClickListener {
                val bottomFragment = BottomSheetStatusFragment()
                bottomFragment.show(parentFragmentManager, "Tag")
            }

            viewModel.responseApproveOrder.observe(viewLifecycleOwner){
                when(it.status){
                    SUCCESS -> {
                        progressDialog.dismiss()
                        if (it.data != null){
                            if (it.data.status == "accepted"){
                                binding.btnGroup.visibility = View.GONE
                                binding.btnGroupAccepted.visibility = View.VISIBLE
                                val bottomFragment = BottomSheetInfoPenawarFragment(
                                    namaPenawar.toString(),
                                    kotaPenawar.toString(),
                                    gambarPenawar.toString(),
                                    namaProduk.toString(),
                                    hargaAwalProduk.toString().toInt(),
                                    hargaDitawarProduk.toString().toInt(),
                                    gambarProduk.toString()
                                )
                                bottomFragment.show(parentFragmentManager, "Tag")
                            } else {
                                showToastSuccess(binding.root, "Tawaran $namaPenawar di Tolak!", resources.getColor(R.color.success))
                                findNavController().popBackStack()
                            }
                        }
                    }
                    ERROR -> {
                        progressDialog.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setTitle("Pesan")
                            .setMessage(it.message)
                            .setPositiveButton("Iya") { positiveButton, _ ->
                                positiveButton.dismiss()
                            }
                            .show()
                    }
                    LOADING -> {
                        progressDialog.show()
                    }
                }
            }

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }



}