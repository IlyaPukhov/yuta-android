package com.yuta.projects.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.domain.model.Team

class ProjectTeamSearchAdapter(
    items: MutableList<Team>,
    private val addedTeamSearchAdapter: ProjectTeamSearchAdapter? = null,
    private val onUpdateCallback: () -> Unit
) : BaseAdapter<Team, BaseAdapter.ViewHolder<Team>>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team_search_in_project, parent, false)
        return TeamViewHolder(view)
    }

    inner class TeamViewHolder(itemView: View) : ViewHolder<Team>(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val buttonAdd: Button = itemView.findViewById(R.id.add_team)
        private val buttonRemove: Button = itemView.findViewById(R.id.remove_team)

        override fun bind(team: Team) {
            name.text = team.name

            addedTeamSearchAdapter?.let {
                buttonRemove.visibility = GONE
                buttonAdd.visibility = VISIBLE

                buttonAdd.setOnClickListener {
                    if (addedTeamSearchAdapter.items.isNotEmpty()) {
                        addedTeamSearchAdapter.removeItem(addedTeamSearchAdapter.items[0])
                    }

                    removeItem(team)
                    addedTeamSearchAdapter.insertItem(team)
                    onUpdateCallback()
                }
            } ?: run {
                buttonAdd.visibility = GONE
                buttonRemove.visibility = VISIBLE

                buttonRemove.setOnClickListener {
                    removeItem(team)
                    onUpdateCallback()
                }
            }
        }
    }
}
