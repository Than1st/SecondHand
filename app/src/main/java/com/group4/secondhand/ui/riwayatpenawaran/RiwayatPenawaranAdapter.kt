@file:Suppress("SENSELESS_COMPARISON")

package com.group4.secondhand.ui.riwayatpenawaran

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
import com.group4.secondhand.data.model.ResponseGetBuyerOrder
import com.group4.secondhand.databinding.ItemRiwayatPenawaranBinding
import com.group4.secondhand.ui.convertDate
import com.group4.secondhand.ui.currency

class RiwayatPenawaranAdapter(private val onItemClick : OnClickListener) : RecyclerView.Adapter<RiwayatPenawaranAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseGetBuyerOrder>(){
        override fun areItemsTheSame(
            oldItem: ResponseGetBuyerOrder,
            newItem: ResponseGetBuyerOrder
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseGetBuyerOrder,
            newItem: ResponseGetBuyerOrder
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this,diffCallBack)

    fun submitData(value:List<ResponseGetBuyerOrder>?) = differ.submitList(value)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatPenawaranAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemRiwayatPenawaranBinding.inflate(inflater,parent,false))
    }

    inner class ViewHolder(private val binding: ItemRiwayatPenawaranBinding):
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: ResponseGetBuyerOrder){
            binding.apply {
                Glide.with(binding.root)
                    .load(data.product.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(12))
                    .placeholder(R.drawable.default_image)
                    .centerCrop()
                    .into(ivProductImage)
                tvProdukName.text = data.product.name
                tvHargaAwalProduk.text = currency(data.basePrice)
                tvHargaDitawarProduk.text = "Tawaranmu ${currency(data.price)}"
                root.setOnClickListener {
                    onItemClick.onClickItem(data)
                }
                if (data.transaction_date != null){
                    tvTanggal.text = convertDate(data.transaction_date)
                } else {
                    tvTanggal.text = "-"
                }
                when (data.status) {
                    "pending" -> {
                        tvStatus.text = "Menunggu Konfirmasi"
                    }
                    "accepted" -> {
                        root.alpha = 0.5f
                        tvStatus.text = "Tawaran Diterima"
                    }
                    else -> {
                        root.alpha = 0.5f
                        tvStatus.text = "Tawaran Ditolak"
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RiwayatPenawaranAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnClickListener{
        fun onClickItem(data: ResponseGetBuyerOrder)
    }

}