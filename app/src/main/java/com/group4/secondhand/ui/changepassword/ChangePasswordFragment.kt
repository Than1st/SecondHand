@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.changepassword

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentChangePasswordBinding
import com.group4.secondhand.ui.akun.AkunFragment.Companion.USER_TOKEN
import com.group4.secondhand.ui.loadingBar
import com.group4.secondhand.ui.showToastSuccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding : FragmentChangePasswordBinding? = null
    private val binding : FragmentChangePasswordBinding get() = _binding!!
    private val viewModel: ChangePasswordViewModel by viewModels()
    private var token = ""

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
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val loading = loadingBar(requireContext())
        loading.setMessage("Please Wait...")
        token = bundle?.getString(USER_TOKEN).toString()
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSimpan.setOnClickListener {
            val current = binding.etOldPassword.text.toString()
            val new = binding.etNewPassword.text.toString()
            val conf = binding.etNewConfirmPassword.text.toString()
            val valid = validation(current, new, conf)
            if (valid){
                viewModel.changePassword(token, current, new, conf)
            }
        }
        viewModel.response.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    when(it.data?.code()){
                        200 -> {
                            showToastSuccess(binding.root, "Berhasil ubah Password", resources.getColor(R.color.success))
                            findNavController().popBackStack()
                        }
                        400 -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage("Password Lama Salah")
                                .setPositiveButton("OK"){dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                        else -> {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan")
                                .setMessage(it.message)
                                .show()
                        }
                    }
                    loading.dismiss()
                }
                ERROR -> {
                    loading.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
                LOADING -> {
                    loading.show()
                }
            }
        }
    }

    private fun validation(
        current: String,
        new: String,
        conf: String
    ): Boolean {
        return when{
            current.isEmpty() -> {
                false
            }
            new.isEmpty() -> {
                false
            }
            conf.isEmpty() -> {
                false
            }
            conf != new -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Password konfirmasi tidak sesuai!")
                    .setPositiveButton("OK"){dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                return false
            }
            else -> {
                true
            }
        }
    }

}