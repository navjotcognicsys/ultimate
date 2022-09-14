package com.todocode.ultimequiz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.todocode.ultimequiz.Models.CompletedQuestion;
import com.todocode.ultimequiz.Models.ContinueQuiz;
import com.todocode.ultimequiz.R;

import java.util.List;

public class CompletedQuestionsAdapter extends RecyclerView.Adapter<CompletedQuestionsAdapter.CatViewHolder> {
    private List<CompletedQuestion> questions;
    public static Context context;

    public CompletedQuestionsAdapter(List<CompletedQuestion> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_completed_question_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.setDetails(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static public class CatViewHolder extends RecyclerView.ViewHolder {
        private TextView type, points, date, true_answer;
        private ImageView imagetype, texttype, audiotype;

        public CatViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.question_type);
            points = itemView.findViewById(R.id.earned_points);
            date = itemView.findViewById(R.id.date);
            true_answer = itemView.findViewById(R.id.true_answer);
            imagetype = itemView.findViewById(R.id.image_type);
            texttype = itemView.findViewById(R.id.text_type);
            audiotype = itemView.findViewById(R.id.audio_type);
        }
        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        void setDetails(CompletedQuestion quiz) {
            type.setText("Question Type : "+quiz.getQuestion_type());
            points.setText("Earned Points : "+quiz.getPoints());
            date.setText("Completed At : "+quiz.getDate());
            true_answer.setText("True Answer : "+quiz.getTrue_answer());
            if (quiz.getQuestion_type().equals("text")) {
                texttype.setVisibility(View.VISIBLE);
            } else if (quiz.getQuestion_type().equals("image")) {
                imagetype.setVisibility(View.VISIBLE);
            } else if (quiz.getQuestion_type().equals("audio")) {
                audiotype.setVisibility(View.VISIBLE);
            }
        }
    }
}





