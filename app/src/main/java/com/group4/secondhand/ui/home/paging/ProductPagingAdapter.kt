package com.group4.secondhand.ui.home.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.data.model.pagingProduk.Product
import com.group4.secondhand.databinding.ItemProductHomeBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.listCategory

class ProductPagingAdapter(private val onClick: (Product) -> Unit) :
    PagingDataAdapter<UiModel.ProductItem, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is UiModel.ProductItem -> (holder as RepoViewHolder).bind(uiModel.products)
            else -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemProductHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return RepoViewHolder(binding)
    }

    inner class RepoViewHolder(private val binding: ItemProductHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var product: Product? = null

        fun bind(data: Product) {

            this.product = data

            binding.root.setOnClickListener {
                onClick(data)
            }
            binding.apply {
                Glide.with(binding.root)
                    .load(data.imageUrl)
                    .placeholder(R.drawable.default_image)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(binding.ivProduk)
                tvNamaProduk.text = data.name
                if (data.categories.isNotEmpty()) {
                    for (data in data.categories) {
                        listCategory += ", ${data.name}"
                    }
                    binding.tvKategori.text = listCategory.drop(2).toString()
                }
                val price = currency(data.basePrice)
                tvHarga.text = price
            }
        }
    }
    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel.ProductItem>() {
            override fun areItemsTheSame(
                oldItem: UiModel.ProductItem,
                newItem: UiModel.ProductItem
            ): Boolean {
                return (oldItem.products.name == newItem.products.name)
            }

            override fun areContentsTheSame(
                oldItem: UiModel.ProductItem,
                newItem: UiModel.ProductItem
            ): Boolean =
                oldItem == newItem
        }
    }
}