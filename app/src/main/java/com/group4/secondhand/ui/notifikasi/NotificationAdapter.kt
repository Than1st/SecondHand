package com.group4.secondhand.ui.notifikasi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.group4.secondhand.data.model.ResponseNotification
import com.group4.secondhand.databinding.NotificationItemBinding
import com.group4.secondhand.ui.convertDate

class NotificationAdapter(private val onItemClick : NotificationAdapter.OnClickListener) :
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
        fun bind(data: ResponseNotification){
            binding.apply {
                tvHargaDitawarProduk.text = data.bidPrice.toString()
                tvTanggal.text = convertDate(data.transactionDate)
                Glide.with(binding.root)
                    .load(data.imageUrl)
                    .centerCrop()
                    .into(ivProductImage)
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