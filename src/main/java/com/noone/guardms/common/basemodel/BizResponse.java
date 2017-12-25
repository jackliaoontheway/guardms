package com.noone.guardms.common.basemodel;

public class BizResponse<T> extends BaseResponse<T>
{
	private Double totalFee;
	
    public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public ErrorInfo getErrorInfo()
    {
        return (getErrors() == null || getErrors().size() == 0) ? null : getErrors().get(0);
    }

}
