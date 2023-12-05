package com.ilyap.yuta.ui.adapters;

import java.util.List;

public interface ListAdapterUpdater<T> {
    void updateList(List<T> items);
}