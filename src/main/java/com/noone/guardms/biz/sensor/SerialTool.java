package com.noone.guardms.biz.sensor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import com.noone.guardms.biz.sensor.exceptions.NoSuchPort;
import com.noone.guardms.biz.sensor.exceptions.NotASerialPort;
import com.noone.guardms.biz.sensor.exceptions.PortInUse;
import com.noone.guardms.biz.sensor.exceptions.ReadDataFromSerialPortFailure;
import com.noone.guardms.biz.sensor.exceptions.SendDataToSerialPortFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortInputStreamCloseFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortOutputStreamCloseFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortParameterFailure;
import com.noone.guardms.biz.sensor.exceptions.TooManyListeners;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class SerialTool {

	private static SerialTool serialTool = null;

	static {
		if (serialTool == null) {
			serialTool = new SerialTool();
		}
	}

	private SerialTool() {
	}

	public static SerialTool getSerialTool() {
		if (serialTool == null) {
			serialTool = new SerialTool();
		}
		return serialTool;
	}

	public static final ArrayList<String> findPort() {

		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

		ArrayList<String> portNameList = new ArrayList<>();

		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}

		return portNameList;

	}

	public static final SerialPort openPort(String portName, int baudrate)
			throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse {

		try {

			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

			CommPort commPort = portIdentifier.open(portName, 3000);

			if (commPort instanceof SerialPort) {

				SerialPort serialPort = (SerialPort) commPort;

				try {
					serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					throw new SerialPortParameterFailure();
				}

				return serialPort;

			} else {
				throw new NotASerialPort();
			}
		} catch (NoSuchPortException e1) {
			throw new NoSuchPort();
		} catch (PortInUseException e2) {
			throw new PortInUse();
		}
	}

	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}

	public static void sendToPort(SerialPort serialPort, byte[] order)
			throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {

		OutputStream out = null;

		try {

			out = serialPort.getOutputStream();
			out.write(order, 0, 1);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
			throw new SendDataToSerialPortFailure();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new SerialPortOutputStreamCloseFailure();
			}
		}

	}

	public static byte[] readFromPort(SerialPort serialPort)
			throws ReadDataFromSerialPortFailure, SerialPortInputStreamCloseFailure {

		InputStream in = null;
		byte[] bytes = null;

		try {

			in = serialPort.getInputStream();
			int bufflenth = in.available();

			while (bufflenth != 0) {
				bytes = new byte[bufflenth];
				in.read(bytes);
				bufflenth = in.available();
			}
		} catch (IOException e) {
			throw new ReadDataFromSerialPortFailure();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new SerialPortInputStreamCloseFailure();
			}

		}

		return bytes;

	}

	public static void addListener(SerialPort port, SerialPortEventListener listener) throws TooManyListeners {

		try {

			port.addEventListener(listener);

			port.notifyOnBreakInterrupt(true);
			port.notifyOnCarrierDetect(true);
			port.notifyOnCTS(true);
			port.notifyOnDSR(true);
			port.notifyOnFramingError(true);
			port.notifyOnOutputEmpty(true);
			port.notifyOnOverrunError(true);
			port.notifyOnParityError(true);
			port.notifyOnRingIndicator(true);

			port.notifyOnDataAvailable(true);
			port.notifyOnBreakInterrupt(true);

		} catch (TooManyListenersException e) {
			throw new TooManyListeners();
		}
	}

}
