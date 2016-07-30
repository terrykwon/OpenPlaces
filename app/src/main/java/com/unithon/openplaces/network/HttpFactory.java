package com.unithon.openplaces.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BoBinLee on 2016. 7. 31..
 */
public class HttpFactory {
    public static HttpService search() {
        return  new Retrofit.Builder()
                .baseUrl(Constant.SEARCH_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HttpService.class);
    }
}
