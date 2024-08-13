package com.yuta.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.yuta.app.MainActivity
import com.yuta.app.R
import com.yuta.app.viewmodel.MainViewModel
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils.getUserId
import com.yuta.domain.model.UserDto

class UserSearchAdapter(context: Context, items: MutableList<UserDto>) :
    BaseAdapter<UserDto, UserSearchAdapter.UserSearchViewHolder>(context, items) {

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

            val fullName = "${userDto.lastName} ${userDto.firstName}" + (userDto.patronymic?.let { " $it" } ?: "")
            name.text = fullName

            userLayout.setOnClickListener {
                val activity = context as MainActivity
                KeyboardUtils.hideKeyboard(activity, itemView)

                if (getUserId(activity) == userDto.id) {
                    activity.selectNavTab(R.id.profileFragment)
                } else {
                    val bundle = Bundle().apply { putInt("userId", userDto.id) }
                    activity.navigate(R.id.action_searchFragment_to_readonlyProfileFragment, bundle)

                    val mainViewModel = ViewModelProvider(activity)[MainViewModel::class.java]
                    mainViewModel.setReadonly(true)
                }
            }
        }
    }
}
