package com.noone.guardms.biz.sensor;

import org.apache.log4j.Logger;

import com.noone.guardms.biz.sensor.exceptions.NoSuchPort;
import com.noone.guardms.biz.sensor.exceptions.NotASerialPort;
import com.noone.guardms.biz.sensor.exceptions.PortInUse;
import com.noone.guardms.biz.sensor.exceptions.ReadDataFromSerialPortFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortInputStreamCloseFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortParameterFailure;
import com.noone.guardms.biz.sensor.exceptions.TooManyListeners;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SensorUtils {

	private Logger logger = Logger.getLogger(SensorUtils.class);

	private SerialPort serialPort = null;

	private static final SensorUtils INSTANCE = new SensorUtils();

	private static final String PORT = "COM3";

	private String status = null;

	public static SensorUtils getInstance() {
		return INSTANCE;
	}

	private SensorUtils() {
		openPort();
	}

	private void closePort() {
		// SerialTool.closePort(this.serialPort);
	}

	private void openPort()  {
		try {
			this.serialPort = SerialTool.openPort(PORT, 9600);

			SerialTool.addListener(this.serialPort, new SerialListener());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public SensorStatus getStatus() {
		SensorStatus result = new SensorStatus();
		try {
			this.status = null;

			// openPort();

			byte[] giveMeTheData = { (byte) 170 };
			SerialTool.sendToPort(this.serialPort, giveMeTheData);

			Thread.sleep(1000);

			if (this.status == null) {
				Thread.sleep(1000);
			} else {
				result = convertStatus(this.status);
				return result;
			}

			if (this.status == null) {
				Thread.sleep(1000);
			} else {
				result = convertStatus(this.status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closePort();
		}
		return result;
	}

	private SensorStatus convertStatus(String statusStr) {
		SensorStatus status = new SensorStatus();

		String[] statusArray = statusStr.split("");

		if ("0".equals(statusArray[5])) {
			status.setDoorOpen(true);
		}
		if ("1".equals(statusArray[6])) {
			status.setHasPerson(true);
		}
		if ("1".equals(statusArray[7])) {
			status.setHasPerson(true);
		}
		return status;
	}

	public void holdTheDoor() {
		try {
			// openPort();

			byte[] holdTheDoor = { (byte) 171 };// 171 lock
			SerialTool.sendToPort(this.serialPort, holdTheDoor);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closePort();
		}
	}

	public void openTheDoor() {
		try {
			// openPort();

			byte[] openTheDoor = { (byte) 172 };// 172 unlok
			SerialTool.sendToPort(this.serialPort, openTheDoor);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closePort();
		}
	}

	private class SerialListener implements SerialPortEventListener {

		public void serialEvent(SerialPortEvent serialPortEvent) {

			switch (serialPortEvent.getEventType()) {

			case SerialPortEvent.BI:
				logger.info("can't get status");
				break;
			case SerialPortEvent.OE:

			case SerialPortEvent.FE:

			case SerialPortEvent.PE:

			case SerialPortEvent.CD:

			case SerialPortEvent.CTS:

			case SerialPortEvent.DSR:

			case SerialPortEvent.RI:

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;

			case SerialPortEvent.DATA_AVAILABLE:
				byte[] data = null;
				try {
					if (serialPort == null) {
						logger.info("serialPort is null");
					} else {
						data = SerialTool.readFromPort(serialPort);
						if (data.length > 0) {
							INSTANCE.status = byteToBit(data[0]);
						}
					}

				} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
					logger.info("can't get status");
				}
				break;
			}
		}

	}

	private static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

}
