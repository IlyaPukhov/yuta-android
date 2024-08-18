package com.yuta.projects.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.yuta.app.MainActivity
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.GlideUtils
import com.yuta.domain.model.UserDto

class ProjectMemberViewHolder(
    itemView: View,
    val context: Context,
    private val managerId: Int
) : BaseAdapter.ViewHolder<UserDto>(itemView) {

    private val avatar: ImageView = itemView.findViewById(R.id.avatar)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val member: View = itemView.findViewById(R.id.member)
    private val teamLeaderIcon: ImageView = itemView.findViewById(R.id.teamLeaderIcon)

    @SuppressLint("SetTextI18n")
    override fun bind(user: UserDto) {
        avatar.visibility = VISIBLE
        GlideUtils.loadImageToImageViewWithCaching(avatar, user.croppedPhoto)

        if (managerId == user.id) {
            teamLeaderIcon.visibility = VISIBLE
        }

        name.text = "${user.lastName} ${user.firstName}"

        member.setOnClickListener { (context.applicationContext as MainActivity).navigate(user.id) }
    }
}
