package com.group4.secondhand.ui.jual

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.databinding.ItemCategoryProductBinding
import com.group4.secondhand.ui.listCategory

class BottomSheetCategoryAdapter(
    private val selected: (ResponseCategoryHome)->Unit,
    private val unselected: (ResponseCategoryHome)->Unit
) :
    RecyclerView.Adapter<BottomSheetCategoryAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseCategoryHome>(){

        override fun areItemsTheSame(
            oldItem: ResponseCategoryHome,
            newItem: ResponseCategoryHome
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseCategoryHome,
            newItem: ResponseCategoryHome
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value:List<ResponseCategoryHome>?) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCategoryProductBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemCategoryProductBinding):
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: ResponseCategoryHome){
            binding.apply {
                cbCategory.text = data.name
                cbCategory.isChecked = listCategory.contains(data.name)
                cbCategory.setOnClickListener{
                    if (!listCategory.contains(data.name)){
                        selected.invoke(data)
                    } else {
                        unselected.invoke(data)
                    }
                }
            }
        }
    }

}