package com.group4.secondhand.ui.editproduct

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
import com.group4.secondhand.databinding.FragmentEditProductBinding
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_CATEGORY
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_DESCRIPTION
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_ID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_IMAGE
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_NAME
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.PRODUCT_PRICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductFragment : Fragment() {
    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!
    private val editProductViewModel: EditProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pd = ProgressDialog(requireContext())
        val args = arguments
        binding.apply {
            etNama.setText(args?.getString(PRODUCT_NAME))
            etHarga.setText(args?.getInt(PRODUCT_PRICE).toString())
            etDeskripsi.setText(args?.getString(PRODUCT_DESCRIPTION))
            etKategori.setText(args?.getString(PRODUCT_CATEGORY))
            Glide.with(requireContext())
                .load(args?.getString(PRODUCT_IMAGE))
                .transform(CenterCrop(), RoundedCorners(12))
                .into(ivFoto)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        editProductViewModel.getToken()
        editProductViewModel.token.observe(viewLifecycleOwner) { token ->
            binding.btnHapus.setOnClickListener {
                if (token != DEFAULT_TOKEN) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pesan")
                        .setMessage("Yakin ingin menghapus produk kamu?")
                        .setPositiveButton("Ya") { dialogP, _ ->
                            editProductViewModel.deleteSellerProduct(
                                token,
                                args?.getInt(PRODUCT_ID).toString().toInt()
                            )
                            dialogP.dismiss()
                        }
                        .setNegativeButton("Batal") { dialogN, _ ->
                            dialogN.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
        editProductViewModel.delete.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    when (it.data?.code()) {
                        200 -> {
                            Toast.makeText(
                                requireContext(),
                                it.data.body()?.msg,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().popBackStack()
                        }
                        400 -> {
                            Toast.makeText(
                                requireContext(),
                                "Product has been order",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().popBackStack()
                        }
                    }
                    pd.dismiss()
                }
                ERROR -> {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                    findNavController().popBackStack()
                }
                LOADING -> {
                    pd.setMessage("Please Wait...")
                    pd.show()
                }
            }
        }
    }
}