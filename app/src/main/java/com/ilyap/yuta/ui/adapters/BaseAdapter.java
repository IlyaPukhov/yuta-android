package com.ilyap.yuta.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T, VH extends BaseAdapter.ViewHolder<T>> extends RecyclerView.Adapter<VH> implements ListAdapterUpdater<T> {
    private final List<T> items;
    private final Context context;

    public BaseAdapter(Context context, List<T> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(items.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateList(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void removeItem(T item) {
        int position = this.items.indexOf(item);
        this.items.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, getItemCount());
    }

    public void insertItem(T item) {
        this.items.add(item);
        int position = this.items.indexOf(item);
        this.notifyItemInserted(position);
        this.notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Context getContext() {
        return context;
    }

    public abstract static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(T item);
    }
}