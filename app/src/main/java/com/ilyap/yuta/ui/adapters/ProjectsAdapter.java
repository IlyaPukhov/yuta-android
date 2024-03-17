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
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.ProjectDto;
import lombok.SneakyThrows;

import java.util.List;

import static androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED;
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
        long downloadId;
        private final String TECH_TASK_NAME = getContext().getString(R.string.tech_task_filename);

        private final TextView name;
        private final ImageView photo;
        private final Button button;
        private final TextView status;
        private final TextView deadline;
        private final TextView description;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.project_name);
            this.photo = itemView.findViewById(R.id.photo);
            this.button = itemView.findViewById(R.id.tech_task_button);
            this.status = itemView.findViewById(R.id.project_status);
            this.deadline = itemView.findViewById(R.id.project_deadline);
            this.description = itemView.findViewById(R.id.project_description);
        }

        @Override
        public void bind(ProjectDto project) {
            loadImage(getContext(), project.getPhotoUrl(), photo);
            button.setOnClickListener(v -> openTechTask(project.getTechnicalTaskUrl()));

            name.setText(project.getName());
            status.setText(project.getStatus());
            deadline.setText(project.getStringDeadline());
            description.setText(project.getDescription());

            //TODO TEAM
        }

        @SneakyThrows
        public void openTechTask(String path) {
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(path))
                    .setTitle(getContext().getString(R.string.tech_task))
                    .setDescription(getContext().getString(R.string.downloading))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, TECH_TASK_NAME);

            DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            downloadId = manager.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (id == downloadId) {
                        openPdf();
                    }
                }
            };
            ContextCompat.registerReceiver(getContext(), onComplete,
                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), RECEIVER_NOT_EXPORTED);
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
    }
}