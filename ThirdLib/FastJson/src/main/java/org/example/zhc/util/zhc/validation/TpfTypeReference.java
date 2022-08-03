package org.example.zhc.util.zhc.validation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 序列化泛型时保存泛型信息用
 * @author aisen
 */
public abstract class TpfTypeReference<T> implements Comparable<TpfTypeReference<T>> {
    protected final Type type;
    protected TpfTypeReference()
    {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() { return type; }

    @Override
    public int compareTo(TpfTypeReference<T> o) { return 0; }
}
