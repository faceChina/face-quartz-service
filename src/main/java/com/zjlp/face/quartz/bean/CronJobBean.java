package com.zjlp.face.quartz.bean;

public class CronJobBean extends JobBean {

	/** 表达式 */
	private String cronExpression;

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
