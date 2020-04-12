package com.alok328raj.digitalcafe.Adapter;

import androidx.annotation.NonNull;

import java.util.Date;

public class HeaderItem extends ListItem {

    @NonNull
    private Date date;

    public HeaderItem(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
