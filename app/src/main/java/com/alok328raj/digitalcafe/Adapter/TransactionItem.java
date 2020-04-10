package com.alok328raj.digitalcafe.Adapter;

import androidx.annotation.NonNull;

import com.alok328raj.digitalcafe.API.Model.transaction.Transaction;

public class TransactionItem extends ListItem {

    @NonNull
    private Transaction transaction;

    public TransactionItem(@NonNull Transaction transaction) {
        this.transaction = transaction;
    }

    @NonNull
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public int getType() {
        return TYPE_TRANSACTION;
    }
}
