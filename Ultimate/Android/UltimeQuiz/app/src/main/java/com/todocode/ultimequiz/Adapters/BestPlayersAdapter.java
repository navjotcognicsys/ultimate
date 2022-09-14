package com.todocode.ultimequiz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.todocode.ultimequiz.Models.Player;
import com.todocode.ultimequiz.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BestPlayersAdapter extends  RecyclerView.Adapter<BestPlayersAdapter.PlayersHolder> {

    private Context context;
    private List<Player> players;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class PlayersHolder extends RecyclerView.ViewHolder {
        private TextView playerName;
        private CircleImageView playerImage;

        public PlayersHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            playerName = (TextView) itemView.findViewById(R.id.best_player_name);
            playerImage = (CircleImageView) itemView.findViewById(R.id.best_player_image);
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
        public void setDetails(Player player) {
            playerName.setText(player.getUsername());
            Picasso.get().load(player.getImage_url()).fit().centerInside().into(playerImage);
        }
    }

    public BestPlayersAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    @NonNull
    @Override
    public PlayersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_best_player_layout, parent, false);
        return new PlayersHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersHolder holder, int position) {
        Player player = players.get(position);
        holder.setDetails(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

}


