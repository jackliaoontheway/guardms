package com.noone.guardms.common.basemodel;

import java.util.Calendar;

import org.apache.log4j.Logger;

public class ErrorInfo {
	private String code;
	private String desc;
	private SEVERITY severity;

	public static enum SEVERITY {
		INFO, WARN, ERROR
	};

	public ErrorInfo(String errorInfo) {
		String desc = " ref#" + Calendar.getInstance().getTimeInMillis();
		this.code = "Error";
		this.desc = errorInfo;
		this.severity = SEVERITY.ERROR;
		logger.error(errorInfo + desc);
	}

	protected final Logger logger = Logger.getLogger(this.getClass());

	protected ErrorInfo(String code, SEVERITY severity, Throwable t, String desc) {
		desc += " ref#" + Calendar.getInstance().getTimeInMillis();
		switch (severity) {
		case ERROR:
			logger.error(code + ": " + desc, t);
			break;
		case INFO:
			logger.info(code + ": " + desc, t);
			break;
		case WARN:
			logger.warn(code + ": " + desc, t);
			break;
		default:
			break;

		}
		this.code = code;
		this.desc = desc;
		this.severity = severity;
	}

	protected static String populateDescription(String desc, String... params) {
		for (int i = 0; params != null && i < params.length; i++) {
			String param = params[i];
			if (param != null)
				desc = desc.replaceFirst("\\{" + i + "\\}", param);
		}
		return desc;
	}

	protected static String populateDescription(String desc, Throwable t) {
		String msg = t.getMessage() != null ? t.getMessage() : "";
		if (desc.indexOf("{0}") != -1) {
			desc = desc.replaceFirst("\\{0\\}", msg);
		} else {
			desc += " " + msg;
		}
		return desc;
	}

	@Deprecated
	protected ErrorInfo(String code, String desc) {
		this(code, SEVERITY.ERROR, desc);
	}

	protected ErrorInfo(String name, SEVERITY severity, String desc) {
		this(name, severity, new Throwable(desc), desc);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + code + ": " + desc + "]";
	}

	public SEVERITY getSeverity() {
		return severity;
	}

	public void setSeverity(SEVERITY severity) {
		this.severity = severity;
	}

}
