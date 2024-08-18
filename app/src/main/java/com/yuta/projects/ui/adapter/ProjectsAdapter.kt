package com.yuta.projects.ui.adapter

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.BaseAdapter
import com.yuta.common.ui.SpanningLinearLayoutManager
import com.yuta.common.util.GlideUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.ProjectDto
import com.yuta.domain.model.UserDto
import com.yuta.projects.ui.dialog.ProjectMenuDialog
import java.io.File

class ProjectsAdapter(
    context: Context,
    items: MutableList<ProjectDto>,
    private val fragment: Fragment
) : BaseAdapter<ProjectDto, ProjectsAdapter.ProjectViewHolder>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    inner class ProjectViewHolder(itemView: View) : ViewHolder<ProjectDto>(itemView) {
        private val name: TextView = itemView.findViewById(R.id.project_name)
        private val photo: ImageView = itemView.findViewById(R.id.photo)
        private val buttonTechTask: Button = itemView.findViewById(R.id.tech_task_button)
        private val menu: Button = itemView.findViewById(R.id.project_menu)
        private val status: TextView = itemView.findViewById(R.id.project_status)
        private val deadline: TextView = itemView.findViewById(R.id.project_deadline)
        private val description: TextView = itemView.findViewById(R.id.project_description)
        private val teamPreview: RecyclerView = itemView.findViewById(R.id.team_preview)
        private val teamText: TextView = itemView.findViewById(R.id.team_text)
        private var downloadId: Long = -1

        override fun bind(project: ProjectDto) {
            setupTeamPreview(project, project.manager)
            setupProjectFields(project)
            setupMenu(project)
        }

        private fun setupTeamPreview(project: ProjectDto, manager: UserDto) {
            val teamMembers = mutableListOf(manager).apply {
                project.team?.members?.let { addAll(it) }
            }
            teamPreview.layoutManager = SpanningLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            teamPreview.adapter = ProjectTeamPreviewAdapter(context, teamMembers, itemView)
        }

        private fun setupProjectFields(project: ProjectDto) {
            GlideUtils.loadImageToImageViewWithoutCaching(photo, project.photo ?: return)

            project.team?.let {
                teamText.text = "${context.getString(R.string.team)} \"${it.name}\""
            }

            name.text = project.name
            status.text = " ${project.status}"
            deadline.text = " ${project.stringDeadline}"
            description.text = " ${project.description}"

            buttonTechTask.apply {
                if (project.technicalTask != null) {
                    setOnClickListener { openTechTask(project.technicalTask!!) }
                    text = context.getString(R.string.tech_task_button)
                    isEnabled = true
                } else {
                    text = context.getString(R.string.tech_task_not_exists)
                    isEnabled = false
                }
            }
        }

        private fun setupMenu(project: ProjectDto) {
            if (project.manager.id == UserUtils.getUserId(context)) {
                menu.visibility = VISIBLE
                menu.setOnClickListener { openMenu(project) }
            } else {
                menu.visibility = GONE
                menu.setOnClickListener(null)
            }
        }

        private fun openTechTask(path: String) {
            val filename = path.substringAfterLast('/')

            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename)
            file.delete()

            val request = DownloadManager.Request(Uri.parse(UserUtils.getPath(path)))
                .setTitle(filename)
                .setDescription(context.getString(R.string.downloading))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = manager.enqueue(request)

            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                        openPdf(file)
                        context.unregisterReceiver(this)
                    }
                }
            }

            ContextCompat.registerReceiver(
                context, onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                ContextCompat.RECEIVER_EXPORTED
            )
        }

        private fun openPdf(file: File) {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(intent)
        }

        private fun openMenu(project: ProjectDto) {
            ProjectMenuDialog(context, fragment, project).start()
        }
    }
}
