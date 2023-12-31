package com.example.retrofit.adapter;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;

public class GsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    public GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type) {
        //通过Type 转换成Gson的TypeAdapter
        //具体类型的json转换依赖于这个TypeAdapter
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

    final static class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Nullable
        @Override
        public RequestBody convert(T value) {
            //将value 的json字符串使用Gson解析
            //并封装成RequestBody返回
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream());
            JsonWriter jsonWriter = null;
            try {
                jsonWriter = gson.newJsonWriter(writer);
                adapter.write(jsonWriter, value);
                jsonWriter.close();
                return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
