package com.alok328raj.digitalcafe.Adapter;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alok328raj.digitalcafe.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_header = (TextView) itemView.findViewById(R.id.txt_header);
        }

    }

    private static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, priceTV, balanceTV;

        TransactionViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            priceTV = itemView.findViewById(R.id.priceTextView);
            balanceTV = itemView.findViewById(R.id.balTextView);
        }

    }

    @NonNull
    private List<ListItem> items = Collections.emptyList();

    public TransactionsAdapter(@NonNull List<ListItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = inflater.inflate(R.layout.view_list_item_header, parent, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_TRANSACTION: {
                View itemView = inflater.inflate(R.layout.view_list_item_event, parent, false);
                return new TransactionViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                HeaderItem header = (HeaderItem) items.get(position);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                // your logic here
                SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                String date = spf.format(header.getDate());
                holder.txt_header.setText(date);
                break;
            }
            case ListItem.TYPE_TRANSACTION: {
                TransactionItem transaction = (TransactionItem) items.get(position);
                TransactionViewHolder holder = (TransactionViewHolder) viewHolder;
                // your logic here
                DecimalFormat form = new DecimalFormat("0.00");
                String price = form.format(transaction.getTransaction().getPrice());
                holder.txt_title.setText(transaction.getTransaction().getMenu());
                holder.priceTV.setText(String.format("Rs. %s", price));
//                String bal = form.format(transaction.getTransaction().getBalance());
                holder.balanceTV.setText(String.format("Rs. %1.2f", transaction.getTransaction().getBalance()));
                break;
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}
