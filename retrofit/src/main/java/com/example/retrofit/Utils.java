package com.example.retrofit;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class Utils {

    /**
     * 在说到泛型类型参数时，要先说一下Type接口，Type接口只有一个实现类Class，但是有四个子接口，这四个Type子接口描述了Java泛型的四种形式。分别是：
     *
     * GenericArrayType 该接口表示一种数组类型，其组件类型为参数化类型或类型变量，如参数化类型数组Map<String, Date>[] mapArr，还有类型变量数组T[] tArr
     *
     * ParameterizedType 该接口表示参数化类型，如 Collection<String>。
     *
     * TypeVariable<D> 该接口是各种类型变量的公共高级接口，可以表示泛型声明的参数类型（不存在的类型），如 class ClassName<T>和Collection<T>，这里的T就是一个类型变量。
     *
     * WildcardType 表示一个通配符类型表达式，如 ?、? extends Number 或 ? super Integer。
     * @param type
     * @return
     */
    static Class<?> getRawType(Type type) {
        checkNotNull(type, "type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

    static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
