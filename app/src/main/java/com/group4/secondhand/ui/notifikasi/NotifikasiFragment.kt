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
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class NotifikasiFragment : Fragment() {
    private var _binding: FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotifikasiViewModel by viewModels()
    private val listNotif: MutableList<ResponseNotification> = ArrayList()

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
        binding.emptyNotif.visibility = View.GONE
        val pd = ProgressDialog(requireContext())
        pd.setMessage("Please Wait...")
        viewModel.getToken()
        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    pd.dismiss()
                    if (it.data == UserPreferences.DEFAULT_TOKEN) {
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
                        viewModel.getNotification(it.data.toString())
                        getNotif()
                    }
                    viewModel.user.removeObservers(viewLifecycleOwner)
                }
                ERROR -> {
                    pd.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
                LOADING -> {
                    pd.show()
                }
            }
        }
    }

    private fun getNotif() {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        viewModel.notification.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    LOADING -> {
                        progressDialog.show()
                    }
                    SUCCESS -> {
                        if (it.data.isNullOrEmpty()) {
                            binding.emptyNotif.visibility = View.VISIBLE
                        } else {
                            for (data in it.data) {
                                if (data.imageUrl.isNullOrEmpty() &&
                                    data.basePrice.isEmpty() &&
                                    data.product != null &&
                                    data.bidPrice.toString().isEmpty() &&
                                    data.productName.isEmpty() &&
                                    data.transactionDate.isNullOrEmpty()
                                ) {
                                } else {
                                    listNotif.add(data)
                                }
                            }
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
                            notificationAdapter.submitData(listNotif)
                            binding.rvNotification.adapter = notificationAdapter
                        }
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