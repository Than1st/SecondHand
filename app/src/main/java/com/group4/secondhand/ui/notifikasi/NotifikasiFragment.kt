@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.notifikasi

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.group4.secondhand.R
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.data.datastore.UserPreferences
import com.group4.secondhand.data.model.ResponseNotification
import com.group4.secondhand.databinding.FragmentNotifikasiBinding
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.ORDER_ID
import com.group4.secondhand.ui.daftarjual.DaftarJualFragment.Companion.USER_TOKEN
import com.group4.secondhand.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotifikasiFragment : Fragment() {
    private var _binding: FragmentNotifikasiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotifikasiViewModel by viewModels()
    private val bundle = Bundle()
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
        pd.setCancelable(false)
        viewModel.getToken()
        viewModel.user.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
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
                        viewModel.user.removeObservers(viewLifecycleOwner)
                    } else {
                        bundle.putString(USER_TOKEN, it.data)
                        getNotif(it.data.toString())
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
                    pd.show()
                }
            }
        }
    }

    private fun getNotif(token: String) {
        val pd = ProgressDialog(requireContext())
        pd.setMessage("Please Wait...")
        pd.setCancelable(false)
        viewModel.getNotification(token)
        viewModel.notification.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    LOADING -> {
                        pd.show()
                        binding.emptyNotif.visibility = View.GONE
                        binding.rvNotification.visibility = View.GONE
                    }
                    SUCCESS -> {
                        listNotif.clear()
                        binding.rvNotification.visibility = View.VISIBLE
                        if (it.data.isNullOrEmpty()) {
                            binding.emptyNotif.visibility = View.VISIBLE
                        } else {
                            for (data in it.data) {
                                when {
                                    data.basePrice.isEmpty() ||
                                            data.product == null ||
                                            data.bidPrice.toString().isEmpty() ||
                                            data.productName.isEmpty() ||
                                            data.transactionDate.isNullOrEmpty() ||
                                            data.status == "create" ||
                                            data.order_id == null -> {
                                    }
                                    else -> {
                                        listNotif.add(data)
                                    }
                                }
                            }
                            val notificationAdapter =
                                NotificationAdapter(object : NotificationAdapter.OnClickListener {
                                    override fun onClickItem(data: ResponseNotification) {
                                        when (data.status) {
                                            "bid" -> {
                                                if (data.receiverId == data.product.userId) {
                                                    bundle.putInt(ORDER_ID, data.order_id)
                                                    viewModel.markReadNotification(token, data.id)
                                                    findNavController().navigate(
                                                        R.id.action_notifikasiFragment_to_infoPenawarFragment,
                                                        bundle
                                                    )
                                                }
                                            }
                                            "declined" -> {
                                                bundle.putInt(
                                                    HomeFragment.PRODUCT_ID,
                                                    data.productId
                                                )
                                                viewModel.markReadNotification(token, data.id)
                                                findNavController().navigate(
                                                    R.id.action_notifikasiFragment_to_detailFragment,
                                                    bundle
                                                )
                                            }
                                            "accepted" -> {
                                                bundle.putInt(
                                                    HomeFragment.PRODUCT_ID,
                                                    data.productId
                                                )
                                                viewModel.markReadNotification(token, data.id)
                                                findNavController().navigate(
                                                    R.id.action_notifikasiFragment_to_detailFragment,
                                                    bundle
                                                )
                                            }
                                        }
                                    }
                                })
                            notificationAdapter.submitData(listNotif)
                            binding.rvNotification.adapter = notificationAdapter
                        }
                        pd.dismiss()
                    }
                    ERROR -> {
                        pd.dismiss()
                        AlertDialog.Builder(requireContext())
                            .setMessage(it.message)
                            .show()
                    }
                }
            }
        }
    }
}