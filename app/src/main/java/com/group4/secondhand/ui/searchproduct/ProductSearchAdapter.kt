package com.group4.secondhand.ui.searchproduct

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.data.model.ResponseGetProduct
import com.group4.secondhand.data.model.ResponseGetProductSearch
import com.group4.secondhand.databinding.ItemProductHomeBinding
import com.group4.secondhand.ui.currency

class ProductSearchAdapter(private val onItemClick: OnClickListener) :
    RecyclerView.Adapter<ProductSearchAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseGetProductSearch>() {
        override fun areItemsTheSame(
            oldItem: ResponseGetProductSearch,
            newItem: ResponseGetProductSearch
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseGetProductSearch,
            newItem: ResponseGetProductSearch
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }
    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<ResponseGetProductSearch>?) = differ.submitList(value)

    interface OnClickListener {
        fun onClickItem(data: ResponseGetProductSearch)
    }

    inner class ViewHolder(private val binding: ItemProductHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        var listCategory = ""
        fun bind(data: ResponseGetProductSearch) {
            binding.apply {
                Glide.with(binding.root)
                    .load(data.imageUrl)
                    .placeholder(R.drawable.default_image)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(binding.ivProduk)
                tvNamaProduk.text = data.name
                if (data.categories.isNotEmpty()) {
                    for (data in data.categories){
                        listCategory += ", ${data.name}"
                    }
                    binding.tvKategori.text = listCategory.drop(2)
                }
                val price = currency(data.basePrice)
                tvHarga.text = price
                root.setOnClickListener {
                    onItemClick.onClickItem(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemProductHomeBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}