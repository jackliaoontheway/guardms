package com.noone.guardms.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.noone.guardms.common.basemodel.BizResponse;
import com.noone.guardms.domain.OrderItem;
import com.noone.guardms.domain.ProductStock;
import com.noone.guardms.domain.ProductStockRepository;
import com.noone.guardms.domain.QProductStock;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.rfid.netreader.RFIDNetReaderFactory;

@Component
public class BizProductStockServiceImpl implements BizProductStockService {

	@Autowired
	ProductStockRepository productStockRepository;

	private final static String netRFIDIP = "192.168.5.7";
	
	@Override
	public BizResponse<List<OrderItem>> retriveProductStockByReadRfid() {

		BizResponse<List<OrderItem>> bizResp = new BizResponse<List<OrderItem>>();
		
		Calendar c = Calendar.getInstance();
		
		RFIDNetReaderFactory factory = RFIDNetReaderFactory.getInstance();
		Set<String> rfidSet = factory.readAllRFID(netRFIDIP);
		System.out.println("first :" + rfidSet);

		if (rfidSet == null || rfidSet.size() == 0) {
			bizResp.addError("No rfid has been readed...");
			return bizResp;
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Set<String> rfidSet2 = factory.readAllRFID(netRFIDIP);
			System.out.println("second :" + rfidSet2);
			if(rfidSet2 != null) {
				rfidSet.addAll(rfidSet2);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Set<String> rfidSet3 = factory.readAllRFID(netRFIDIP);
			System.out.println("third :" + rfidSet3);
			if(rfidSet3 != null) {
				rfidSet.addAll(rfidSet3);
			}
		}
		
		System.out.println("all rfid :" + rfidSet);

		List<ProductStock> stockList = new ArrayList<ProductStock>();

		for (String rfid : rfidSet) {
			ProductStock stock = getProductStockByCriteria(rfid);
			if (stock != null) {
				stockList.add(stock);
			} else {

			}
		}

		if (stockList.size() == 0) {
			bizResp.addError("rfid has't readed in stock...");
			return bizResp;
		}

		if (rfidSet.size() != stockList.size()) {
			System.out.println("some rfid has't readed in stock...");
			// bizResp.addError("some rfid has't readed in stock...");
			// return bizResp;
		}

		convertToOrderItem(stockList, bizResp);
		return bizResp;
	}

	private List<OrderItem> convertToOrderItem(List<ProductStock> stockList, BizResponse<List<OrderItem>> bizResp) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		Map<String, OrderItem> map = new HashMap<String, OrderItem>();

		Double totalFee = 0.0;
		for (ProductStock stock : stockList) {
			// 已经支付的 不要显示
			if ("PAID".equals(stock.getStatus())) {
				continue;
			}
			String sku = stock.getSku();
			OrderItem item = map.get(sku);

			Double price = Double.parseDouble(StringUtils.isEmpty(stock.getPrice()) ? "0" : stock.getPrice());
			String rfid = stock.getRfid();
			if (item == null) {
				item = new OrderItem();
				item.setSku(sku);
				item.setName(stock.getName());
				item.setPrice(price);
				item.setRfids(rfid);
				item.setQty(1);
				item.setItemFee(price);
				map.put(sku, item);
			} else {
				item.setQty(item.getQty() + 1);
				item.setRfids(item.getRfids() + "&" + rfid);
				item.setItemFee(item.getItemFee() + price);
			}

			double convertedItemFee = new BigDecimal(item.getItemFee()).setScale(3, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			item.setItemFee(convertedItemFee);

			totalFee += price;
		}

		list.addAll(map.values());
		double convertedTotalFee = new BigDecimal(totalFee).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		bizResp.setTotalFee(convertedTotalFee);
		bizResp.setData(list);
		
		if(list.size() == 0) {
			bizResp.setAllPaided("1");
		} else {
			bizResp.setHasData("1");
		}

		return list;
	}

	public ProductStock getProductStockByCriteria(String rfid) {
		ProductStock stock = null;
		try {
			BooleanExpression predicate = QProductStock.productStock.rfid.eq(rfid);
			stock = productStockRepository.findOne(predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

}
