package com.noone.guardms.common.basemodel;

public class ClientRequest<T> extends BaseRequest<T>
{
    private String nonceToken; //against CRSF

    public String getNonceToken()
    {
        return nonceToken;
    }

    public void setNonceToken(String nonceToken)
    {
        this.nonceToken = nonceToken;
    }
    
}
