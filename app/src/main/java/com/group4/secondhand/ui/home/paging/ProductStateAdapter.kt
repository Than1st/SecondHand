package com.group4.secondhand.ui.home.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.group4.secondhand.R
import com.group4.secondhand.databinding.LoadingItemBinding

class ProductStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ProductStateAdapter.ReposLoadStateViewHolder>() {

    inner class ReposLoadStateViewHolder(
        private val binding: LoadingItemBinding,
        retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
//            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ReposLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.loading_item, parent, false)
        val binding = LoadingItemBinding.bind(view)
        return ReposLoadStateViewHolder(binding, retry)
    }
}