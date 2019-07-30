package com.sagar.android.paymentgateway.ui.item_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sagar.android.paymentgateway.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import java.util.*

class ItemAdapter(val callback: CallBack) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    interface CallBack {
        fun buy()
    }

    inner class ViewHolder(
        private val listItemBinding: ListItemBinding
    ) : RecyclerView.ViewHolder(listItemBinding.root) {

        fun bind() {
            Picasso.get()
                .load(
                    "https://picsum.photos/300/200?" + Random().nextInt()
                )
                .into(
                    listItemBinding.appcompatImageView
                )

            listItemBinding.buttonBuy.setOnClickListener { callback.buy() }
        }
    }
}