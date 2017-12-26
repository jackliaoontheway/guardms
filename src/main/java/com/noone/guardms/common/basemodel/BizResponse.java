package com.noone.guardms.common.basemodel;

public class BizResponse<T> extends BaseResponse<T>
{
	private String hasData;
	private String allPaided;
	private Double totalFee;
	
    public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	
	
	public String getHasData() {
		return hasData;
	}

	public void setHasData(String hasData) {
		this.hasData = hasData;
	}

	public String getAllPaided() {
		return allPaided;
	}

	public void setAllPaided(String allPaided) {
		this.allPaided = allPaided;
	}

	public ErrorInfo getErrorInfo()
    {
        return (getErrors() == null || getErrors().size() == 0) ? null : getErrors().get(0);
    }

}
