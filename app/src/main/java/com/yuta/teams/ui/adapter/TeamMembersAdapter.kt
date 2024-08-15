package com.yuta.teams.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yuta.app.MainActivity
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.TeamMember

class TeamMembersAdapter(
    context: Context,
    items: MutableList<TeamMember>
) : BaseAdapter<TeamMember, BaseAdapter.ViewHolder<TeamMember>>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team_member_card, parent, false)
        return TeamMemberViewHolder(view)
    }

    inner class TeamMemberViewHolder(itemView: View) : ViewHolder<TeamMember>(itemView) {
        private val card: View = itemView.findViewById(R.id.card)
        private val avatar: ImageView = itemView.findViewById(R.id.avatar)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val teamLeaderIcon: ImageView = itemView.findViewById(R.id.teamLeaderIcon)

        override fun bind(teamMember: TeamMember) {
            val userDto = teamMember.member
            val leader = teamMember.team.leader
            GlideUtils.loadImageToImageViewWithCaching(avatar, userDto.croppedPhoto)

            if (userDto == leader) {
                teamLeaderIcon.visibility = VISIBLE
            }

            name.text = UserUtils.getFullName(userDto)

            card.setOnClickListener { (context.applicationContext as MainActivity).navigate(userDto.id) }
        }
    }
}
