package com.noone.guardms.common.basemodel;

public class ServerResponse<T> extends BaseResponse<T> {
	private String nonceToken;
	private String hasData;
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

	public String getNonceToken() {
		return nonceToken;
	}

	public void setNonceToken(String nonceToken) {
		this.nonceToken = nonceToken;
	}
}
