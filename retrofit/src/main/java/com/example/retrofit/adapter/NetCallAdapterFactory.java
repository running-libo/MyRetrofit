package com.example.retrofit.adapter;

import com.example.retrofit.call.NetCall;
import com.example.retrofit.Retrofit;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * NetCallAdapter，支持Call对象到 NetCall对象的转换
 */
public class NetCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?> get(Type returnType, Retrofit retrofit) {

        if (getRawType(returnType) != NetCall.class) {
            return null;
        }
        //要求开发者方法的返回类型必须写成 NetCall<T> 或者NetCall<? extends Foo> 的形式,泛型内的类型就是Json数据对应的Class
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("return must be parameterized as NetCall<Foo> or NetCall<? extends Foo>");
        }

        Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);
        return new CallAdapter<NetCall>() {

            @Override
            public NetCall adapt(Call call) {
                return netCallback -> call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call1, IOException e) {
                        netCallback.onFailure(e);
                    }

                    @Override
                    public void onResponse(Call call1, Response response) throws IOException {
                        Object object = retrofit.responseBodyTConverter(innerType).convert(response.body());
                        netCallback.onSuccess(object);
                    }
                });
            }
        };
    }
}
