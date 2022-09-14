package com.todocode.ultimequiz.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.todocode.ultimequiz.Models.Referral;
import com.todocode.ultimequiz.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyReferralsAdapter extends RecyclerView.Adapter<MyReferralsAdapter.CatViewHolder> {
    private List<Referral> referrals;
    private com.todocode.ultimequiz.Adapters.MyReferralsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(com.todocode.ultimequiz.Adapters.MyReferralsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public MyReferralsAdapter(List<Referral> referrals) {
        this.referrals = referrals;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_referral_layout, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.setDetails(referrals.get(position));
    }

    @Override
    public int getItemCount() {
        return referrals.size();
    }

    static public class CatViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private CircleImageView image;

        public CatViewHolder(@NonNull View itemView, final com.todocode.ultimequiz.Adapters.MyReferralsAdapter.OnItemClickListener listener) {
            super(itemView);
            username = itemView.findViewById(R.id.referral_username);
            image = itemView.findViewById(R.id.referral_image);
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
        void setDetails(Referral referral) {
            username.setText(referral.getPlayerUsername());
            Picasso.get().load(referral.getPlayerImageUrl()).fit().centerInside().into(image);
        }
    }
}




