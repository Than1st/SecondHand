package com.group4.secondhand.ui.daftarjual

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
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.data.model.ResponseSellerOrder
import com.group4.secondhand.data.model.ResponseSellerProduct
import com.group4.secondhand.databinding.FragmentDaftarJualBinding
import com.group4.secondhand.ui.akun.AkunFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaftarJualFragment : Fragment() {
    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!
    private val daftarJualViewModel: DaftarJualViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDaftarJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daftarJualViewModel.getToken()
        daftarJualViewModel.token.observe(viewLifecycleOwner) {
            daftarJualViewModel.getDataUser(it)
            daftarJualViewModel.getProduct(it)
            daftarJualViewModel.getOrder(it)
            if (it == DEFAULT_TOKEN) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Anda Belom Masuk")
                    .setPositiveButton("Login") { dialogP, _ ->
                        findNavController().navigate(R.id.action_daftarJualFragment_to_loginCompose)
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogN, _ ->
                        findNavController().navigate(R.id.action_daftarJualFragment_to_homeFragment)
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
                daftarJualViewModel.token.removeObservers(viewLifecycleOwner)
            }
        }
        setSellerName()
        setRecycler()
        getSellerProduct()


    }

    private fun setSellerName() {
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
                        binding.cardSeller.visibility = View.VISIBLE
                        binding.shimmerCard.visibility = View.GONE
                    }
                }
                ERROR -> {
                    binding.cardSeller.visibility = View.GONE
                }
                LOADING -> {
                    binding.btnDiminati.visibility = View.GONE
                    binding.btnTerjual.visibility = View.GONE
                    binding.btnProduk.visibility = View.GONE
                    binding.cardSeller.visibility = View.GONE
                    binding.shimmerCard.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setRecycler() {
        binding.btnProduk.setOnClickListener {
            binding.rvProduct.visibility = View.VISIBLE
            binding.rvDiminati.visibility = View.GONE
            binding.rvTerjual.visibility = View.GONE
        }
        binding.btnDiminati.setOnClickListener {
            getSellerOrder()
            binding.rvProduct.visibility = View.GONE
            binding.rvTerjual.visibility = View.GONE
        }
        binding.btnTerjual.setOnClickListener {
            binding.rvProduct.visibility = View.GONE
            binding.rvDiminati.visibility = View.GONE
            binding.rvTerjual.visibility = View.VISIBLE
        }
    }

    private fun getSellerProduct() {
        daftarJualViewModel.product.observe(viewLifecycleOwner) {
            when (it.status) {
                LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvProduct.visibility = View.GONE
                    binding.rvDiminati.visibility = View.GONE
                    binding.rvTerjual.visibility = View.GONE
                }
                SUCCESS -> {
                    if (it.data != null) {
                        val sellerProductAdapter =
                            SellerProductAdapter(object : SellerProductAdapter.OnclickListener {
                                override fun onClickItem(data: ResponseSellerProduct) {
                                    Toast.makeText(
                                        requireContext(),
                                        "go to edit",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })
                        sellerProductAdapter.submitData(it.data)
                        binding.rvProduct.adapter = sellerProductAdapter
                        binding.pbLoading.visibility = View.GONE
                        binding.rvProduct.visibility = View.VISIBLE
                    }

                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getSellerOrder() {
        daftarJualViewModel.order.observe(viewLifecycleOwner) {
            when (it.status) {
                LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.rvProduct.visibility = View.GONE
                    binding.rvDiminati.visibility = View.GONE
                    binding.rvTerjual.visibility = View.GONE
                }
                SUCCESS -> {
                    if (it.data != null) {
                        val sellerOrderAdapter =
                            SellerOrderAdapter(object : SellerOrderAdapter.OnClickListener {
                                override fun onClickItem(data: ResponseSellerOrder) {
                                    "go to info penawar"
                                }
                            })
                        sellerOrderAdapter.submitData(it.data)
                        binding.rvDiminati.adapter = sellerOrderAdapter
                        binding.pbLoading.visibility = View.GONE
                        binding.rvDiminati.visibility = View.VISIBLE
                    }

                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}