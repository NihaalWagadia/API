package com.example.ign_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ign_app.CommentPackage.FeedComment;
import com.example.ign_app.CommentPackage.content.ContentComment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleAdapter extends  RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {


    private Context mContext;
    private List<ArticleData> articleDataList;
    private static final String BASE_URL = "https://ign-apis.herokuapp.com/";
    List<CommentData> commentDataList;
    ArrayList<ContentComment> contentCommentArrayList;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;




    public ArticleAdapter(Context mContext, List<ArticleData> articleDataList) {
        this.mContext = mContext;
        this.articleDataList = articleDataList;
    //    this.commentDataList = commentDataList;

    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType){
//            case VIEW_TYPE_NORMAL:
//
//            case  VIEW_TYPE_LOADING:
//                return  new ArticleViewHolder(
//                        LayoutInflater.from(parent.getContext()).inflate(R.layout.articlecard,parent,false));
//
//            default:
//                return null;
//
//        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.articlecard, null);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder holder, int position) {

        final ArticleData articleData = articleDataList.get(position);
       // final CommentData commentData = commentDataList.get(position);
        holder.head_txt.setText(articleData.getHeadline());
        holder.des_txt.setText(articleData.getDescription());
        holder.authorName_txt.setText(articleData.getAuthorName());
        Picasso.with(mContext).load(articleData.getUrlImage()).into(holder.article_image);


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CommentApi CApi = retrofit.create(CommentApi.class);
            Call<FeedComment> call2 = CApi.getStuff(articleData.getContentId());
            call2.enqueue(new Callback<FeedComment>() {
                @Override
                public void onResponse(Call<FeedComment> call, Response<FeedComment> response) {
                    contentCommentArrayList= response.body().getContent();
//                    Log.d(TAG, "onResposnse: \n" +
//                            "COUNTT:" + contentCommentArrayList.get(i).getCount() + "\n");
//
//
//                        String count123 = contentCommentArrayList.get(i).getCoun;
//                        String con = contentCommentArrayList.get(i).getId();
//
//                        Log.d("BENCHOD", count123 + " " + con);
                    holder.comments.setText(contentCommentArrayList.get(0).getCount());

                }

                @Override
                public void onFailure(Call<FeedComment> call, Throwable t) {

                }
            });

        if (articleData.getAuthorImage().isEmpty()) {
            Picasso.with(mContext).load(R.drawable.ic_launcher_background).into(holder.author_image);
        } else{
            Picasso.with(mContext).load(articleData.getAuthorImage()).into(holder.author_image);
        }

        //holder.comments.setText(commentData.getComment());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(articleData.getSlug()));
                mContext.startActivity(i);
            }
        });


    }



//    @Override
//    public int getItemViewType(int position) {
//        if (isLoaderVisible) {
//            return position == articleDataList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
//        } else {
//            return VIEW_TYPE_NORMAL;
//        }
//    }
//    private static DiffUtil.ItemCallback<Data> dataItemCallback =
//            new DiffUtil.ItemCallback<Data>() {
//                @Override
//                public boolean areItemsTheSame(@NonNull Data oldItem, @NonNull Data newItem) {
//                    return oldItem.getContentId()
//                }
//
//                @Override
//                public boolean areContentsTheSame(@NonNull Data oldItem, @NonNull Data newItem) {
//                    return false;
//                }
//            }
    @Override
    public int getItemCount() {
        return articleDataList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{

        TextView head_txt, des_txt, authorName_txt, comments;
        ImageView article_image, author_image;
        RelativeLayout relativeLayout;
        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            head_txt = itemView.findViewById(R.id.headline_article);
            des_txt = itemView.findViewById(R.id.article_description);
            authorName_txt = itemView.findViewById(R.id.article_author_name);
            article_image = itemView.findViewById(R.id.article_image);
            author_image = itemView.findViewById(R.id.article_author);
            comments = itemView.findViewById(R.id.comments);
            relativeLayout = itemView.findViewById(R.id.relative_layout);

        }
    }
}
