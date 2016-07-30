package com.unithon.openplaces.network;


import com.unithon.openplaces.network.response.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by choi on 2016-07-30.
 */
public interface HttpService {

    /**
     * 실시간 주변 지역 상권 정보 요청
     */
    @GET("/find/open-place")
    Call<List<SearchResponse>> search(@Query("title") String title, @Query("lat") double lat, @Query("lng") double lng, @Query("region") String region);
}
