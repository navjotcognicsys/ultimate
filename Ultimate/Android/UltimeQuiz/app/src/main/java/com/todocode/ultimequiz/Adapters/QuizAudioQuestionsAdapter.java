package com.todocode.ultimequiz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.todocode.ultimequiz.Models.AudioQuestion;
import com.todocode.ultimequiz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class QuizAudioQuestionsAdapter extends RecyclerView.Adapter<QuizAudioQuestionsAdapter.CatViewHolder> {
    private List<AudioQuestion> quizzes;
    public static Context context;
    private com.todocode.ultimequiz.Adapters.QuizAudioQuestionsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(com.todocode.ultimequiz.Adapters.QuizAudioQuestionsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public QuizAudioQuestionsAdapter(List<AudioQuestion> quizzes, Context context) {
        this.quizzes = quizzes;
        this.context = context;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_audioquestion_layout, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.setDetails(quizzes.get(position));
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    static public class CatViewHolder extends RecyclerView.ViewHolder {
        private TextView question_rank;
        private CardView card;
        private ImageView audio, completed;

        public CatViewHolder(@NonNull View itemView, final com.todocode.ultimequiz.Adapters.QuizAudioQuestionsAdapter.OnItemClickListener listener) {
            super(itemView);
            question_rank = itemView.findViewById(R.id.question_type);
            audio = itemView.findViewById(R.id.image);
            completed = itemView.findViewById(R.id.completed_image);
            card = itemView.findViewById(R.id.question_card);
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
        void setDetails(AudioQuestion question) {
            question_rank.setText("Question "+String.valueOf(question.getRank()));
            StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.domain_name)+"/api/questions/audio/completed/check", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("0")) {
                            completed.setVisibility(View.VISIBLE);
                            audio.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("question_id", String.valueOf(question.getId()));
                    SharedPreferences userSituationId;
                    userSituationId = context.getSharedPreferences("userId", MODE_PRIVATE);
                    params.put("player_id", userSituationId.getString("userId", ""));
                    params.put("key", context.getResources().getString(R.string.api_secret_key));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
    }
}



