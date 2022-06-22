package com.group4.secondhand.ui.editakun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.group4.secondhand.R
import com.group4.secondhand.data.model.RequestUpdateUser
import com.group4.secondhand.databinding.FragmentEditAkunBinding
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_ADDRESS
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_CITY
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_EMAIL
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_IMAGE
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_NAME
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_PHONE_NUMBER
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_TOKEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAkunFragment : Fragment() {
    private var _binding : FragmentEditAkunBinding? = null
    private val binding get() = _binding!!
    private val viewModel : EditAkunViewModel by viewModels()

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
        _binding = FragmentEditAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        binding.apply {
            etNama.setText(
                cekNull(bundle?.getString(USER_NAME).toString())
            )
            etEmail.setText(
                cekNull(bundle?.getString(USER_EMAIL).toString())
            )
            etKota.setText(
                cekNull(bundle?.getString(USER_CITY).toString())
            )
            etAlamat.setText(
                cekNull(bundle?.getString(USER_ADDRESS).toString())
            )
            etNoHp.setText(
                cekNull(bundle?.getString(USER_PHONE_NUMBER).toString())
            )
            Glide.with(requireContext())
                .load(bundle?.getString(USER_IMAGE)?:R.drawable.default_image)
                .transform(CenterCrop(), RoundedCorners(8))
                .into(ivProfile)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSimpan.setOnClickListener {
            resetErrors()
            val nama = binding.etNama.text.toString()
            val email = binding.etEmail.text.toString()
            val kota = binding.etKota.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val isValid = validation(nama, email, kota, alamat, noHp)
            if (isValid){
                val request = RequestUpdateUser(
                    nama,
                    email,
                    noHp,
                    alamat,
                    "no_image",
                    kota
                )
                val token = bundle?.getString(USER_TOKEN)
                if (token != null) {
                    viewModel.updateDataUser(token, request)
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "Berhasil Update Data!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun resetErrors() {
        binding.apply {
            namaContainer.error = ""
            emailContainer.error = ""
            kotaContainer.error = ""
            alamatContainer.error = ""
            noHpContainer.error = ""
        }
    }

    private fun validation(nama: String, email: String, kota: String, alamat: String, nohp: String): Boolean {
        return when {
            nama.isEmpty() -> {
                binding.namaContainer.error = "Nama tidak boleh kosong!"
                false
            }
            email.isEmpty() -> {
                binding.emailContainer.error = "Email tidak boleh kosong!"
                false
            }
            kota.isEmpty() -> {
                binding.kotaContainer.error = "Kota tidak boleh kosong!"
                false
            }
            alamat.isEmpty() -> {
                binding.alamatContainer.error = "Alamat tidak boleh kosong!"
                false
            }
            nohp.isEmpty() -> {
                binding.noHpContainer.error = "Nomor Hp tidak boleh kosong!"
                false
            }
            else -> {
                true
            }
        }
    }

    private fun cekNull(value: String): String{
        return when (value) {
            "no_image" -> {
                ""
            }
            "no_city" -> {
                ""
            }
            "no_address" -> {
                ""
            }
            "no_number" -> {
                ""
            }
            "null" -> {
                ""
            }
            else -> {
                value
            }
        }
    }

}