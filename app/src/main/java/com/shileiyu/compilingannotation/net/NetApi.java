package com.shileiyu.compilingannotation.net;

import com.shileiyu.RetrofitProxy;
import com.shileiyu.RetrofitTarget;
import com.shileiyu.compilingannotation.bean.net.Discovery;
import com.shileiyu.compilingannotation.bean.net.Recommend;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit2 网络封装
 *
 * @author shilei.yu
 * @since on 2017/7/10.
 */

public class NetApi {
    @RetrofitTarget
    public static API api;
    private static final String BASE_URL = "http://mobile.ximalaya.com";

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .client(Client.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        api = retrofit.create(API.class);
    }

    @RetrofitProxy
    public interface API {
        @GET("/mobile/discovery/v3/recommend/ts-1500624532898")
        Call<Discovery> getDiscovery();

        @GET("/mobile/discovery/v1/recommend/albums")
        Call<Recommend> getRecommend(@Query("pageId") int pageId, @Query("pageSize") int pageSize);
    }
}
