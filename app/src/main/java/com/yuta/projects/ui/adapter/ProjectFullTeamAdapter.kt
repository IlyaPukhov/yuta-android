package com.yuta.projects.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.domain.model.UserDto

class ProjectFullTeamAdapter(
    private val context: Context,
    items: MutableList<UserDto>
) : BaseAdapter<UserDto, BaseAdapter.ViewHolder<UserDto>>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<UserDto> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_member, parent, false)
        return ProjectMemberViewHolder(view, context, items[0].id)
    }
}
