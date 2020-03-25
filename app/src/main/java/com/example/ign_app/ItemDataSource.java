package com.example.ign_app;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.ign_app.model.Feed;
import com.example.ign_app.model.data.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemDataSource extends PageKeyedDataSource<Integer, Data> {
    public static final int START_INDEX = 1;
    public static final int PAGE_SIZE = 8;
    private static final String BASE_URL = "https://ign-apis.herokuapp.com/";



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Data> callback) {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IgnApi ignApi = retrofit.create(IgnApi.class);
        Call<Feed> call = ignApi.getStuff(1,8);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                if(response.body() != null){
                 //  ArrayList<Feed> list = response.body();
                    callback.onResult(response.body().getData(), null, START_INDEX+1);

                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Data> callback) {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IgnApi ignApi = retrofit.create(IgnApi.class);
        Call<Feed> call = ignApi.getStuff(params.key,8);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Integer key = (params.key > 1 ) ? params.key-1:null;
                if(response.body()!= null){
                    callback.onResult(response.body().getData(), key);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Data> callback) {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IgnApi ignApi = retrofit.create(IgnApi.class);
        Call<Feed> call = ignApi.getStuff(params.key,8);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Integer key =  params.key+1;
                if(response.body() !=null){
                    callback.onResult(response.body().getData(), key);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });
    }


}
