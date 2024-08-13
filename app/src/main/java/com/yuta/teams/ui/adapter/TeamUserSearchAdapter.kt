package com.yuta.teams.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yuta.app.R
import com.yuta.common.ui.AppDialog
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.GlideUtils.loadImageToImageViewWithoutCaching
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.UserDto
import com.yuta.teams.ui.dialog.CreateTeamDialog

class TeamUserSearchAdapter(
    private val dialog: AppDialog,
    items: MutableList<UserDto>,
    private val membersAdapter: TeamUserSearchAdapter? = null
) : BaseAdapter<UserDto, BaseAdapter.ViewHolder<UserDto>>(dialog.context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_search_in_team, parent, false)
        return UserViewHolder(view)
    }

    inner class UserViewHolder(itemView: View) : ViewHolder<UserDto>(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val avatar: ImageView = itemView.findViewById(R.id.avatar)
        private val buttonAdd: Button = itemView.findViewById(R.id.btnAdd)
        private val buttonRemove: Button = itemView.findViewById(R.id.btnRemove)

        override fun bind(userDto: UserDto) {
            loadImageToImageViewWithoutCaching(avatar, userDto.croppedPhoto)
            name.text = UserUtils.getFullName(userDto)

            if (membersAdapter != null) {
                buttonRemove.visibility = View.GONE
                buttonAdd.visibility = View.VISIBLE

                buttonAdd.setOnClickListener {
                    removeItem(userDto)
                    membersAdapter.insertItem(userDto)
                    (dialog as? CreateTeamDialog)?.updateAddedTextVisibility()
                }
            } else {
                buttonAdd.visibility = View.GONE
                buttonRemove.visibility = View.VISIBLE

                buttonRemove.setOnClickListener {
                    removeItem(userDto)
                    (dialog as? CreateTeamDialog)?.updateAddedTextVisibility()
                }
            }
        }
    }
}
