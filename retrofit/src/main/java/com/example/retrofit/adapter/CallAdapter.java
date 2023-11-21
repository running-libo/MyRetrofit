package com.example.retrofit.adapter;

import com.example.retrofit.Retrofit;
import com.example.retrofit.Utils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Call;

/**
 * 这个功能相当于让Retrofit框架支持 对方法返回类型的自定义适配
 */
public interface CallAdapter<T> {

    T adapt(Call call);

    abstract class Factory {

        public abstract CallAdapter<?> get(Type returnType , Retrofit retrofit);

        /**
         * 获取类型的泛型上的类型
         * 比如 Call<Response> 则 第0个泛型是Response.class
         */
        protected static Type getParameterUpperBound(int index, ParameterizedType type) {
            return Utils.getParameterUpperBound(index, type);
        }

        /**
         * 获取Type对应的Class
         * @param type
         * @return
         */
        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }
}
