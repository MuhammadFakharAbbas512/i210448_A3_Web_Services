package com.muhammadfakharabbas.i210448

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SearchRecyclerAdapter (list: ArrayList<MentorModel>, c: Context) :
    RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder>(){
    var list = list
    var context = c
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater
            .from(context)
            .inflate(R.layout.mentor_rv, parent, false)
        return MyViewHolder(v)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /* // original code without dp
         holder.name.text = list.get(position).name
         holder.phone.text = list.get(position).phno
         holder.email.text = list.get(position).email*/
        val currentItem = list[position]
        holder.mid.text = currentItem.mid
        holder.name.text = currentItem.name
        holder.desc.text = currentItem.description
        holder.fee.text = currentItem.fee
        holder.status.text = currentItem.status
        // Load image into dp ImageView using Picasso
        // Picasso.get().load(currentItem.img).into(holder.dp)
    }
    //holder.dp =  list.get(position).dp


    class MyViewHolder: RecyclerView.ViewHolder{
        constructor(itemView: View) : super(itemView)
        var row= itemView.findViewById<LinearLayout>(R.id.mentor_rv)
        var mid = itemView.findViewById<TextView>(R.id.mentorName)
        var name = itemView.findViewById<TextView>(R.id.mentorName)
        var desc = itemView.findViewById<TextView>(R.id.desc)
        var fee = itemView.findViewById<TextView>(R.id.fee)
        var status = itemView.findViewById<TextView>(R.id.status)
       // var dp = itemView.findViewById<ImageView>(R.id.dp)
    }

}