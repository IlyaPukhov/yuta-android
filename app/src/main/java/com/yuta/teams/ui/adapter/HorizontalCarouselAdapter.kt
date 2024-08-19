package com.yuta.teams.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.ui.SpanningLinearLayoutManager
import com.yuta.domain.model.TeamMember

class HorizontalCarouselAdapter(
    private val context: Context,
    items: MutableList<List<TeamMember>>
) : BaseAdapter<List<TeamMember>, BaseAdapter.ViewHolder<List<TeamMember>>>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_carousel, parent, false).apply {
                layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        return HorizontalViewHolder(view)
    }

    inner class HorizontalViewHolder(itemView: View) : ViewHolder<List<TeamMember>>(itemView) {
        private val horizontalRecyclerView: RecyclerView = itemView.findViewById(R.id.horizontalRecyclerView)

        override fun bind(members: List<TeamMember>) {
            horizontalRecyclerView.layoutManager =
                SpanningLinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            horizontalRecyclerView.adapter = TeamMembersAdapter(context, members.toMutableList())
        }
    }
}
