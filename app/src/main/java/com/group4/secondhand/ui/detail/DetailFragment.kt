@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.detail

import android.annotation.SuppressLint
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.datastore.UserPreferences.Companion.DEFAULT_TOKEN
import com.group4.secondhand.databinding.FragmentDetailBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.home.HomeFragment.Companion.PRODUCT_ID
import com.group4.secondhand.ui.home.HomeFragment.Companion.result
import com.group4.secondhand.ui.jual.JualFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var convertBasePrice: String
    private var isBid = false
    private var wishlistId = -1
    private var token = ""
    private val detailViewModel: DetailViewModel by viewModels()
    private var basePrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.statusBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, result
        )
        val bundleLengkapi = Bundle()
        val bundle = arguments
        val productId = bundle?.getInt(PRODUCT_ID)
        detailViewModel.getToken()

        val pd = ProgressDialog(requireContext())
        detailViewModel.token.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data != DEFAULT_TOKEN) {
                        token = it.data.toString()
                        detailViewModel.getBuyerOrder(token)
                        detailViewModel.getBuyerWishlist(token)
                    } else {
                        binding.btnWishlist.visibility = View.GONE
                    }
                    pd.dismiss()
                }
                ERROR -> {
                    pd.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
                LOADING -> {
                    pd.setMessage("Please Wait...")
                    pd.show()
                }
            }
        }

        detailViewModel.getBuyerOrder.observe(viewLifecycleOwner) {
            for (data in 0 until (it.data?.size ?: 0)) {
                if (it.data?.get(data)?.productId == productId) {
                    isBid = true
                }
            }
            if (isBid) {
                binding.btnSayaTertarikNego.isEnabled = false
                binding.btnSayaTertarikNego.backgroundTintList =
                    requireContext().getColorStateList(R.color.dark_grey)
                binding.btnSayaTertarikNego.text = "Menunggu Respon Penjual"
            }
        }

        var productName = ""
        var imageURL = ""
        if (productId != null) {
            detailViewModel.getProdukById(productId)
            getBuyerWishlist(productId)
        }

        detailViewModel.detailProduk.observe(viewLifecycleOwner) { it ->
            when (it.status) {
                SUCCESS -> {
                    when (it.data?.code()) {
                        200 -> if (it.data.body() != null) {
                            productName = it.data.body()?.name.toString()
                            imageURL = it.data.body()?.imageUrl.toString()

                            binding.tvNamaPenjual.text = it.data.body()?.user?.fullName
                            Glide.with(binding.ivAvatarPenjual)
                                .load(it.data.body()?.user?.imageUrl)
                                .centerCrop()
                                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                                .into(binding.ivAvatarPenjual)

                            Glide.with(binding.imageView)
                                .load(it.data.body()?.imageUrl)
                                .into(binding.imageView)
                            binding.tvProdukName.text = it.data.body()?.name
                            binding.tvDeskripsiProduk.text = it.data.body()?.description
                            binding.tvKotaPenjual.text = it.data.body()?.location

                            if (it.data.body()?.basePrice != null) {
                                convertBasePrice = currency(it.data.body()?.basePrice!!)
                                binding.tvProdukHarga.text = convertBasePrice
                                basePrice = it.data.body()?.basePrice!!
                            }
                            if (it.data.body()?.status == "seller") {
                                binding.btnSayaTertarikNego.isEnabled = false
                                binding.btnSayaTertarikNego.backgroundTintList =
                                    requireContext().getColorStateList(R.color.dark_grey)
                                binding.btnSayaTertarikNego.text = "Barang sudah terjual"
                            }

                            var listCategory = ""
                            if (it.data.body()?.categories != null) {
                                for (data in it.data.body()!!.categories) {
                                    listCategory += ", ${data.name}"
                                }
                                binding.tvProdukKategori.text = listCategory.drop(2)
                            }
                        }
                    }
                    pd.dismiss()
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                    pd.dismiss()
                }
                LOADING -> {
                    pd.setMessage("Please Wait...")
                    pd.show()
                }
            }
            binding.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        detailViewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data != null) {
                        val kota = it.data.city ?: "noKota"
                        val alamat = it.data.address ?: "noAddress"
                        val gambar = it.data.imageUrl ?: "noImage"
                        val noHp = it.data.phoneNumber ?: "noHp"
                        if (kota == "noKota" || alamat == "noAddress" || gambar == "noImage" || noHp == "noHp") {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Lengkapi data terlebih dahulu sebelum Jual Barang")
                                .setCancelable(false)
                                .setPositiveButton("Iya") { positiveButton, _ ->
                                    bundleLengkapi.putString(
                                        JualFragment.NAME_USER_KEY,
                                        it.data.fullName
                                    )
                                    positiveButton.dismiss()
                                    findNavController().navigate(
                                        R.id.action_detailFragment_to_lengkapiInfoAkunFragment,
                                        bundleLengkapi
                                    )
                                }
                                .setNegativeButton("Tidak") { negativeButton, _ ->
                                    negativeButton.dismiss()
                                }
                                .show()
                        } else {
                            val bottomFragment = BottomSheetDetailFragment(
                                productId!!,
                                productName,
                                basePrice,
                                imageURL,
                                refreshButton = { detailViewModel.getBuyerOrder(token) }
                            )
                            bottomFragment.show(parentFragmentManager, "Tag")
                        }
                    }
                }
                ERROR -> {}
                LOADING -> {}
            }
        }
        binding.btnSayaTertarikNego.setOnClickListener {
            detailViewModel.getUserData(token)
            if (token == DEFAULT_TOKEN) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Anda Belom Masuk")
                    .setPositiveButton("Login") { dialogP, _ ->
                        findNavController().navigate(R.id.action_detailFragment_to_loginCompose)
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogN, _ ->
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }

    private fun getBuyerWishlist(productId: Int) {
        var wishlist = false
        val fillWishlist = resources.getDrawable(R.drawable.ic_wishlist)
        val outlinedWishlist = resources.getDrawable(R.drawable.ic_unwishlist)
        binding.btnWishlist.setOnClickListener {
            if (wishlist) {
                binding.btnWishlist.setImageDrawable(outlinedWishlist)
                detailViewModel.removeWishlist(token, wishlistId)
                wishlist = false
            } else {
                binding.btnWishlist.setImageDrawable(fillWishlist)
                detailViewModel.addWishlist(token, productId)
                wishlist = true
            }
        }
        detailViewModel.getBuyerWishlist.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data != null) {
                        for (data in it.data) {
                            if (data.product?.id == productId) {
                                wishlist = true
                                wishlistId = data.id
                                binding.btnWishlist.setImageDrawable(fillWishlist)

                            }
                        }
                    }
                    binding.btnWishlist.isEnabled = true
                }
                ERROR -> {}
                LOADING -> {
                    binding.btnWishlist.isEnabled = false
                }
            }
        }


        detailViewModel.removeWishlist.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    detailViewModel.getBuyerWishlist(token)
                    Toast.makeText(
                        requireContext(),
                        "Berhasil menghapus produk dari wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Gagal menghapus produk dari wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                LOADING -> {
                    binding.btnWishlist.isEnabled = false
                }
            }
        }
        detailViewModel.addWishlist.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    detailViewModel.getBuyerWishlist(token)
                    Toast.makeText(
                        requireContext(),
                        "Berhasil menambahkan produk ke wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        "Gagal menambahkan produk ke wishlist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                LOADING -> {
                    binding.btnWishlist.isEnabled = false
                }
            }
        }

    }

}
