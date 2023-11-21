package com.example.retrofit;

import com.example.retrofit.http.GET;
import com.example.retrofit.http.POST;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Retrofit {
    private OkHttpClient mOkHttpClient;
    private Converter.Factory mConverterFactory;

    public Retrofit(
            OkHttpClient okHttpClient,
            Converter.Factory converterFactory) {
        this.mOkHttpClient = okHttpClient;
        this.mConverterFactory = converterFactory;
    }

    /**
     * Retrofit动态代理创建Service，调用接口方法，获取接口及注解信息
     * 泛型T是 ApiService类
     */
    public <T> T createService(final Class<T> service) {

        //使用动态代理创建代理对象
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service.getClass()}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Annotation[] annotations = method.getAnnotations(); //获取方法所有的注解
                for (Annotation annotation: annotations) {
                    if (annotation instanceof GET) { //拿到注解判断是否为GET类型
                        //获取注解的value，拼接request调用okhttp发起请求
                        return parseGet(((GET) annotation).value());
                    } else if (annotation instanceof POST) {
                        //获取注解的value，拼接request调用okhttp发起请求
                        String url = ((POST)annotation).value();
                        return parsePost(url, method, args);
                    }
                }
                return null;
            }
        });
    }

    /**
     * Get请求处理
     * @param url
     * @return
     */
    private Call parseGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return mOkHttpClient.newCall(request);
    }

    /**
     * Post请求处理
     * @param url
     * @param method
     * @param args
     * @return
     */
    private Call parsePost(String url, Method method, Object[] args) {
        //在 paresePost方法中我们首先通过Method的getGenericParameterTypes方法获取所有参数的Type类型,并且通过Type类获得参数的原始Class类型，之后就可以使用Gson转换成对应的Json对象了。
        //method. getGenericParameterTypes()  成员方法参数类型的泛型参数
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length > 0) {
            //这个泛型的类型即为需要解析的Entity类型
            RequestBody requestBody = requestBodyConverter(genericParameterTypes[0]).convert(args[0]);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            return mOkHttpClient.newCall(request);
        }
        return null;
    }

    public <T> Converter<T, RequestBody> requestBodyConverter(Type type) {
        return (Converter<T, RequestBody>) mConverterFactory.requestBodyConverter(type);
    }

}
