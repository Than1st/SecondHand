package com.group4.secondhand.ui.penawar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.R
import com.group4.secondhand.databinding.FragmentBottomSheetStatusBinding

class BottomSheetStatusProdukFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetStatusBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSayaTertarikNego.setOnClickListener {
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rb_berhasil_terjual -> {
                    Toast.makeText(context, "Berhasil Terjual ", Toast.LENGTH_SHORT).show()

                }
                R.id.rb_batalkan_transaksi -> {
                    Toast.makeText(context, "Batalkan Transaksi", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val id: Int = binding.radioGroup.checkedRadioButtonId
            if (id != -1) {
                binding.btnSayaTertarikNego.isEnabled = true
                binding.btnSayaTertarikNego.backgroundTintList = requireContext().getColorStateList(R.color.dark_blue)
            }
        }
    }



}

