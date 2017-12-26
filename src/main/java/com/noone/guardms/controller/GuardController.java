package com.noone.guardms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noone.guardms.biz.BizProductStockService;
import com.noone.guardms.common.basemodel.BizResponse;
import com.noone.guardms.common.basemodel.ServerResponse;
import com.noone.guardms.domain.OrderItem;

@RestController
@RequestMapping("/guardms")
public class GuardController extends BaseController {

	@Autowired
	BizProductStockService bizProductStockService;

	@GetMapping("/readrfid")
	public @ResponseBody ServerResponse<List<OrderItem>> readRFID() {

		ServerResponse<List<OrderItem>> serverResponse = new ServerResponse<List<OrderItem>>();

		BizResponse<List<OrderItem>> bizResp = bizProductStockService.retriveProductStockByReadRfid();
		if (bizResp != null) {
			serverResponse.setData(bizResp.getData());
			serverResponse.setHasData(bizResp.getHasData());
			serverResponse.setAllPaided(bizResp.getAllPaided());
		}

		return serverResponse;
	}

}