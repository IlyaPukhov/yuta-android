package com.yuta.search.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yuta.app.MainActivity
import com.yuta.app.R
import com.yuta.common.callback.NavigationToProfileCallback
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.UserDto

class UserSearchAdapter(
    context: Context,
    items: MutableList<UserDto>
) : BaseAdapter<UserDto, BaseAdapter.ViewHolder<UserDto>>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    inner class UserSearchViewHolder(itemView: View) : ViewHolder<UserDto>(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val avatar: ImageView = itemView.findViewById(R.id.avatar)
        private val userLayout: View = itemView.findViewById(R.id.user_layout)

        override fun bind(userDto: UserDto) {
            GlideUtils.loadImageToImageViewWithCaching(avatar, userDto.croppedPhoto)

            name.text = UserUtils.getFullName(userDto)

            userLayout.setOnClickListener {
                val activity = context.applicationContext as MainActivity
                KeyboardUtils.hideKeyboard(activity, itemView)

                (activity as NavigationToProfileCallback).navigate(userDto.id)
            }
        }
    }
}
