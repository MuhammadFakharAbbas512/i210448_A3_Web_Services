package com.muhammadfakharabbas.i210448

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MentorAdapter(private val context: Context, private val mentorList: ArrayList<MentorModel>) :
RecyclerView.Adapter<MentorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mentor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorAdapter.ViewHolder, position: Int) {
        val mentor = mentorList[position]
        val mid = mentor.mid
        holder.mentorName.text = mentor.name
        holder.mentorDesc.text = mentor.description
        holder.mentorStatus.text = mentor.status
        holder.fee.text = mentor.fee
      Glide.with(context).load(mentor.dpUrl).placeholder(R.drawable.profile_image).into(holder.imgUser)

        holder.layoutMentor.setOnClickListener {
            //  onItemClickListener?.invoke(user)
            val intent = Intent(context,pg10mentor::class.java)
           /* val msg = "mID: ${mentor.mid}, mName: ${mentor.name}, Position: ${position.toString()}"
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()*/
            intent.putExtra("mid",mentor.mid )
            intent.putExtra("mentorName", mentor.name)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mentorList.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       // val mentorId: TextView = view.findViewById(R.id.mentorid)
        val mentorName: TextView = view.findViewById(R.id.mentorname)
        val mentorDesc: TextView = view.findViewById(R.id.mentordesc)
        val mentorStatus: TextView = view.findViewById(R.id.mentorstatus)
        val imgUser: ImageView = view.findViewById(R.id.mentorimg)
        var fee = itemView.findViewById<TextView>(R.id.mentorfee)
        val layoutMentor: LinearLayout = view.findViewById(R.id.layoutMentor)
    }
}