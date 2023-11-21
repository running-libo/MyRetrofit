package com.example.retrofit;

/**
 * 请求回调处理接口
 * @param <T>
 */
public interface NetCall<T> {

    public void execute(NetCallback<T> netCallback);
}
