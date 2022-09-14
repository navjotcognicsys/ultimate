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
import com.todocode.ultimequiz.Models.Category;
import com.todocode.ultimequiz.R;

import java.util.List;

public class PopularCategoriesAdapter extends RecyclerView.Adapter<PopularCategoriesAdapter.CatViewHolder> {
    private List<Category> categories;
    private com.todocode.ultimequiz.Adapters.PopularCategoriesAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(com.todocode.ultimequiz.Adapters.PopularCategoriesAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public PopularCategoriesAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_category_single_layout, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        holder.setDetails(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static public class CatViewHolder extends RecyclerView.ViewHolder {
        private KenBurnsView categoryImage;
        private TextView categoryName;

        public CatViewHolder(@NonNull View itemView, final com.todocode.ultimequiz.Adapters.PopularCategoriesAdapter.OnItemClickListener listener) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_img);
            categoryName = itemView.findViewById(R.id.category_name);
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
        void setDetails(Category category) {
            categoryName.setText(category.getName());
            Picasso.get().load(category.getImg()).into(categoryImage);
        }
    }
}


