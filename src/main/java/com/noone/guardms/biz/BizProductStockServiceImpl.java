package com.noone.guardms.biz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.noone.guardms.biz.sensor.SensorStatus;
import com.noone.guardms.biz.sensor.SensorUtils;
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

	private Logger logger = Logger.getLogger(BizProductStockServiceImpl.class);

	private final static String netRFIDIP = "192.168.5.7";

	@Override
	public BizResponse<List<OrderItem>> retriveProductStockByReadRfid() {

		BizResponse<List<OrderItem>> bizResp = new BizResponse<List<OrderItem>>();

		SensorUtils sensorUtils = SensorUtils.getInstance();
		SensorStatus status = sensorUtils.getStatus();
		if (status == null) {
			logger.error("Sensor encounted an Exception.");
		} else {
			if(status.isDoorOpen()) {
				sensorUtils.holdTheDoor();
			}
			// No person
			if (!status.isHasPerson()) {
				return bizResp;
			}
		}

		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) - 10);
		c.set(Calendar.MILLISECOND, 0);
		Date startDate = c.getTime();

		RFIDNetReaderFactory factory = RFIDNetReaderFactory.getInstance();
		Set<String> rfidSet = factory.readAllRFID(netRFIDIP, startDate);
		logger.info("first :" + rfidSet);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Set<String> rfidSet2 = factory.readAllRFID(netRFIDIP, startDate);
		logger.info("second :" + rfidSet2);
		if (rfidSet2 != null) {
			rfidSet.addAll(rfidSet2);
		}
		/*
		 * try { Thread.sleep(1000); } catch (InterruptedException e) {
		 * e.printStackTrace(); } Set<String> rfidSet3 =
		 * factory.readAllRFID(netRFIDIP,startDate); System.out.println("third :" +
		 * rfidSet3); if(rfidSet3 != null) { rfidSet.addAll(rfidSet3); }
		 */
		logger.info("all rfid :" + rfidSet);

		List<ProductStock> stockList = new ArrayList<ProductStock>();

		for (String rfid : rfidSet) {
			ProductStock stock = getProductStockByCriteria(rfid);
			if (stock != null) {
				stockList.add(stock);
			}
		}

		if (stockList.size() == 0) {
			logger.info("rfid has't readed in stock...");
		}

		if (rfidSet.size() != stockList.size()) {
			logger.info("some rfid has't readed in stock...");
		}

		boolean hasUnPaidedItem = checkHasUnpaidedItem(stockList);
		
		if (hasUnPaidedItem) {
			bizResp.setHasData("1");
		} else {
			bizResp.setAllPaided("1");
		}
		return bizResp;
	}

	private boolean checkHasUnpaidedItem(List<ProductStock> stockList) {
		boolean hasUnPaidedItem = false;
		for (ProductStock stock : stockList) {
			// 已经支付
			if ("PAID".equals(stock.getStatus())) {
				continue;
			}
			hasUnPaidedItem = true;
			break;
		}
		
		return hasUnPaidedItem;
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
