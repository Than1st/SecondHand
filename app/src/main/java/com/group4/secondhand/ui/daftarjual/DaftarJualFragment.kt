package com.group4.secondhand.ui.daftarjual

import android.app.AlertDialog
import android.graphics.Color
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
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_ADDRESS
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_CITY
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_NAME
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_PHONE_NUMBER
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_TOKEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaftarJualFragment : Fragment() {
    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!
    private val daftarJualViewModel: DaftarJualViewModel by viewModels()
    private val bundle = Bundle()
    private val bundlePenawar = Bundle()

    companion object{
        const val PENAWAR_USER_TOKEN = "penawarUserToken"
        const val PENAWAR_USER_NAME = "penawarUserName"
        const val PENAWAR_USER_CITY = "penawarUserCity"
        const val PENAWAR_USER_IMAGE = "penawarUserCity"
        const val PENAWAR_ORDER_ID = "penawarOrderId"
        const val PENAWAR_PRODUCT_IMAGE = "penawarProductImage"
        const val PENAWAR_PRODUCT_NAME = "penawarProductName"
        const val PENAWAR_PRODUCT_PRICE = "penawarProductPrice"
        const val PENAWAR_PRODUCT_BID = "penawarProductBid"
        const val PENAWAR_PRODUCT_BID_DATE = "penawarProductBidDate"
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
            } else {
                bundle.putString(USER_TOKEN, it)
                bundlePenawar.putString(PENAWAR_USER_TOKEN, it)
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
                    binding.btnDiminati.visibility = View.VISIBLE
                    binding.btnTerjual.visibility = View.VISIBLE
                    binding.btnProduk.visibility = View.VISIBLE
                    if (it.data != null) {
                        if(it.data.city == "no_city"){
                            binding.tvKotaPenjual.text = "-"
                        }else{
                            binding.tvKotaPenjual.text = it.data.city
                        }
                        binding.tvNamaPenjual.text = it.data.fullName
                        Glide.with(requireContext())
                            .load(it.data.imageUrl)
                            .transform(CenterCrop(), RoundedCorners(12))
                            .placeholder(R.drawable.default_image)
                            .into(binding.ivAvatarPenjual)
                        binding.cardSeller.visibility = View.VISIBLE
                        binding.shimmerCard.visibility = View.GONE
                        bundle.putString(USER_NAME, it.data.fullName)
                        bundle.putString(USER_CITY, it.data.city)
                        bundle.putString(USER_ADDRESS,it.data.address)
                        bundle.putString(USER_PHONE_NUMBER,it.data.phoneNumber)
                        if (it.data.imageUrl != null){
                            bundle.putString(AkunFragment.USER_IMAGE, it.data.imageUrl.toString())
                        }
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
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.action_daftarJualFragment_to_editAkunFragment,bundle)
        }
    }

    private fun setRecycler() {
        binding.btnProduk.setOnClickListener {
            binding.rvProduct.visibility = View.VISIBLE
            binding.rvDiminati.visibility = View.GONE
            binding.rvTerjual.visibility = View.GONE
            binding.btnProduk.setBackgroundColor(Color.parseColor("#06283D"))
            binding.btnDiminati.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnTerjual.setBackgroundColor(Color.parseColor("#47B5FF"))
        }
        binding.btnDiminati.setOnClickListener {
            getSellerOrder()
            binding.rvProduct.visibility = View.GONE
            binding.rvTerjual.visibility = View.GONE
            binding.btnProduk.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnDiminati.setBackgroundColor(Color.parseColor("#06283D"))
            binding.btnTerjual.setBackgroundColor(Color.parseColor("#47B5FF"))
        }
        binding.btnTerjual.setOnClickListener {
            binding.rvProduct.visibility = View.GONE
            binding.rvDiminati.visibility = View.GONE
            binding.rvTerjual.visibility = View.VISIBLE
            binding.btnProduk.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnDiminati.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnTerjual.setBackgroundColor(Color.parseColor("#06283D"))
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
                        binding.btnProduk.setBackgroundColor(Color.parseColor("#06283D"))
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
                                    bundlePenawar.putString(PENAWAR_USER_NAME, data.buyerInformation.fullName)
                                    bundlePenawar.putString(PENAWAR_USER_CITY, data.buyerInformation.city.toString())
                                    bundlePenawar.putInt(PENAWAR_ORDER_ID, data.id)
                                    bundlePenawar.putString(PENAWAR_PRODUCT_NAME, data.product.name)
                                    bundlePenawar.putString(PENAWAR_PRODUCT_PRICE, data.product.basePrice.toString())
                                    bundlePenawar.putString(PENAWAR_PRODUCT_BID, data.price.toString())
                                    bundlePenawar.putString(PENAWAR_PRODUCT_IMAGE, data.product.imageUrl)
                                    bundlePenawar.putString(PENAWAR_PRODUCT_BID_DATE, data.createdAt)
                                    findNavController().navigate(R.id.action_daftarJualFragment_to_infoPenawarFragment, bundlePenawar)
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