package com.nikhil.chattingapplication.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.MessageItemBinding
import com.nikhil.chattingapplication.databinding.RecyclerItemBinding
import com.nikhil.chattingapplication.dataclasses.Message

class MessageAdapter(val list:List<Message>,private val currentUserId:String):RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: MessageItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            if (message.senderid == currentUserId) {
                binding.tv2.visibility = View.VISIBLE
                binding.tv.visibility = View.GONE
                binding.tv2.text = message.message
            } else {
                binding.tv.visibility = View.VISIBLE
                binding.tv2.visibility = View.GONE
                binding.tv.text = message.message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].senderid==currentUserId)1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {

        val binding=MessageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}