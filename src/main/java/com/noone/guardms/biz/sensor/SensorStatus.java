package com.noone.guardms.biz.sensor;

public class SensorStatus {
	
	boolean hasPerson;
	
	boolean isDoorOpen;

	public boolean isHasPerson() {
		return hasPerson;
	}

	public void setHasPerson(boolean hasPerson) {
		this.hasPerson = hasPerson;
	}

	public boolean isDoorOpen() {
		return isDoorOpen;
	}

	public void setDoorOpen(boolean isDoorOpen) {
		this.isDoorOpen = isDoorOpen;
	}
}
