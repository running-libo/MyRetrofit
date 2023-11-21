package com.example.retrofit.adapter;

import androidx.annotation.Nullable;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 对Json转换这部分做下抽象封装，提取成一个负责Json转换的接口 由应用层传入具体的实现.
 * @param <F>
 * @param <T>
 */
public interface Converter<F, T> {

    @Nullable
    T convert(F value);

    abstract class Factory {
        public Converter<?, RequestBody> requestBodyConverter(Type type) {
            return null;
        }

        public Converter<ResponseBody, ?> responseBodyConverter(Type type) {
            return null;
        }
    }
}
