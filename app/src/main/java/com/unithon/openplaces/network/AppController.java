package com.unithon.openplaces.network;

import android.app.Application;

import com.unithon.openplaces.model.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by choi on 2016-07-30.
 */
public class AppController extends Application {
    private static AppController instance;
    private static HttpService httpService;

    public static AppController getInstance() {
        if(instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    public synchronized static HttpService getHttpService() {
        return  new Retrofit.Builder()
                .baseUrl(Constant.SEARCH_API_URL)
    .addConverterFactory(GsonConverterFactory.create())
            .build()
    .create(HttpService.class);
    }
    public synchronized static HttpService getSearch() {
        return  new Retrofit.Builder()
                .baseUrl(Constant.GOOGLE_SEARCH_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HttpService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance();
        AppController.instance = this;
    }
}
