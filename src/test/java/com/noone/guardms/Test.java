package com.noone.guardms;

import javax.swing.JOptionPane;

import com.noone.guardms.biz.sensor.SerialTool;
import com.noone.guardms.biz.sensor.exceptions.NoSuchPort;
import com.noone.guardms.biz.sensor.exceptions.NotASerialPort;
import com.noone.guardms.biz.sensor.exceptions.PortInUse;
import com.noone.guardms.biz.sensor.exceptions.ReadDataFromSerialPortFailure;
import com.noone.guardms.biz.sensor.exceptions.SendDataToSerialPortFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortInputStreamCloseFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortOutputStreamCloseFailure;
import com.noone.guardms.biz.sensor.exceptions.SerialPortParameterFailure;
import com.noone.guardms.biz.sensor.exceptions.TooManyListeners;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Test {

	private SerialPort serialPort = null;

	public static void main(String[] args) throws Exception {

		

		Test test = new Test();

		test.openPort();

		// test.holdTheDoor();
		
		test.getStatus();
		
		// test.openTheDoor();

		// test.closePort();
	}

	public void closePort() {
		SerialTool.closePort(this.serialPort);
	}

	public void openPort() throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse, TooManyListeners {
		
		// List<String> commList = SerialTool.findPort();
		
		this.serialPort = SerialTool.openPort("COM3", 9600);

		SerialTool.addListener(this.serialPort, new SerialListener());
	}
	
	public void getStatus() throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		byte[] giveMeTheData = { (byte) 170 };
		SerialTool.sendToPort(this.serialPort, giveMeTheData);
	}

	public void holdTheDoor() throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		byte[] holdTheDoor = { (byte) 171 };// 171 lock
		SerialTool.sendToPort(this.serialPort, holdTheDoor);
	}

	public void openTheDoor() throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		byte[] openTheDoor = { (byte) 172 };// 172 unlok
		SerialTool.sendToPort(this.serialPort, openTheDoor);
	}

	private class SerialListener implements SerialPortEventListener {

		public void serialEvent(SerialPortEvent serialPortEvent) {
			System.out.println("serialPortEvent" +"-- - - - - - -" + serialPortEvent);
			
			switch (serialPortEvent.getEventType()) {

			case SerialPortEvent.BI: // 10 ͨѶ�ж�
				JOptionPane.showMessageDialog(null, "�봮���豸ͨѶ�ж�", "����", JOptionPane.INFORMATION_MESSAGE);
				break;

			case SerialPortEvent.OE: // 7 ��λ�����������

			case SerialPortEvent.FE: // 9 ֡����

			case SerialPortEvent.PE: // 8 ��żУ�����

			case SerialPortEvent.CD: // 6 �ز����

			case SerialPortEvent.CTS: // 3 �������������

			case SerialPortEvent.DSR: // 4 ����������׼������

			case SerialPortEvent.RI: // 5 ����ָʾ

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 ��������������
				break;

			case SerialPortEvent.DATA_AVAILABLE: // 1 ���ڴ��ڿ�������
				byte[] data = null;
				try {
					if (serialPort == null) {
						JOptionPane.showMessageDialog(null, "���ڶ���Ϊ�գ�����ʧ�ܣ�", "����", JOptionPane.INFORMATION_MESSAGE);
					} else {
						data = SerialTool.readFromPort(serialPort); // ��ȡ���ݣ������ֽ�����
						System.out.println(data[0] +"--" + byteToBit(data[0]));
					}

				} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
					JOptionPane.showMessageDialog(null, e, "����", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}

				break;

			}

		}

	}
	
	// 010
	
	public static String byteToBit(byte b) {  
	    return "" +(byte)((b >> 7) & 0x1) +   
	    (byte)((b >> 6) & 0x1) +   
	    (byte)((b >> 5) & 0x1) +   
	    (byte)((b >> 4) & 0x1) +   
	    (byte)((b >> 3) & 0x1) +   
	    (byte)((b >> 2) & 0x1) +   
	    (byte)((b >> 1) & 0x1) +   
	    (byte)((b >> 0) & 0x1);  
	}

}
