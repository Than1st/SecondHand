@file:Suppress("SENSELESS_COMPARISON")

package com.group4.secondhand.ui.notifikasi

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group4.secondhand.data.model.ResponseNotification
import com.group4.secondhand.databinding.NotificationItemBinding
import com.group4.secondhand.ui.convertDate
import com.group4.secondhand.ui.currency
import com.group4.secondhand.ui.striketroughtText


class NotificationAdapter(
    private val onItemClick : OnClickListener
    ) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private val diffCallBack = object : DiffUtil.ItemCallback<ResponseNotification>(){
        override fun areItemsTheSame(
            oldItem: ResponseNotification,
            newItem: ResponseNotification
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResponseNotification,
            newItem: ResponseNotification
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this,diffCallBack)
    fun submitData(value:List<ResponseNotification>?) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(NotificationItemBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: NotificationItemBinding):
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: ResponseNotification){
            binding.apply {
                when (data.status) {
                    "bid" -> {
                        if (data.product != null){
                            if(data.receiverId == data.product.userId){
                                tvPesan.text = "Ada yang tawar produkmu!"
                            } else {
                                tvPesan.text = "Tawaranmu belum diterima oleh penjual, sabar ya!"
                            }
                        } else {
                            tvPesan.text = "Produk Sudah di hapus"
                        }
                    }
                    "declined" -> {
                        tvStatusProduk.text = "Produk Ditolak"
                        if (data.product != null){
                            if (data.receiverId == data.product.userId){
                                tvPesan.text = "Anda menolak Tawaran ini"
                            } else {
                                tvPesan.text = "Tawaran Anda ditolak oleh Penjual"
                            }
                        } else {
                            tvPesan.text = "Produk Sudah di hapus"
                        }
                    }
                    "accepted" -> {
                        tvStatusProduk.text = "Produk Diterima"
                        if (data.product != null){
                            if (data.receiverId == data.product.userId){
                                tvPesan.text = "Anda menerima Tawaran ini"
                            } else {
                                tvPesan.text = "Tawaran Anda diterima oleh Penjual"
                            }
                        } else {
                            tvPesan.text = "Produk Sudah di hapus"
                        }
                    }
                    else -> {
                        tvPesan.text = " "
                    }
                }
                tvHargaDitawarProduk.text =
                    if (data.status == "declined") "Ditolak " + currency(data.bidPrice)
                    else if(data.status == "accepted") "Diterima " + currency(data.bidPrice)
                    else "Ditawar " + currency(data.bidPrice)
                tvProdukName.text = data.productName
                tvHargaAwalProduk.apply {
                    text = striketroughtText(this, currency(data.basePrice.toInt()))
                }
                tvTanggal.text = convertDate(data.transactionDate)
                if (!data.read){
                    Glide.with(binding.root)
                        .load(data.imageUrl)
                        .centerCrop()
                        .into(ivProductImage)
                }
                root.setOnClickListener{
                    onItemClick.onClickItem(data)
                }
            }
        }
    }

    interface OnClickListener{
        fun onClickItem(data: ResponseNotification)
    }
}