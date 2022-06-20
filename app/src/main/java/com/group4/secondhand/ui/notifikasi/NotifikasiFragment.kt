@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.notifikasi

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
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.model.ResponseNotification
import com.group4.secondhand.databinding.FragmentNotifikasiBinding
import com.group4.secondhand.ui.home.HomeFragment.Companion.result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotifikasiFragment : Fragment() {
    private var _binding: FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotifikasiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.statusBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, result
        )
        val progressDialog = ProgressDialog(requireContext())
        viewModel.getToken()
        viewModel.user.observe(viewLifecycleOwner) {
            if (it.token == UserPreferences.DEFAULT_TOKEN) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Pesan")
                    .setMessage("Anda Belom Masuk")
                    .setPositiveButton("Login") { dialogP, _ ->
                        findNavController().navigate(R.id.action_notifikasiFragment_to_loginCompose)
                        dialogP.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogN, _ ->
                        findNavController().navigate(R.id.action_notifikasiFragment_to_homeFragment2)
                        dialogN.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            } else {
                viewModel.getNotification(it.token)
            }
        }
        viewModel.notification.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    LOADING -> {
                        progressDialog.show()
                    }
                    SUCCESS -> {
                        val notificationAdapter =
                            NotificationAdapter(object : NotificationAdapter.OnClickListener {
                                override fun onClickItem(data: ResponseNotification) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Notif Id = ${data.id}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        notificationAdapter.submitData(it.data)
                        binding.rvNotification.adapter = notificationAdapter
                        progressDialog.dismiss()
                    }
                    ERROR -> {
                        progressDialog.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setMessage(it.message)
                            .show()
                    }
                }
            }
        }

    }


}