package com.noone.guardms.biz;

import java.util.List;

import com.noone.guardms.common.basemodel.BizResponse;
import com.noone.guardms.domain.OrderItem;

public interface BizProductStockService {
	
	BizResponse<List<OrderItem>> retriveProductStockByReadRfid();
	
}