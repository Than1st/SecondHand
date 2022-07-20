package com.group4.secondhand.ui.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.R
import com.group4.secondhand.data.model.ResponseGetBuyerWishlist
import com.group4.secondhand.databinding.ItemProductHomeBinding
import com.group4.secondhand.ui.currency

class WishlistAdapter(private val onClickItem: OnClickListener) :
    RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseGetBuyerWishlist>() {
        override fun areItemsTheSame(
            oldItem: ResponseGetBuyerWishlist,
            newItem: ResponseGetBuyerWishlist
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseGetBuyerWishlist,
            newItem: ResponseGetBuyerWishlist
        ): Boolean {
            return oldItem.product?.name == newItem.product?.name
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitData(value: List<ResponseGetBuyerWishlist>?) = differ.submitList(value)

    interface OnClickListener {
        fun onClickItem(data: ResponseGetBuyerWishlist)
    }

    inner class ViewHolder(private val binding: ItemProductHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseGetBuyerWishlist) {
            binding.apply {
                if (data.product?.status == "seller"){
                    tvTerjual.visibility = View.VISIBLE
                }
                Glide.with(binding.root)
                    .load(data.product?.imageUrl)
                    .placeholder(R.drawable.default_image)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(binding.ivProduk)
                tvNamaProduk.text = data.product?.name
                tvKategori.visibility = View.GONE
                val price = data.product?.basePrice?.let { currency(it) }
                tvHarga.text = price
                root.setOnClickListener {
                    onClickItem.onClickItem(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemProductHomeBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}