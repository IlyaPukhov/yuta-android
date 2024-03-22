package com.ilyap.yuta.ui.adapters;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Project;
import com.ilyap.yuta.models.ProjectDto;
import com.ilyap.yuta.models.ProjectResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.project.ProjectDialog;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static android.app.DownloadManager.EXTRA_DOWNLOAD_ID;
import static android.app.DownloadManager.Request;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static com.ilyap.yuta.utils.UserUtils.loadImage;

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
        private final String TECH_TASK_NAME = getContext().getString(R.string.tech_task_filename);
        private final TextView name;
        private final ImageView photo;
        private final Button buttonTechTask;
        private final Button menu;
        private final TextView status;
        private final TextView deadline;
        private final TextView description;
        private final RecyclerView teamPreview;
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

            this.teamPreview = itemView.findViewById(R.id.team_preview);
        }

        @Override
        public void bind(ProjectDto project) {
            setupProjectFields(project);
            setupMenu(project);
            setupTeamPreview(project.getId());
        }

        private void setupTeamPreview(int projectId) {
            RequestViewModel viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);
            viewModel.getResultLiveData().removeObservers(fragment.getViewLifecycleOwner());
            viewModel.getProject(projectId);

            viewModel.getResultLiveData().observe(fragment.getViewLifecycleOwner(), result -> {
                if (!(result instanceof ProjectResponse)) return;
                Project project = ((ProjectResponse) result).getProject();

                List<User> teamMembers = new ArrayList<>();
                teamMembers.add(project.getTeam().getLeader());
                teamMembers.addAll(project.getTeam().getMembers());

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                teamPreview.setLayoutManager(layoutManager);
                teamPreview.setAdapter(new ProjectTeamPreviewAdapter(getContext(), teamMembers));
            });
        }

        private void setupProjectFields(ProjectDto project) {
            loadImage(getContext(), project.getPhotoUrl(), photo);
            buttonTechTask.setOnClickListener(v -> openTechTask(project.getTechnicalTaskUrl()));

            name.setText(project.getName());
            status.setText(project.getStatus());
            deadline.setText(project.getStringDeadline());
            description.setText(project.getDescription());
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
            Request request = new Request(
                    Uri.parse(path))
                    .setTitle(getContext().getString(R.string.tech_task))
                    .setDescription(getContext().getString(R.string.downloading))
                    .setNotificationVisibility(Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, TECH_TASK_NAME);

            DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = manager.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        openPdf();
                    }
                }
            };
            ContextCompat.registerReceiver(getContext(), onComplete,
                    new IntentFilter(ACTION_DOWNLOAD_COMPLETE), RECEIVER_NOT_EXPORTED);
        }

        private void openPdf() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + TECH_TASK_NAME
            );
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getContext().startActivity(intent);
        }

        private void openMenu(ProjectDto project) {
            CustomDialog projectDialog = new ProjectDialog(getContext(), fragment, project);
            projectDialog.start();
        }
    }
}