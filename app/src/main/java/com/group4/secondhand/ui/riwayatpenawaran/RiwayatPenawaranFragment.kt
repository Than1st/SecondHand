@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.riwayatpenawaran

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentRiwayatPenawaranBinding
import com.group4.secondhand.data.model.ResponseGetBuyerOrder
import com.group4.secondhand.databinding.MenuRiwayatPenawaranBinding
import com.group4.secondhand.ui.akun.AkunFragment
import com.group4.secondhand.ui.home.HomeFragment
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
                                        Toast.makeText(
                                            requireContext(),
                                            "Anda Mencet edit tawaran",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    dialogBinding.listClose.setOnClickListener {
                                        dialog.dismiss()
                                    }
                                    dialog.show()
                                }
                            }
                        })
                        adapter.submitData(it.data)
                        binding.rvRiwayat.adapter = adapter
                        progressDialog.dismiss()
                    } else {
                        binding.emptyRiwayat.visibility = View.VISIBLE
                        progressDialog.dismiss()
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
    }

}