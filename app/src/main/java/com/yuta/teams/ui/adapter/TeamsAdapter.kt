package com.yuta.teams.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.Team
import com.yuta.domain.model.TeamMember
import com.yuta.teams.ui.dialog.DeleteTeamDialog
import com.yuta.teams.ui.dialog.EditTeamDialog

class TeamsAdapter(
    context: Context,
    items: MutableList<List<TeamMember>>,
    private val fragment: Fragment,
    private val onUpdateCallback: () -> Unit
) : BaseAdapter<List<TeamMember>, BaseAdapter.ViewHolder<List<TeamMember>>>(context, items) {

    companion object {
        private const val PAGE_SIZE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    inner class CarouselViewHolder(itemView: View) : ViewHolder<List<TeamMember>>(itemView) {
        private val carouselNumberTextView: TextView = itemView.findViewById(R.id.teamName)
        private val imagePager: ViewPager2 = itemView.findViewById(R.id.imagePager)
        private val dotsLayout: LinearLayout = itemView.findViewById(R.id.dotsLayout)
        private val btnPrev: ImageButton = itemView.findViewById(R.id.btnPrev)
        private val btnNext: ImageButton = itemView.findViewById(R.id.btnNext)
        private val editTeam: Button = itemView.findViewById(R.id.editTeam)
        private val deleteTeam: Button = itemView.findViewById(R.id.deleteTeam)
        private lateinit var team: Team
        private var viewPagerCallback: ViewPager2.OnPageChangeCallback? = null

        override fun bind(carousel: List<TeamMember>) {
            val teamMember = carousel.firstOrNull() ?: return
            team = teamMember.team
            carouselNumberTextView.text = team.name

            val pages = getPagesList(carousel).toMutableList()
            val horizontalCarouselAdapter = HorizontalCarouselAdapter(context, pages)
            imagePager.adapter = horizontalCarouselAdapter

            dotsLayout.removeAllViews()
            setupDots(pages.size)
            setupNavButtons(pages)
            setupTeamButtons(team.leader.id)
            setupViewPagerCyclic(pages)
        }

        private fun <T> setupViewPagerCyclic(pages: List<List<T>>) {
            viewPagerCallback?.let { imagePager.unregisterOnPageChangeCallback(it) }
            viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
                private var myState = 0
                private var currentPosition = 0

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    if (myState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position) {
                        when (position) {
                            0 -> imagePager.setCurrentItem(pages.size - 1, true)
                            pages.size - 1 -> imagePager.setCurrentItem(0, true)
                        }
                    }
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    currentPosition = position
                    super.onPageSelected(position)
                    updateDots(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    myState = state
                    super.onPageScrollStateChanged(state)
                }
            }
            imagePager.registerOnPageChangeCallback(viewPagerCallback!!)
        }

        private fun setupTeamButtons(leaderId: Int) {
            if (leaderId == UserUtils.getUserId(context)) {
                editTeam.visibility = VISIBLE
                deleteTeam.visibility = VISIBLE
                editTeam.setOnClickListener { openEditTeamDialog() }
                deleteTeam.setOnClickListener { openDeleteTeamDialog() }
            } else {
                editTeam.visibility = GONE
                deleteTeam.visibility = GONE
                editTeam.setOnClickListener(null)
                deleteTeam.setOnClickListener(null)
            }
        }

        private fun openDeleteTeamDialog() {
            DeleteTeamDialog(fragment, team) { onUpdateCallback() }.start()
        }

        private fun openEditTeamDialog() {
            EditTeamDialog(fragment, team) { onUpdateCallback() }.start()
        }

        private fun setupDots(size: Int) {
            (0 until size).forEach { i ->
                val dot = ImageView(context)
                dot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_not_selected))

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)

                dotsLayout.addView(dot, params)
            }
            updateDots(0)
        }

        private fun updateDots(position: Int) {
            for (i in 0 until dotsLayout.childCount) {
                val dot = dotsLayout.getChildAt(i) as ImageView
                dot.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        if (i == position) R.drawable.dot_selected else R.drawable.dot_not_selected
                    )
                )
            }
        }

        private fun <T> setupNavButtons(pages: List<List<T>>) {
            if (pages.size < 2) {
                btnPrev.imageTintList = ContextCompat.getColorStateList(context, R.color.light_gray)
                btnNext.imageTintList = ContextCompat.getColorStateList(context, R.color.light_gray)
            }

            btnPrev.setOnClickListener {
                val currentPos = imagePager.currentItem
                if (currentPos > 0) {
                    imagePager.currentItem = currentPos - 1
                } else if (currentPos == 0) {
                    imagePager.currentItem = pages.size - 1
                }
            }

            btnNext.setOnClickListener {
                val currentPos = imagePager.currentItem
                val lastPos = imagePager.adapter?.itemCount?.minus(1) ?: 0

                if (currentPos < lastPos) {
                    imagePager.currentItem = currentPos + 1
                } else if (currentPos == lastPos) {
                    imagePager.currentItem = 0
                }
            }
        }

        private fun <T> getPagesList(list: List<T>): List<List<T>> {
            val pages = mutableListOf<List<T>>()
            for (i in list.indices step PAGE_SIZE) {
                val end = (i + PAGE_SIZE).coerceAtMost(list.size)
                pages.add(list.subList(i, end))
            }
            return pages
        }
    }
}
