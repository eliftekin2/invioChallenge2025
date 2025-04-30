package com.eliftekin.inviochallenge.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.eliftekin.inviochallenge.model.DataList;

public class CityDiffUtil extends DiffUtil.ItemCallback<DataList>{
    @Override
    public boolean areItemsTheSame(@NonNull DataList oldItem, @NonNull DataList newItem) {
        return oldItem.getCity().equals(newItem.getCity());
    }

    @Override
    public boolean areContentsTheSame(@NonNull DataList oldItem, @NonNull DataList newItem) {
        return oldItem.equals(newItem);
    }
}
