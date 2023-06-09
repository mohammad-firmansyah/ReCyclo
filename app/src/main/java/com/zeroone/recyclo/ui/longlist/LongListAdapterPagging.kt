package com.zeroone.recyclo.ui.longlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeroone.recyclo.R
import com.zeroone.recyclo.api.response.DataItem
import com.zeroone.recyclo.ui.detail.DetailActivity

class LongListAdapterPagging :  PagingDataAdapter<DataItem, LongListAdapterPagging.ListViewHolder>(
    DIFF_CALLBACK) {


     var onItemClick : ((DataItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.goods_item, parent, false)
        return ListViewHolder(view)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val goods = getItem(position)
        if (goods != null) {
            Glide.with(holder.itemView.context)
                .load(goods.image1)
                .centerCrop()
                .into(holder.hero)
        }

        holder.name.text = goods?.title


        holder.itemView.setOnClickListener {
            getItem(position)?.let { it1 -> onItemClick?.invoke(it1) }
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("goods",goods)
            holder.itemView.context.startActivity(intent)
        }

        holder.price.text = goods?.price
        holder.city.text = goods?.userId
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.title_goods)
        var hero: ImageView = itemView.findViewById(R.id.hero)
        var price: TextView = itemView.findViewById(R.id.price)
        var city: TextView = itemView.findViewById(R.id.city)
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}