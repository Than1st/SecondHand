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
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_TOKEN
import com.group4.secondhand.ui.showToastSuccess
import com.group4.secondhand.ui.striketroughtText
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
        progressDialog.setCancelable(false)
        val bundlePenawar = arguments
        val idOrder = bundlePenawar?.getInt(ORDER_ID)
        val token = bundlePenawar?.getString(USER_TOKEN)
        var status: String
        if (idOrder != null) {
            if (token != null) {
                viewModel.getOrderById(idOrder, token)
            }
        }
        viewModel.responseOrder.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    if(it.data != null){
                        it.data.let { data ->
                            binding.apply {
                                tvNamaPenawar.text = data.user.fullName
                                tvKotaPenawar.text = data.user.city
                                Glide.with(requireContext())
                                    .load("")
                                    .placeholder(R.drawable.default_image)
                                    .transform(CenterCrop(), RoundedCorners(12))
                                    .into(ivAvatarPenawar)
                                tvNamaProduk.text = data.productName
                                tvHargaAwal.apply {
                                    text = striketroughtText(this, currency(data.basePrice.toInt()))

                                }
                                tvHargaDitawar.text = getString(R.string.ditawar, currency(data.price))
                                tvTanggal.text = convertDate(data.createdAt)
                                Glide.with(requireContext())
                                    .load(data.product.imageUrl)
                                    .transform(CenterCrop(), RoundedCorners(12))
                                    .into(ivProductImage)

                                when (data.status) {
                                    "accepted" -> {
                                        btnGroup.visibility = View.GONE
                                        btnGroupAccepted.visibility = View.VISIBLE
                                        if (data.product.status == "seller"){
                                            btnGroupAccepted.visibility = View.GONE
                                            tvPesan.visibility = View.VISIBLE
                                            tvPesan.text = getString(R.string.produk_sudah_terjual)
                                        }
                                    }
                                    "declined" -> {
                                        btnGroup.visibility = View.GONE
                                        btnGroupAccepted.visibility = View.GONE
                                        tvPesan.visibility = View.VISIBLE
                                        tvPesan.text = getString(R.string.tawaran_sudah_di_tolak)
                                    }
                                    "pending"->{
                                        btnGroupAccepted.visibility = View.GONE
                                        if (data.product.status == "seller"){
                                            btnGroup.visibility = View.GONE
                                            btnGroupAccepted.visibility = View.GONE
                                            tvPesan.visibility = View.VISIBLE
                                            tvPesan.text = getString(R.string.produk_sudah_laku)
                                        } else if (data.product.status == "sold"){
                                            btnGroupAccepted.visibility = View.GONE
                                        }
                                    }
                                }

                                btnTolak.setOnClickListener {
                                    if (data.status == "pending" && data.product.status == "sold") {
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
                                                    viewModel.updateOrderStatus(token, idOrder, body)
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
                                    if (data.status == "pending" && data.product.status == "sold") {
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
                                                    viewModel.updateOrderStatus(token, idOrder, body)
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
                                        data.user.fullName,
                                        data.user.city,
                                        data.user.phoneNumber,
                                        "",
                                        data.product.name,
                                        data.basePrice.toInt(),
                                        data.price,
                                        data.product.imageUrl
                                    )
                                    bottomFragment.show(parentFragmentManager, "Tag")
                                }

                                btnStatus.setOnClickListener {
                                    val bottomFragment = BottomSheetStatusFragment(
                                        token.toString(),
                                        data.productId
                                    )
                                    bottomFragment.show(parentFragmentManager, "Tag")
                                }

                                viewModel.responseApproveOrder.observe(viewLifecycleOwner){ resApprove ->
                                    when(resApprove.status){
                                        SUCCESS -> {
                                            progressDialog.dismiss()
                                            if (resApprove.data != null){
                                                if (resApprove.data.status == "accepted"){
                                                    binding.btnGroup.visibility = View.GONE
                                                    binding.btnGroupAccepted.visibility = View.VISIBLE
                                                    val bottomFragment = BottomSheetInfoPenawarFragment(
                                                        data.user.fullName,
                                                        data.user.city,
                                                        data.user.phoneNumber,
                                                        "",
                                                        data.product.name,
                                                        data.basePrice.toInt(),
                                                        data.price,
                                                        data.product.imageUrl
                                                    )
                                                    bottomFragment.show(parentFragmentManager, "Tag")
                                                } else {
                                                    showToastSuccess(binding.root, "Tawaran ${data.user.fullName} di Tolak!", resources.getColor(R.color.success))
                                                    binding.btnGroupAccepted.visibility = View.GONE
                                                    binding.btnGroup.visibility = View.GONE
                                                    binding.tvPesan.visibility = View.VISIBLE
                                                    binding.tvPesan.text = "Tawaran Di Tolak!"
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
                        progressDialog.dismiss()
                    }
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            findNavController().popBackStack()
                        }
                        .show()
                }
                LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }
}