@file:Suppress("DEPRECATION")

package com.group4.secondhand.ui.jual

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group4.secondhand.data.api.Status.*
import com.group4.secondhand.databinding.FragmentBottomSheetCategoryProductBinding
import com.group4.secondhand.ui.listCategory
import com.group4.secondhand.ui.listCategoryId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetPilihCategoryFragment(
    private val update: ()->Unit
) : BottomSheetDialogFragment() {

    private var _binding : FragmentBottomSheetCategoryProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JualViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCategoryProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait...")
        binding.btnKirimCategory.setOnClickListener {
            progressDialog.show()
            viewModel.addCategory(listCategory)
            Handler().postDelayed({
                progressDialog.dismiss()
                update.invoke()
                dismiss()
            }, 1000)
        }
        viewModel.getCategoryHome()
        viewModel.category.observe(viewLifecycleOwner){
            when(it.status){
                SUCCESS -> {
                    progressDialog.dismiss()
                    if(it.data != null){
                        val adapter = BottomSheetCategoryAdapter(
                            selected = { selected ->
                                listCategory.add(selected.name)
                                listCategoryId.add(selected.id)
                            },
                            unselected = { unselected ->
                                listCategory.remove(unselected.name)
                                listCategoryId.add(unselected.id)
                            }
                        )
                        adapter.submitData(it.data)
                        binding.rvPilihKategori.adapter = adapter
                    }
                }
                ERROR -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .setPositiveButton("Ok"){ dialog, _ ->
                            dialog.dismiss()
                            findNavController().popBackStack()
                        }
                        .show()
                }
                LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }
}