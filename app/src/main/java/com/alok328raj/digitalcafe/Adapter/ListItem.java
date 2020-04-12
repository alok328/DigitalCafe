package com.alok328raj.digitalcafe.Adapter;

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TRANSACTION = 1;

    abstract public int getType();

}
