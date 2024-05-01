package com.muhammadfakharabbas.i210448


import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.muhammadfakharabbas.i210448.R
import com.muhammadfakharabbas.i210448.pg15chat_person
import com.muhammadfakharabbas.i210448.User
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    /*private var onItemClickListener: ((User) -> Unit)? = null
    var firebaseUser: FirebaseUser? = null
    fun setOnItemClickListener(listener: (User) -> Unit) {
        this.onItemClickListener = listener
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUserName.text = user.name
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.profile_image).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {
          //  onItemClickListener?.invoke(user)
            val intent = Intent(context,pg15chat_person::class.java)
            intent.putExtra("userId",user.userId)
            intent.putExtra("userName",user.name)
            val msg = "User ID: ${user.userId}, Name: ${user.name}, Position: ${position.toString()}"
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.userName)
        val imgUser: CircleImageView = view.findViewById(R.id.userImage)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
    }
}
