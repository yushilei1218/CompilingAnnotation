package com.shileiyu.compilingannotation.net;

import android.util.Log;
import android.util.SparseArray;


import com.shileiyu.compilingannotation.util.SetUtil;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;

/**
 * 网络请求池：
 * <p>
 */

public final class CallPool {
    private CallPool() {
    }

    private static final String TAG = "CallPool";

    private static final SparseArray<List<WeakReference<Call>>> pool = new SparseArray<>();

    public static synchronized void addCall(Call call, int taskId) {
        int index = pool.indexOfKey(taskId);
        if (index < 0) {
            pool.put(taskId, new LinkedList<WeakReference<Call>>());
        }
        List<WeakReference<Call>> list = pool.get(taskId);
        boolean contains = false;
        for (WeakReference<Call> w : list) {
            if (w != null && w.get() != null && w.get().equals(call)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            Log.i(TAG, "addCall ok " + taskId + " " + call.toString());
            list.add(new WeakReference<Call>(call));
        }
    }

    public static synchronized void removeCall(Call call) {
        for (int i = 0; i < pool.size(); i++) {
            int key = pool.keyAt(i);
            List<WeakReference<Call>> list = pool.get(key);
            if (SetUtil.isEmpty(list))
                break;
            Iterator<WeakReference<Call>> iterator = list.iterator();
            while (iterator.hasNext()) {
                WeakReference<Call> next = iterator.next();
                if (next.get() != null && next.get().equals(call)) {
                    Log.i(TAG, "removeCall ok " + call.toString());
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 取消某个taskId下所有网络请求
     *
     * @param taskId 任务id
     */
    public static synchronized void cancelCall(int taskId) {
        List<WeakReference<Call>> set = pool.get(taskId);
        if (!SetUtil.isEmpty(set)) {
            for (WeakReference<Call> w : set) {
                if (w != null && w.get() != null) {
                    w.get().cancel();
                    Log.i(TAG, "cancelCall ok " + taskId + " " + w.get().toString());
                }
            }
        }
        pool.delete(taskId);
    }
}
