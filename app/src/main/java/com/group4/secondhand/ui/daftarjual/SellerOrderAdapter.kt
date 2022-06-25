package com.group4.secondhand.ui.daftarjual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.group4.secondhand.data.model.ResponseSellerOrder
import com.group4.secondhand.databinding.ItemDiminatiBinding
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.formatDate

class SellerOrderAdapter(private val OnItemClick: OnClickListener) :
    RecyclerView.Adapter<SellerOrderAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<ResponseSellerOrder>() {
        override fun areItemsTheSame(
            oldItem: ResponseSellerOrder,
            newItem: ResponseSellerOrder
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseSellerOrder,
            newItem: ResponseSellerOrder
        ): Boolean {
            return oldItem.id == oldItem.id
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<ResponseSellerOrder>?) = differ.submitList(value)

    inner class ViewHolder(private val binding: ItemDiminatiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponseSellerOrder) {
            val basePrice = currency(data.product.basePrice)
            val priceNego = currency(data.price)
            val date = formatDate(data.createdAt)
            binding.apply {
                if (data.status != "declined" && data.status != "accepted") {
                    Glide.with(binding.root)
                        .load(data.product.imageUrl)
                        .transform(CenterCrop(), RoundedCorners(12))
                        .into(binding.ivProductImage)
                    tvNamaProduk.text = data.product.name
                    tvHargaAwalProduk.text = basePrice
                    tvHargaDitawarProduk.text = "Ditawar $priceNego"
                    tvTanggal.text = date.toString()
                    root.setOnClickListener {
                        OnItemClick.onClickItem(data)
                    }
                } else {
                    binding.root.visibility = View.GONE
                }
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(data: ResponseSellerOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemDiminatiBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}