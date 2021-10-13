package com.example.mooglekotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mooglekotlin.databinding.ActivityMessageBinding

class MessageAdapter: ListAdapter<Message, MessageAdapter.ItemHolder>(ItemComparator()) {



    class ItemHolder(private val binding: ActivityMessageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message) = with(binding){
            tvUserName.text = message.name
            tvMessage.text = message.message


        }
    companion object{
        fun create(parent: ViewGroup): ItemHolder{
            return ItemHolder(ActivityMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }
    }
    }
    class ItemComparator : DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
     holder.bind(getItem(position))
    }
}