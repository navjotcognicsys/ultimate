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

public class AllPlayersAdapter extends  RecyclerView.Adapter<AllPlayersAdapter.FirstPlayerHolder> {

    private Context context;
    private List<Player> players;
    private AllPlayersAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(AllPlayersAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public class FirstPlayerHolder extends RecyclerView.ViewHolder {
        private TextView playerName, playerPoints, playerPosition;
        private CircleImageView playerImage;

        public FirstPlayerHolder(@NonNull View itemView, final AllPlayersAdapter.OnItemClickListener listener) {
            super(itemView);
            playerName = (TextView) itemView.findViewById(R.id.all_player_name);
            playerPoints = (TextView) itemView.findViewById(R.id.all_player_points);
            playerImage = (CircleImageView) itemView.findViewById(R.id.all_player_image);
            playerPosition = (TextView) itemView.findViewById(R.id.rank);
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
            playerPoints.setText(String.valueOf(player.getTotal_score() + " pts."));
            playerPosition.setText(String.valueOf(getAdapterPosition()+4));
            Picasso.get().load(player.getImage_url()).fit().centerInside().into(playerImage);
        }
    }

    public AllPlayersAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    @NonNull
    @Override
    public FirstPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_leaderboard_layout, parent, false);
        return new FirstPlayerHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FirstPlayerHolder holder, int position) {
        Player player = players.get(position);
        holder.setDetails(player);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}



