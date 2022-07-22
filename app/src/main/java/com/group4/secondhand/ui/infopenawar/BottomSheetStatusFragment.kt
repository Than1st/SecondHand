@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.infopenawar

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.model.RequestUpdateStatusProduk
import com.group4.secondhand.databinding.FragmentBottomSheetStatusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetStatusFragment(
    private val token: String,
    private val produkId: Int,
    private val back: (stat : String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InfoPenawarViewModel by viewModels()
    private var status = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetStatusBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCancelable(false)
        binding.btnKirimStatus.setOnClickListener {
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_berhasil_terjual -> {
                    val request = RequestUpdateStatusProduk(
                        "seller"
                    )
                    status = "accepted"
                    viewModel.updateStatusProduk(token, produkId, request)
                }
                R.id.rb_batalkan_transaksi -> {
                    val request = RequestUpdateStatusProduk(
                        "available"
                    )
                    status = "declined"
                    viewModel.updateStatusProduk(token, produkId, request)
                }
            }

        }

        viewModel.responseStatus.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    if(it.data != null){
                        when(it.data.code()){
                            200 ->{
                                Handler().postDelayed({
                                    progressDialog.dismiss()
                                    dismiss()
                                    back(status)
                                },1000)
                            }
                            400 -> {
                                progressDialog.dismiss()
                                AlertDialog.Builder(requireContext())
                                    .setMessage("Produk Tidak Ditemukan!")
                                    .setPositiveButton("Ok") { dialog, _ ->
                                        dialog.dismiss()
                                        findNavController().popBackStack()
                                    }
                                    .show()
                            }
                        }
                    }
//                    viewModel.responseStatus.removeObservers(viewLifecycleOwner)
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

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            val id: Int = binding.radioGroup.checkedRadioButtonId
            if (id != -1) {
                binding.btnKirimStatus.isEnabled = true
                binding.btnKirimStatus.backgroundTintList = requireContext().getColorStateList(R.color.dark_blue)
            }
        }
    }



}

