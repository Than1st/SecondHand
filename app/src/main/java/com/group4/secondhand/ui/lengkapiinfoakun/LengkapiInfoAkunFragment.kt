package com.group4.secondhand.ui.lengkapiinfoakun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.group4.secondhand.databinding.FragmentLengkapiInfoAkunBinding
import com.group4.secondhand.ui.jual.JualFragment.Companion.NAME_USER_KEY

class LengkapiInfoAkunFragment : Fragment() {

    private var _binding : FragmentLengkapiInfoAkunBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentLengkapiInfoAkunBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val nama = bundle?.getString(NAME_USER_KEY)
        binding.etNama.setText(nama)
    }

}