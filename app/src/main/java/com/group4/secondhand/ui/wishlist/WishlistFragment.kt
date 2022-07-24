package com.group4.secondhand.ui.wishlist

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.model.ResponseGetBuyerWishlist
import com.group4.secondhand.databinding.FragmentWishlistBinding
import com.group4.secondhand.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val wishlistViewModel: WishlistViewModel by viewModels()
    private var token = ""
    private lateinit var wishlistAdapter: WishlistAdapter
    private val wishlistProduct: MutableList<ResponseGetBuyerWishlist> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wishlistViewModel.getToken()
        wishlistViewModel.token.observe(viewLifecycleOwner) {
            token = it.data.toString()
            wishlistViewModel.getBuyerWishlist(token)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        getWishlist()
    }

    private fun getWishlist() {
        wishlistViewModel.getBuyerWishlist.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    binding.shimmer.visibility = View.GONE
                    binding.rvProduct.visibility = View.VISIBLE
                    if (it.data?.size == 0) {
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.tvEmptyProduct.visibility = View.VISIBLE
                    }
                    if (it.data != null) {
                        wishlistProduct.clear()
                        for (data in it.data){
                            if (data.product != null){
                                wishlistProduct.add(data)
                            }
                        }
                        wishlistAdapter = WishlistAdapter(object : WishlistAdapter.OnClickListener {
                            override fun onClickItem(data: ResponseGetBuyerWishlist) {
                                val productBundle = Bundle()
                                productBundle.putInt(HomeFragment.PRODUCT_ID, data.productId!!)
                                Handler().postDelayed({
                                    findNavController().navigate(
                                        R.id.action_wishlistFragment_to_detailFragment,
                                        productBundle
                                    )
                                }, 1000)
                            }
                        })
                        wishlistAdapter.submitData(wishlistProduct)
                        binding.rvProduct.adapter = wishlistAdapter
                    }
                }
                ERROR -> {
                    Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT)
                        .show()
                }
                LOADING -> {
                    binding.shimmer.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.GONE
                    binding.tvEmptyProduct.visibility = View.GONE
                    binding.rvProduct.visibility = View.GONE
                }
            }
        }
    }
}