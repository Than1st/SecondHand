package com.group4.secondhand.ui.searchproduct

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status
import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.data.model.ResponseGetProductSearch
import com.group4.secondhand.databinding.FragmentSearchBinding
import com.group4.secondhand.ui.MainActivity
import com.group4.secondhand.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var productSearchAdapter: ProductSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultSearch()
        detailProduct()
        binding.searchView.requestFocus()
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchView.showKeyboard()
            } else {
                binding.searchView.hideKeyboard()
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchView.hideKeyboard()
                binding.searchView.clearFocus()
                searchViewModel.getProduct("available","",p0.toString(),"","")
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun resultSearch(){
        searchViewModel.product.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    binding.shimmer.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.GONE
                    binding.tvEmptyProduct.visibility = View.GONE
                    binding.rvProduct.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.shimmer.visibility = View.GONE
                    if (it.data?.size == 0) {
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.tvEmptyProduct.visibility = View.VISIBLE
                    }
                    binding.rvProduct.visibility = View.VISIBLE
                    productSearchAdapter.submitData(it.data)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun detailProduct() {
        productSearchAdapter = ProductSearchAdapter(object : ProductSearchAdapter.OnClickListener {
            override fun onClickItem(data: ResponseGetProductSearch) {
                val productBundle = Bundle()
                productBundle.putInt(HomeFragment.PRODUCT_ID, data.id)
                Handler().postDelayed({
                    findNavController().navigate(
                        R.id.action_searchFragment_to_detailFragment,
                        productBundle
                    )
                }, 1000)
            }
        })
        binding.rvProduct.adapter = productSearchAdapter
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getWindowToken(), 0)
    }
}