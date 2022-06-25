package com.group4.secondhand.ui.daftarjual

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group4.secondhand.data.model.ResponseSellerProduct
import com.group4.secondhand.databinding.ItemProductHomeBinding
import com.group4.secondhand.ui.currency

class SellerProductAdapter(private val onItemClick: OnclickListener) :
    RecyclerView.Adapter<SellerProductAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<ResponseSellerProduct>() {
        override fun areItemsTheSame(
            oldItem: ResponseSellerProduct,
            newItem: ResponseSellerProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseSellerProduct,
            newItem: ResponseSellerProduct
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<ResponseSellerProduct>?) = differ.submitList(value)

    inner class ViewHolder(private val binding: ItemProductHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseSellerProduct) {
            binding.apply {
                Glide.with(binding.root)
                    .load(data.imageUrl)
                    .into(binding.ivProduk)
                tvNamaProduk.text = data.name
                if (data.categories.isNotEmpty()) {
                    if (data.categories.size > 2) {
                        tvKategori.text =
                            "${data.categories[0].name}, ${data.categories[1].name}, ${data.categories[2].name} "
                    } else if (data.categories.size > 1) {
                        tvKategori.text = "${data.categories[0].name}, ${data.categories[1].name}"
                    } else {
                        tvKategori.text = "${data.categories[0].name}"
                    }
                }
                val price = currency(data.basePrice)
                tvHarga.text = price
                root.setOnClickListener {
                    onItemClick.onClickItem(data)
                }
            }
        }

    }

    interface OnclickListener {
        fun onClickItem(data: ResponseSellerProduct)
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