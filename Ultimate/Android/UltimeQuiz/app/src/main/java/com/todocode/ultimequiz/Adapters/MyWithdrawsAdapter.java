package com.todocode.ultimequiz.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todocode.ultimequiz.Models.Withdraw;
import com.todocode.ultimequiz.R;

import java.util.List;

public class MyWithdrawsAdapter extends RecyclerView.Adapter<MyWithdrawsAdapter.CatViewHolder> {
    private List<Withdraw> withdraws;
    private com.todocode.ultimequiz.Adapters.MyWithdrawsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(com.todocode.ultimequiz.Adapters.MyWithdrawsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public MyWithdrawsAdapter(List<Withdraw> withdraws) {
        this.withdraws = withdraws;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_withdraw_layout, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.setDetails(withdraws.get(position));
    }

    @Override
    public int getItemCount() {
        return withdraws.size();
    }

    static public class CatViewHolder extends RecyclerView.ViewHolder {
        private TextView amount, date;
        private TextView status;

        public CatViewHolder(@NonNull View itemView, final com.todocode.ultimequiz.Adapters.MyWithdrawsAdapter.OnItemClickListener listener) {
            super(itemView);
            amount = itemView.findViewById(R.id.withdraw_amount_and_method);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        @SuppressLint("SetTextI18n")
        void setDetails(Withdraw withdraw) {
            amount.setText(withdraw.getAmount()+ " via " + withdraw.getPaymentMethod());
            date.setText(withdraw.getDate());
            status.setText(withdraw.getStatus());
            if (withdraw.getStatus().equals("paid")) {
                status.setBackgroundColor(Color.parseColor("#6B02BC65"));
            } else if (withdraw.getStatus().equals("rejected")) {
                status.setBackgroundColor(Color.parseColor("#75DC0101"));
            }
        }
    }
}



