package com.group4.secondhand.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group4.secondhand.data.model.ResponseCategoryHome
import com.group4.secondhand.databinding.ItemCategoryHomeBinding
import com.group4.secondhand.ui.lastPosition


class CategoryAdapter(private val onItemClick: OnClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var rowIndex = -1
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseCategoryHome>() {
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
    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<ResponseCategoryHome>?) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemCategoryHomeBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data,position)
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemCategoryHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseCategoryHome,position: Int) {
            binding.tvNamaKategori.text = data.name
            binding.root.setOnClickListener {
                onItemClick.onClickItem(data)
                rowIndex = position
                lastPosition = position
                notifyDataSetChanged()
            }
            if ( lastPosition == position){
                binding.cardCategory.setBackgroundColor(Color.parseColor("#06283D"))
            }else{
                if (rowIndex == position){
                    binding.cardCategory.setBackgroundColor(Color.parseColor("#06283D"))
                }else{
                    binding.cardCategory.setBackgroundColor(Color.parseColor("#47B5FF"))
                }
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(data: ResponseCategoryHome)
    }
}