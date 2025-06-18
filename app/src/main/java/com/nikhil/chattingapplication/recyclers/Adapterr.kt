package com.nikhil.chattingapplication.recyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.RecyclerItemBinding
import com.nikhil.chattingapplication.dataclasses.User
import com.nikhil.chattingapplication.interfaces.UserInter

class Adapterr (val list:List<User>,var clickinter:UserInter):RecyclerView.Adapter<Adapterr.ViewHolder>(){
    inner class ViewHolder(val binding:RecyclerItemBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bindata(position: Int){
            val user=list[position]
            binding.name.setText(list[position].name)
            binding.age.text=user.age?.toString()
            Glide.with(itemView.context)
                .load(user.profileimageurl)
                .placeholder(R.drawable.baseline_home_24)
                .into(binding.imgUserProfile)
            binding.card.setOnClickListener{
                clickinter.chatclick(user,position)
            }
        }

        }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding=RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindata(position)
    }
}