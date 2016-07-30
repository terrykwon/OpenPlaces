package com.unithon.openplaces.network;

import android.support.annotation.NonNull;

import com.unithon.openplaces.model.LocalResponse;
import com.unithon.openplaces.model.SearchResponse;

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
     * @param region 지역 정보
     * @param title 상권 정보
     * @return 상호명, 전화번호, 주소, 마감시간, 오픈시간, 영업상태
     */
    @GET("find/open-place")
    Call<SearchResponse> search(@NonNull @Query("region") String region, @NonNull @Query("title") String title);

    /**
     *
     * @param key google api key
     * @param location gps(위도/경도)
     * @param radius 현재 위치로부터 반경(미터단위)
     * @return
     */
    @GET("json")
    Call<List<LocalResponse>> localSearch(@Query("key") String key, @Query("location") String location, @Query("radius") long radius);
}
