package com.ilyap.yutaold.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

@Getter
@AllArgsConstructor
public abstract class BaseAdapter<T, VH extends BaseAdapter.ViewHolder<T>> extends RecyclerView.Adapter<VH> implements ListAdapterUpdater<T> {
    private final Context context;
    private final List<T> items;

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
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
        this.notifyItemRemoved(position);
        this.items.remove(position);
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

    public abstract static class ViewHolder<T> extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(T item);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        View view = new View(context);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }
}