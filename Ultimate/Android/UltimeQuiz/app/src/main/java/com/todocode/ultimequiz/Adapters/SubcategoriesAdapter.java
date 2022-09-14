package com.todocode.ultimequiz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;
import com.todocode.ultimequiz.Activities.QuizzesActivity;
import com.todocode.ultimequiz.Models.Subcategory;
import com.todocode.ultimequiz.R;

import java.util.List;

public class SubcategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> news;
    private OnItemClickListener myListener;
    private static final int TYPE_NEW = 0;
    private static final int TYPE_ADMOB = 1;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(com.todocode.ultimequiz.Adapters.SubcategoriesAdapter.OnItemClickListener newsListener) {
        myListener = newsListener;
    }

    public SubcategoriesAdapter(Context context, List<Object> news) {
        this.context = context;
        this.news = news;
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        private KenBurnsView categoryImage;
        private TextView categoryName, numberOfQuiz;

        public NewsHolder(@NonNull View itemView, final com.todocode.ultimequiz.Adapters.SubcategoriesAdapter.OnItemClickListener newsListener) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.subcategory_img);
            categoryName = itemView.findViewById(R.id.subcategory_name);
            numberOfQuiz = itemView.findViewById(R.id.number_of_quizzes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (newsListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            newsListener.onItemClick(position);
                        }
                    }
                }
            });
        }
        @SuppressLint("SetTextI18n")
        public void setDetails(final Subcategory category) {
            categoryName.setText(category.getName());
            numberOfQuiz.setText(category.getNumber_of_quizzes()+ " Quiz");
            Picasso.get().load(category.getImg()).into(categoryImage);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ADMOB:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.admob_native_small_layout,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case TYPE_NEW:
            default:
                View view = LayoutInflater.from(context).inflate(R.layout.single_subcategory_layout, parent, false);
                return new NewsHolder(view,myListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_ADMOB:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) news.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case TYPE_NEW:
                // fall through
            default:
                NewsHolder menuItemHolder = (NewsHolder) holder;
                final Subcategory category = (Subcategory) news.get(position);
                menuItemHolder.setDetails(category);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent article = new Intent(context, QuizzesActivity.class);
                        article.putExtra("id", category.getId());
                        article.putExtra("subcategory_name", category.getName());
                        context.startActivity(article);
                    }
                });
        }
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

        private UnifiedNativeAdView adView;

        public UnifiedNativeAdView getAdView() {
            return adView;
        }

        UnifiedNativeAdViewHolder(View view) {
            super(view);
            adView = (UnifiedNativeAdView) view.findViewById(R.id.ad_view_small);

            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = news.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return TYPE_ADMOB;
        }
        return TYPE_NEW;
    }

}





