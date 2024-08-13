package com.yuta.__old.ui.adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuta.__old.R;
import com.yuta.__old.ui.adapter.layoutmanagers.SpanningLinearLayoutManager;
import com.yuta.common.ui.BaseAdapter;
import com.yuta.common.ui.AppDialog;
import com.yuta.__old.ui.dialog.project.ProjectDialog;
import com.yuta.common.util.GlideUtils;
import com.yuta.app.domain.model.entity.ProjectDto;
import com.yuta.app.domain.model.entity.User;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static android.app.DownloadManager.EXTRA_DOWNLOAD_ID;
import static android.app.DownloadManager.Request;
import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;
import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.content.ContextCompat.RECEIVER_EXPORTED;
import static com.yuta.common.util.UserUtils.getPath;
import static com.yuta.common.util.UserUtils.getUserId;

public class ProjectsAdapter extends BaseAdapter<ProjectDto, BaseAdapter.ViewHolder<ProjectDto>> {

    private final Fragment fragment;

    public ProjectsAdapter(Context context, List<ProjectDto> items, Fragment fragment) {
        super(context, items);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    public class ProjectViewHolder extends ViewHolder<ProjectDto> {
        private final TextView name;
        private final ImageView photo;
        private final Button buttonTechTask;
        private final Button menu;
        private final TextView status;
        private final TextView deadline;
        private final TextView description;
        private final RecyclerView teamPreview;
        private final TextView teamText;
        long downloadId;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            this.menu = itemView.findViewById(R.id.project_menu);
            this.name = itemView.findViewById(R.id.project_name);
            this.photo = itemView.findViewById(R.id.photo);
            this.buttonTechTask = itemView.findViewById(R.id.tech_task_button);
            this.status = itemView.findViewById(R.id.project_status);
            this.deadline = itemView.findViewById(R.id.project_deadline);
            this.description = itemView.findViewById(R.id.project_description);
            this.teamText = itemView.findViewById(R.id.team_text);

            this.teamPreview = itemView.findViewById(R.id.team_preview);
        }

        @Override
        public void bind(ProjectDto project) {
            setupTeamPreview(project, project.getManager());
            setupProjectFields(project);
            setupMenu(project);
        }

        private void setupTeamPreview(ProjectDto project, User manager) {
            List<User> teamMembers = new ArrayList<>();
            teamMembers.add(manager);
            if (project.getTeam() != null) {
                teamMembers.addAll(project.getTeam().getMembers());
            }
            teamPreview.setLayoutManager(new SpanningLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            teamPreview.setAdapter(new ProjectTeamPreviewAdapter(getContext(), teamMembers, itemView));
        }

        private void setupProjectFields(ProjectDto project) {
            GlideUtils.loadImageToImageViewWithoutCaching(photo, project.getPhoto());

            if (project.getTeam() != null) {
                String teamName = getContext().getString(R.string.team) + " \"" + project.getTeam().getName() + "\"";
                teamText.setText(teamName);
            }

            name.setText(project.getName());
            status.setText(String.format(" %s", project.getStatus()));
            deadline.setText(String.format(" %s", project.getStringDeadline()));
            description.setText(String.format(" %s", project.getDescription()));

            if (project.getTechnicalTask() != null) {
                buttonTechTask.setOnClickListener(v -> openTechTask(project.getTechnicalTask()));
                buttonTechTask.setText(R.string.tech_task_button);
                buttonTechTask.setEnabled(true);
            } else {
                buttonTechTask.setText(R.string.tech_task_not_exists);
                buttonTechTask.setEnabled(false);
            }
        }

        private void setupMenu(ProjectDto project) {
            if (project.getManager().getId() == getUserId(getContext())) {
                menu.setVisibility(VISIBLE);
                menu.setOnClickListener(v -> openMenu(project));
            } else {
                menu.setVisibility(GONE);
                menu.setOnClickListener(null);
            }
        }

        @SneakyThrows
        public void openTechTask(String path) {
            String filename = path.substring(path.lastIndexOf('/') + 1);

            File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "/" + filename);
            file.delete();

            Request request = new Request(
                    Uri.parse(getPath(path)))
                    .setTitle(filename)
                    .setDescription(getContext().getString(R.string.downloading))
                    .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, filename);

            DownloadManager manager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
            downloadId = manager.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        openPdf(file);
                        getContext().unregisterReceiver(this);
                    }
                }
            };
            ContextCompat.registerReceiver(getContext(), onComplete,
                    new IntentFilter(ACTION_DOWNLOAD_COMPLETE), RECEIVER_EXPORTED);
        }

        private void openPdf(File file) {
            Uri uri = FileProvider.getUriForFile(getContext(),
                    getContext().getApplicationContext().getPackageName() + ".provider", file);

            Intent intent = new Intent(ACTION_VIEW);
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            getContext().startActivity(intent);
        }

        private void openMenu(ProjectDto project) {
            AppDialog projectDialog = new ProjectDialog(getContext(), fragment, project);
            projectDialog.start();
        }
    }
}