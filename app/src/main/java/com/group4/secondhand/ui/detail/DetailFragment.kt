package com.group4.secondhand.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.group4.secondhand.databinding.FragmentDetailBinding
import com.group4.secondhand.ui.penawar.BottomSheetInfoPenawarFragment
import com.group4.secondhand.ui.penawar.BottomSheetStatusProdukFragment


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDeskripsi.setOnClickListener {
            var bottomFragment = BottomSheetDetailFragment()
            bottomFragment.show(getParentFragmentManager() ,"Tag")

//            val bottomFragment = BottomSheetInfoPenawarFragment()
//            bottomFragment.show(getParentFragmentManager() ,"Tag")

//            val bottomFragment = BottomSheetStatusProdukFragment()
//            bottomFragment.show(getParentFragmentManager() ,"Tag")

        }

    }

}