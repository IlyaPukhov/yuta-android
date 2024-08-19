package com.yuta.teams.ui.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.UserDto

class TeamUserSearchAdapter(
    items: MutableList<UserDto>,
    dialog: Dialog,
    private val membersAdapter: TeamUserSearchAdapter? = null,
    private val onUpdateCallback: () -> Unit
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
            GlideUtils.loadImageToImageViewWithoutCaching(avatar, userDto.croppedPhoto)
            name.text = UserUtils.getFullName(userDto)

            membersAdapter?.let {
                buttonRemove.visibility = View.GONE
                buttonAdd.visibility = View.VISIBLE

                buttonAdd.setOnClickListener {
                    removeItem(userDto)
                    membersAdapter.insertItem(userDto)
                    onUpdateCallback()
                }
            } ?: run {
                buttonAdd.visibility = View.GONE
                buttonRemove.visibility = View.VISIBLE

                buttonRemove.setOnClickListener {
                    removeItem(userDto)
                    onUpdateCallback()
                }
            }
        }
    }
}
