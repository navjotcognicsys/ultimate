package com.todocode.ultimequiz.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.todocode.ultimequiz.Models.ContinueQuiz;
import com.todocode.ultimequiz.R;

import java.util.List;

public class StartedQuizStatisticsAdapter extends RecyclerView.Adapter<StartedQuizStatisticsAdapter.CatViewHolder> {
    private List<ContinueQuiz> quizzes;
    private StartedQuizStatisticsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(StartedQuizStatisticsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public StartedQuizStatisticsAdapter(List<ContinueQuiz> quizzes) {
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_startedquiz_statistics_layout, parent, false), mListener);
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
        private KenBurnsView quizImg;
        private TextView quizName, remainingsQuestions, correctPercentage;

        public CatViewHolder(@NonNull View itemView, final com.todocode.ultimequiz.Adapters.StartedQuizStatisticsAdapter.OnItemClickListener listener) {
            super(itemView);
            quizImg = itemView.findViewById(R.id.quiz_img);
            quizName = itemView.findViewById(R.id.quiz_name);
            correctPercentage =  itemView.findViewById(R.id.correct_percentage);
            remainingsQuestions = itemView.findViewById(R.id.remainings_questions);
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
        void setDetails(ContinueQuiz quiz) {
            quizName.setText(quiz.quizName);
            Integer CompletedPercentage = quiz.getQuizNumberOfQuestions() - quiz.getQuizNumberOfRemainingQuestions();
            Integer percentage = (CompletedPercentage*100) / quiz.getQuizNumberOfQuestions();
            correctPercentage.setText(String.valueOf(percentage)+ "% Of Progress");
            remainingsQuestions.setText(String.valueOf(CompletedPercentage)+ " Completed questions");
            Picasso.get().load(quiz.getQuizImageUrl()).into(quizImg);
        }
    }
}




