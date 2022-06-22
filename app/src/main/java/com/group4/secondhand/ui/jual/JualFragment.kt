package com.group4.secondhand.ui.jual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentJualBinding

class JualFragment : Fragment() {

    private var _binding: FragmentJualBinding? = null
    private val binding get() = _binding!!

    companion object{
        const val NAMA_PRODUK_KEY = "namaProduk"
        const val HARGA_PRODUK_KEY = "hargaProduk"
        const val DESKRIPSI_PRODUK_KEY = "deskripsiProduk"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.btnPreview.setOnClickListener {
            resetError()
            val namaProduk = binding.etNama.text.toString()
            val hargaProduk = binding.etHarga.text.toString()
            val deskripsiProduk = binding.etDeskripsi.text.toString()
            val validation = validation(
                namaProduk,
                hargaProduk,
                deskripsiProduk,
                "uri gamana"
            )
            if (validation == "passed"){
                val bundle = Bundle()
                bundle.putString(NAMA_PRODUK_KEY, namaProduk)
                bundle.putString(HARGA_PRODUK_KEY, hargaProduk)
                bundle.putString(DESKRIPSI_PRODUK_KEY, deskripsiProduk)
                findNavController().navigate(R.id.action_jualFragment_to_previewProductFragment, bundle)
            }
        }
    }

    private fun resetError() {
        binding.namaContainer.error = null
        binding.hargaContainer.error = null
        binding.deskripsiContainer.error = null
    }

    private fun validation(namaProduk: String, hargaProduk: String, deskripsiProduk: String, uriFoto: String): String {
        when {
            namaProduk.isEmpty() -> {
                binding.namaContainer.error = "Nama Produk tidak boleh kosong"
                return "Nama Produk Kosong!"
            }
            hargaProduk.isEmpty() -> {
                binding.hargaContainer.error = "Harga Produk tidak boleh kosong"
                return "Harga Produk Kosong!"
            }
            deskripsiProduk.isEmpty() -> {
                binding.deskripsiContainer.error = "Nama Produk tidak boleh kosong"
                return "Deskripsi Produk Kosong!"
            }
            uriFoto.isEmpty() -> {
                Toast.makeText(requireContext(), "Foto Produk Kosong", Toast.LENGTH_SHORT).show()
                return "Foto Produk Kosong!"
            }
            else -> {
                return "passed"
            }
        }
    }

}