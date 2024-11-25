package com.example.wqcomic.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    //private static final String BASE_URL = "http://192.168.0.111:3000/";//CF IPV4
   // private static final String BASE_URL = "http://192.168.16.108:3000/";//CF IPV4
   //private static final String BASE_URL = "http://172.19.200.253:3000/";//Ck IPV4
   private static final String BASE_URL = "http://192.168.1.16:3000/";//Ck IPV4
  //  private static final String BASE_URL = "http://192.168.1.159:3000/";//Home IPV4

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static UserApi getApiServiceUsers() {
        return retrofit.create(UserApi.class);
    }
    public static ComicApi getApiServiceComics(){
        return retrofit.create(ComicApi.class);
    }
}
