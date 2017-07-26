package com.shileiyu.compilingannotation;

import com.shileiyu.compilingannotation.bean.net.Discovery;
import com.shileiyu.compilingannotation.bean.net.Recommend;
import com.shileiyu.compilingannotation.net.CallPool;
import com.shileiyu.compilingannotation.net.NetApi;

import retrofit2.Call;
import retrofit2.http.Query;

/**
 * @auther by yushilei.
 * @time 2017/7/26-21:35
 * @desc
 */

public class NetProxy implements NetApi.API {
    private int taskID;

    public NetProxy(int taskID) {
        this.taskID = taskID;
    }

    @Override
    public Call<Discovery> getDiscovery() {
        Call<Discovery> call = NetApi.api.getDiscovery();
        inCall(call, taskID);
        return call;
    }

    @Override
    public Call<Recommend> getRecommend(@Query("pageId") int pageId, @Query("pageSize") int pageSize) {
        Call<Recommend> call = NetApi.api.getRecommend(pageId, pageSize);
        inCall(call, taskID);
        return call;
    }

    private void inCall(Call call, int taskId) {
        CallPool.addCall(call, taskId);
    }
}
