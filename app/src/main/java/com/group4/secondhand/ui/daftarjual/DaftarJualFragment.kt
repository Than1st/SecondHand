package com.group4.secondhand.ui.daftarjual

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.group4.secondhand.data.api.Status.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.data.model.ResponseSellerProduct
import com.group4.secondhand.databinding.FragmentDaftarJualBinding
import com.group4.secondhand.ui.previewproduct.PreviewProductFragment.Companion.PESAN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaftarJualFragment : Fragment() {
    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!
    private val daftarJualViewModel: DaftarJualViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val success = bundle?.getInt(PESAN)

        if (success == 1){
            binding.alertSuccess.visibility = View.VISIBLE
            Handler().postDelayed({
                binding.alertSuccess.visibility = View.GONE
            }, 3000)
        }

        binding.btnClose.setOnClickListener {
            binding.alertSuccess.visibility = View.GONE
        }

        daftarJualViewModel.getToken()
        daftarJualViewModel.token.observe(viewLifecycleOwner) {
            daftarJualViewModel.getDataUser(it)
            daftarJualViewModel.getProduct(it)
        }
        setSellerName()
        setRecycler()
        getSellerProduct()

    }

    private fun setSellerName() {
        val progressBar = ProgressDialog(requireContext())
        daftarJualViewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data != null) {
                        binding.tvNamaPenjual.text = it.data.fullName
                        binding.tvKotaPenjual.text = it.data.city
                        Glide.with(requireContext())
                            .load(it.data.imageUrl)
                            .transform(CenterCrop(), RoundedCorners(12))
                            .into(binding.ivAvatarPenjual)
                        progressBar.dismiss()
                    }
                }
                ERROR -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage(it.message)
                        .show()
                    progressBar.dismiss()
                }
                LOADING -> {
                    progressBar.setMessage("Please Wait...")
                    progressBar.show()
                }
            }
        }
    }
    private fun setRecycler(){
        binding.btnProduk.setOnClickListener {
            binding.rvProduct.visibility = View.VISIBLE
            binding.rvDiminati.visibility = View.GONE
            binding.rvTerjual.visibility = View.GONE
        }
        binding.btnDiminati.setOnClickListener {
            binding.rvProduct.visibility = View.GONE
            binding.rvDiminati.visibility = View.VISIBLE
            binding.rvTerjual.visibility = View.GONE
        }
        binding.btnTerjual.setOnClickListener {
            binding.rvProduct.visibility = View.GONE
            binding.rvDiminati.visibility = View.GONE
            binding.rvTerjual.visibility = View.VISIBLE
        }
    }
    private fun getSellerProduct(){
        daftarJualViewModel.product.observe(viewLifecycleOwner){
            when(it.status){
                LOADING ->{
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvProduct.visibility = View.GONE
                    binding.rvDiminati.visibility = View.GONE
                    binding.rvTerjual.visibility = View.GONE
                }
                SUCCESS ->{
                    if (it.data != null){
                        val sellerProductAdapter = SellerProductAdapter(object : SellerProductAdapter.OnclickListener{
                            override fun onClickItem(data: ResponseSellerProduct) {
                                Toast.makeText(requireContext(), "go to edit", Toast.LENGTH_SHORT).show()
                            }

                        })
                        sellerProductAdapter.submitData(it.data)
                        binding.rvProduct.adapter = sellerProductAdapter
                        binding.pbLoading.visibility = View.GONE
                        binding.rvProduct.visibility = View.VISIBLE
                    }

                }
                ERROR ->{
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}