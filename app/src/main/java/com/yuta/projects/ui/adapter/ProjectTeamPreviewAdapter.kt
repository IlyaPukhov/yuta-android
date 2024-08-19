package com.yuta.projects.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.ui.GridSpacingItemDecoration
import com.yuta.domain.model.UserDto

class ProjectTeamPreviewAdapter(
    private val context: Context,
    items: MutableList<UserDto>,
    private val projectView: View
) : BaseAdapter<UserDto, BaseAdapter.ViewHolder<UserDto>>(items) {

    companion object {
        private const val MAX_MEMBERS_COUNT = 4
        private const val ENOUGH_USERS = 0
        private const val MORE_USERS = 1
        private const val ANIMATION_DURATION = 500L
        private const val MINIMIZE_Y = 10
    }

    private val itemDecoration = GridSpacingItemDecoration(MAX_MEMBERS_COUNT, 15, true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<UserDto> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_member, parent, false)
        return when (viewType) {
            MORE_USERS -> ProjectMoreMembersViewHolder(view)
            else -> ProjectMemberViewHolder(view, context, items[0].id)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.size > MAX_MEMBERS_COUNT && position == MAX_MEMBERS_COUNT - 1) MORE_USERS else ENOUGH_USERS
    }

    override fun getItemCount(): Int {
        return minOf(items.size, MAX_MEMBERS_COUNT)
    }

    inner class ProjectMoreMembersViewHolder(itemView: View) : ViewHolder<UserDto>(itemView) {
        private val remainingMembersCount: TextView = itemView.findViewById(R.id.plus_members)
        private val member: View = itemView.findViewById(R.id.member)
        private val teamContainer: View = projectView.findViewById(R.id.team_container)
        private val closeButton: View = projectView.findViewById(R.id.close)
        private val teamRecyclerView: RecyclerView = projectView.findViewById(R.id.team_list)

        init {
            if (teamRecyclerView.itemDecorationCount == 0) {
                teamRecyclerView.addItemDecoration(itemDecoration)
            }
            teamRecyclerView.setHasFixedSize(true)
        }

        override fun bind(item: UserDto) {
            remainingMembersCount.visibility = VISIBLE
            val text = "+${items.size - MAX_MEMBERS_COUNT + 1}"
            remainingMembersCount.text = text

            member.setOnClickListener {
                setupFullTeamView(items)
                slideUp(teamContainer)
            }

            closeButton.setOnClickListener {
                slideDown(teamContainer)
            }
        }

        private fun setupFullTeamView(team: List<UserDto>) {
            teamRecyclerView.layoutManager = GridLayoutManager(context, MAX_MEMBERS_COUNT)
            teamRecyclerView.adapter = ProjectFullTeamAdapter(context, team.toMutableList())
            teamContainer.translationY = MINIMIZE_Y.toFloat()
        }

        private fun slideUp(view: View) {
            view.visibility = VISIBLE
            val animate = TranslateAnimation(0f, 0f, view.height.toFloat(), MINIMIZE_Y.toFloat()).apply {
                duration = ANIMATION_DURATION
                fillAfter = true
            }
            view.startAnimation(animate)
        }

        private fun slideDown(view: View) {
            val animate = TranslateAnimation(0f, 0f, MINIMIZE_Y.toFloat(), view.height.toFloat()).apply {
                duration = ANIMATION_DURATION
            }
            view.startAnimation(animate)
            view.visibility = INVISIBLE
        }
    }
}
