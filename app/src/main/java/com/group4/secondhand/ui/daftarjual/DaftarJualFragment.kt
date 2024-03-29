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
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_PHONE_NUMBER
import com.group4.secondhand.ui.listCategory
import com.group4.secondhand.ui.listCategoryId
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class DaftarJualFragment : Fragment() {
    private var _binding: FragmentDaftarJualBinding? = null
    private val binding get() = _binding!!
    private val daftarJualViewModel: DaftarJualViewModel by viewModels()
    private val bundle = Bundle()
    private val bundlePenawar = Bundle()
    private val bundleEdit = Bundle()
    private var token = ""
    private val listProduct: MutableList<ResponseSellerProduct> = ArrayList()
    private val listProductSold: MutableList<ResponseSellerProduct> = ArrayList()
    private val listOrder: MutableList<ResponseSellerOrder> = ArrayList()

    companion object {
        const val USER_TOKEN = "UserToken"
        const val USER_CITY = "UserCity"
        const val ORDER_ID = "OrderId"
        const val PRODUCT_IMAGE = "ProductImage"
        const val PRODUCT_NAME = "ProductName"
        const val PRODUCT_DESCRIPTION = "productDescription"
        const val PRODUCT_PRICE = "ProductPrice"
        const val PRODUCT_ID = "productId"
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
                bundle.putString(AkunFragment.USER_TOKEN, it)
                bundlePenawar.putString(USER_TOKEN, it)
                token = it
            }
            getSellerProduct("")
            setSellerName()
        }
        setRecycler()
    }

    private fun setSellerName() {
        daftarJualViewModel.getDataUser(token)
        daftarJualViewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data != null) {
                        if (it.data.city.isEmpty()) {
                            binding.tvKotaPenjual.text = "-"
                        } else {
                            binding.tvKotaPenjual.text = it.data.city
                        }
                        binding.tvNamaPenjual.text = it.data.fullName
                        Glide.with(requireContext())
                            .load(it.data.imageUrl)
                            .transform(CenterCrop(), RoundedCorners(12))
                            .placeholder(R.drawable.default_image)
                            .into(binding.ivAvatarPenjual)
                        binding.cardSeller.visibility = View.VISIBLE
                        binding.shimmerCard.visibility = View.INVISIBLE
                        bundle.putString(AkunFragment.USER_NAME, it.data.fullName)
                        bundle.putString(AkunFragment.USER_CITY, it.data.city)
                        bundle.putString(USER_ADDRESS, it.data.address)
                        bundle.putString(USER_PHONE_NUMBER, it.data.phoneNumber)
                        if (it.data.imageUrl != null) {
                            bundle.putString(AkunFragment.USER_IMAGE, it.data.imageUrl.toString())
                        }
                    }
                }
                ERROR -> {
                    binding.cardSeller.visibility = View.GONE
                }
                LOADING -> {

                    binding.cardSeller.visibility = View.GONE
                    binding.shimmerCard.visibility = View.VISIBLE
                }
            }
        }
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.action_daftarJualFragment_to_editAkunFragment, bundle)
        }
    }

    private fun setRecycler() {
        binding.btnProduk.setBackgroundColor(Color.parseColor("#06283D"))
        binding.btnProduk.setOnClickListener {
            getSellerProduct("")
            binding.btnProduk.setBackgroundColor(Color.parseColor("#06283D"))
            binding.btnDiminati.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnTerjual.setBackgroundColor(Color.parseColor("#47B5FF"))
        }
        binding.btnDiminati.setOnClickListener {
            binding.rvProduct.visibility = View.GONE
            binding.lottieEmpty.visibility = View.GONE
            binding.rvOrder.visibility = View.VISIBLE
            binding.btnProduk.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnDiminati.setBackgroundColor(Color.parseColor("#06283D"))
            binding.btnTerjual.setBackgroundColor(Color.parseColor("#47B5FF"))
            getSellerOrder()
        }
        binding.btnTerjual.setOnClickListener {
            binding.rvProduct.visibility = View.VISIBLE
            binding.lottieEmpty.visibility = View.GONE
            binding.rvOrder.visibility = View.GONE
            binding.btnProduk.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnDiminati.setBackgroundColor(Color.parseColor("#47B5FF"))
            binding.btnTerjual.setBackgroundColor(Color.parseColor("#06283D"))
            getSellerProduct("seller")
        }
    }

    private fun getSellerProduct(produkStatus:String) {
        binding.rvProduct.visibility = View.VISIBLE
        binding.rvOrder.visibility = View.GONE
        binding.lottieEmpty.visibility = View.GONE
        daftarJualViewModel.getProduct(token)
        daftarJualViewModel.product.observe(viewLifecycleOwner) {
            when (it.status) {
                LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.GONE
                    binding.rvProduct.visibility = View.GONE
                    binding.btnDiminati.isEnabled = false
                    binding.btnTerjual.isEnabled = false
                    binding.btnProduk.isEnabled = false
                }
                SUCCESS -> {
                    if (it.data != null) {
                        val sellerProductAdapter =
                            SellerProductAdapter(object : SellerProductAdapter.OnclickListener {
                                override fun onClickItem(data: ResponseSellerProduct) {
                                    bundleEdit.apply {
                                        putInt(PRODUCT_ID, data.id)
                                        putString(PRODUCT_NAME, data.name)
                                        putInt(PRODUCT_PRICE, data.basePrice)
                                        listCategory.clear()
                                        listCategoryId.clear()
                                        for (kategori in data.categories) {
                                            listCategory.add(kategori.name)
                                            listCategoryId.add(kategori.id)
                                        }
                                        putString(PRODUCT_DESCRIPTION, data.description)
                                        putString(PRODUCT_IMAGE, data.imageUrl)
                                        putString(USER_CITY, data.location)
                                    }
                                    if (data.status != "seller"){
                                        findNavController().navigate(
                                            R.id.action_daftarJualFragment_to_editProductFragment,
                                            bundleEdit
                                        )
                                    }else{
                                        Toast.makeText(requireContext(), "Produk sudah terjual", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            })
                        listProduct.clear()
                        listProductSold.clear()
                        for (i in it.data) {
                            if (i.status == "available") {
                                listProduct.add(i)
                            } else if(i.status == "seller"){
                                listProductSold.add(i)
                            }
                        }
                        if (produkStatus == "seller" ){
                            if (listProductSold.size == 0) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            }else{
                                binding.lottieEmpty.visibility = View.GONE
                            }
                            sellerProductAdapter.submitData(listProductSold)
                        } else{
                            if (listProduct.size == 0 ) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            }else{
                                binding.lottieEmpty.visibility = View.GONE
                            }
                            sellerProductAdapter.submitData(listProduct)
                        }
                        binding.rvProduct.adapter = sellerProductAdapter
                    }

                    binding.buttonGrup.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.btnDiminati.isEnabled = true
                    binding.btnTerjual.isEnabled = true
                    binding.btnProduk.isEnabled = true
                }
                ERROR -> {
                    Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getSellerOrder() {
        daftarJualViewModel.getOrder(token, "")
        daftarJualViewModel.order.observe(viewLifecycleOwner) {
            when (it.status) {
                LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.GONE
                    binding.rvOrder.visibility = View.GONE
                    binding.btnDiminati.isEnabled = false
                    binding.btnTerjual.isEnabled = false
                    binding.btnProduk.isEnabled = false
                }
                SUCCESS -> {
                    if (it.data != null) {
                        val sellerOrderAdapter =
                            SellerOrderAdapter(object : SellerOrderAdapter.OnClickListener {
                                override fun onClickItem(data: ResponseSellerOrder) {
                                    bundlePenawar.putInt(ORDER_ID, data.id)
                                    findNavController()
                                        .navigate(R.id.action_daftarJualFragment_to_infoPenawarFragment,bundlePenawar)
                                }
                            })
                        listOrder.clear()
                        for (data in it.data) {
                                if (data.product.status != "seller"){
                                    listOrder.add(data)
                                }
                        }
                        sellerOrderAdapter.submitData(listOrder)
                        binding.rvOrder.adapter = sellerOrderAdapter
                    }
                    if (listOrder.size == 0) {
                        binding.lottieEmpty.visibility = View.VISIBLE
                    }
                    binding.rvOrder.visibility = View.VISIBLE
                    binding.pbLoading.visibility = View.GONE
                    binding.btnDiminati.isEnabled = true
                    binding.btnTerjual.isEnabled = true
                    binding.btnProduk.isEnabled = true
                }
                ERROR -> {
                    Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}