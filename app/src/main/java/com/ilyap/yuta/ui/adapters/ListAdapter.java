package com.ilyap.yuta.ui.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.loadImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;

import java.util.List;

public class ListAdapter extends ArrayAdapter<User> {
    private final int layout;
    private final ArrayAdapter<User> adapterMembers;

    public ListAdapter(Context context, int layout, List<User> items, ArrayAdapter<User> adapterMembers) {
        super(context, layout, items);
        this.layout = layout;
        this.adapterMembers = adapterMembers;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ItemViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(layout, parent, false);

            holder = new ItemViewHolder();
            holder.name = view.findViewById(R.id.name);
            holder.avatar = view.findViewById(R.id.avatar);
            holder.buttonAdd = view.findViewById(R.id.btnAdd);
            holder.buttonRemove = view.findViewById(R.id.btnRemove);

            view.setTag(holder);
        } else {
            holder = (ItemViewHolder) view.getTag();
        }

        User user = getItem(position);
        if (user != null) {
            // TODO
            loadImage(getContext(), user.getPhoto(), holder.avatar);

            String userName = user.getLastName() + " " + user.getFirstName() + (user.getPatronymic() == null ? "" : " " + user.getPatronymic());
            holder.name.setText(userName);

            if (adapterMembers != null) {
                holder.buttonRemove.setVisibility(GONE);
                holder.buttonAdd.setVisibility(VISIBLE);
                holder.buttonAdd.setOnClickListener(v -> {
                    this.remove(user);
                    adapterMembers.add(user);
                });
            } else {
                holder.buttonAdd.setVisibility(GONE);
                holder.buttonRemove.setVisibility(VISIBLE);
                holder.buttonRemove.setOnClickListener(v -> this.remove(user));
            }
        }

        return view;
    }

    private static class ItemViewHolder {
        private TextView name;
        private ImageView avatar;
        private Button buttonAdd;
        private Button buttonRemove;
    }
}