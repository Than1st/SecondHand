@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.akun

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
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
import com.group4.secondhand.databinding.ActivityMainBinding.inflate
import com.group4.secondhand.databinding.FragmentAkunBinding
import com.group4.secondhand.databinding.FragmentChangePasswordBinding
import com.group4.secondhand.databinding.MenuPengaturanAkunBinding
import com.group4.secondhand.ui.changepassword.ChangePasswordFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AkunFragment : Fragment() {

    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AkunViewModel by viewModels()
    private val bundle = Bundle()

    companion object {
        const val USER_NAME = "fullname"
        const val USER_EMAIL = "email"
        const val USER_PHONE_NUMBER = "phone_number"
        const val USER_ADDRESS = "address"
        const val USER_CITY = "city"
        const val USER_IMAGE = "image_url"
        const val USER_TOKEN = "user_token"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireContext())
        viewModel.getToken()
        viewModel.alreadyLogin.observe(viewLifecycleOwner) {
            if (it == DEFAULT_TOKEN) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Anda Belom Masuk")
                    .setPositiveButton("Login") { dialogP, _ ->
                        findNavController().navigate(R.id.action_akunFragment_to_loginCompose)
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogN, _ ->
                        findNavController().navigate(R.id.action_akunFragment_to_homeFragment)
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
                viewModel.alreadyLogin.removeObservers(viewLifecycleOwner)
            } else {
                bundle.putString(USER_TOKEN, it)
                viewModel.getUserData(it)
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data != null) {
                        bundle.putString(USER_NAME, it.data.fullName)
                        bundle.putString(USER_EMAIL, it.data.email)
                        bundle.putString(USER_CITY, it.data.city)
                        bundle.putString(USER_ADDRESS, it.data.address)
                        bundle.putString(USER_PHONE_NUMBER, it.data.phoneNumber)
                        if (it.data.imageUrl != null) {
                            binding.tvNama.text = it.data.fullName
                            binding.tvNomor.text = it.data.phoneNumber
                            binding.tvEmail.text = it.data.email
                            Glide.with(requireContext())
                                .load(it.data.imageUrl.toString())
                                .placeholder(R.drawable.image_profile)
                                .transform(CenterCrop(), RoundedCorners(12))
                                .into(binding.ivProfile)
                            bundle.putString(USER_IMAGE, it.data.imageUrl.toString())
                        }
                    }
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                }
                LOADING -> {
                    progressDialog.setMessage("Please Wait...")
                    progressDialog.show()
                }
            }
        }

        binding.listUbahAkun.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_editAkunFragment, bundle)
        }
        binding.listPengaturanAkun.setOnClickListener {
            val dialogBinding = MenuPengaturanAkunBinding.inflate(LayoutInflater.from(requireContext()))
            val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            dialogBuilder.setView(dialogBinding.root)
            val dialog = dialogBuilder.create()
            dialog.setCancelable(true)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.listChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_akunFragment_to_changePasswordFragment, bundle)
                dialog.dismiss()
            }
            dialogBinding.listClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.listRiwayatPenawaran.setOnClickListener {
            findNavController().navigate(R.id.action_akunFragment_to_riwayatPenawaranFragment, bundle)
        }
        binding.listWishlist.setOnClickListener{
            findNavController().navigate(R.id.action_akunFragment_to_wishlistFragment)
        }

        binding.listKeluar.setOnClickListener {
            AlertDialog
                .Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Yakin ingin keluar?")
                .setPositiveButton("Iya") { dialogPositive, _ ->
                    viewModel.deleteToken()
                    Toast
                        .makeText(
                            requireContext(),
                            "Logout Success",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    findNavController().navigate(R.id.action_akunFragment_to_homeFragment)
                    dialogPositive.dismiss()
                }
                .setNegativeButton("Tidak") { dialogNegative, _ ->
                    dialogNegative.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }

}