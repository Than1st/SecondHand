@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.riwayatpenawaran

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.model.ResponseGetBuyerOrder
import com.group4.secondhand.databinding.FragmentRiwayatPenawaranBinding
import com.group4.secondhand.databinding.MenuRiwayatPenawaranBinding
import com.group4.secondhand.ui.akun.AkunFragment
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.ACCESS_TOKEN
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.FOTO_PRODUK
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.HARGA_AWAL_PRODUK
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.HARGA_TAWAR_PRODUK
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.KOTA_PENJUAL
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.NAMA_PENJUAL
import com.group4.secondhand.ui.edittawaran.EditTawaranFragment.Companion.NAMA_PRODUK
import com.group4.secondhand.ui.home.HomeFragment
import com.group4.secondhand.ui.showToastSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiwayatPenawaranFragment : Fragment() {

    private var _binding: FragmentRiwayatPenawaranBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RiwayatPenawaranViewModel by viewModels()
    private val bundleOrder = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatPenawaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val token = bundle?.getString(AkunFragment.USER_TOKEN)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCancelable(false)
        if (token != null) {
            viewModel.getRiwayatPenawaran(token)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getRiwayat.observe(viewLifecycleOwner){
            when (it.status){
                SUCCESS -> {
                    if (it.data != null){
                        val adapter = RiwayatPenawaranAdapter(object: RiwayatPenawaranAdapter.OnClickListener {
                            override fun onClickItem(data: ResponseGetBuyerOrder) {
                                if (data.status == "pending"){
                                    val dialogBinding = MenuRiwayatPenawaranBinding.inflate(LayoutInflater.from(requireContext()))
                                    val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                    dialogBuilder.setView(dialogBinding.root)
                                    val dialog = dialogBuilder.create()
                                    dialog.setCancelable(true)
                                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                    dialogBinding.listLiatProduk.setOnClickListener {
                                        bundleOrder.putInt(HomeFragment.PRODUCT_ID, data.productId)
                                        findNavController().navigate(R.id.action_riwayatPenawaranFragment_to_detailFragment, bundleOrder)
                                        dialog.dismiss()
                                    }
                                    dialogBinding.listEditTawaran.setOnClickListener {
                                        val bundleEdit = Bundle()
                                        bundleEdit.putString(FOTO_PRODUK, data.product.imageUrl)
                                        bundleEdit.putString(NAMA_PRODUK, data.product.name)
                                        bundleEdit.putString(HARGA_AWAL_PRODUK, data.basePrice.toString())
                                        bundleEdit.putInt(HARGA_TAWAR_PRODUK, data.price)
                                        bundleEdit.putString(NAMA_PENJUAL, data.product.user.fullName)
                                        bundleEdit.putString(KOTA_PENJUAL, data.user.city)
                                        bundleEdit.putString(ACCESS_TOKEN, token)
                                        findNavController().navigate(R.id.action_riwayatPenawaranFragment_to_editTawaranFragment, bundleEdit)
                                        dialog.dismiss()
                                    }
                                    dialogBinding.listHapusTawaran.setOnClickListener {
                                        if (token != null) {
                                            AlertDialog.Builder(requireContext())
                                                .setTitle("Pesan")
                                                .setMessage("Yakin batalkan tawaran?")
                                                .setPositiveButton("Yakin"){ positif, _ ->
                                                    positif.dismiss()
                                                    viewModel.deleteBuyerOrder(token, data.id)
                                                    dialog.dismiss()
                                                }
                                                .setNegativeButton("Tidak"){ negatif, _ ->
                                                    negatif.dismiss()
                                                }
                                                .show()
                                        }
                                    }
                                    dialogBinding.listClose.setOnClickListener {
                                        dialog.dismiss()
                                    }
                                    dialog.show()
                                }
                            }
                        })
                        if (it.data.isNotEmpty()){
                            adapter.submitData(it.data)
                            binding.rvRiwayat.adapter = adapter
                            progressDialog.dismiss()
                        } else {
                            adapter.submitData(it.data)
                            binding.rvRiwayat.adapter = adapter
                            binding.emptyRiwayat.visibility = View.VISIBLE
                            progressDialog.dismiss()
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

        viewModel.deleteOrder.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    showToastSuccess(binding.root, "Berhasil Hapus Tawaran", resources.getColor(R.color.success))
                    Handler().postDelayed({
                        if (token != null) {
                            progressDialog.dismiss()
                            viewModel.getRiwayatPenawaran(token)
                        }
                    }, 1000)
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
    }

}