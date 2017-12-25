package com.noone.guardms.common.basemodel;

public abstract class BaseRequest<T>
{
    private T data;

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
    
}
