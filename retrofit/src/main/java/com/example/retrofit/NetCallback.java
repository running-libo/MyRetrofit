package com.example.retrofit;

/**
 * 统一的网络请求回调
 * @param <T>
 */
public interface NetCallback<T> {

    void onFailure(Exception e);

    void onSuccess(T data);
}
