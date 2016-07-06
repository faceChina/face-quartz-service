package com.zjlp.face.quartz.bean;

import java.util.Date;
import java.util.Map;

import org.quartz.Job;
import org.quartz.Scheduler;

/**
 * 
 * @ClassName: JobBean 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author dzq
 * @date 2014年8月22日 下午3:44:21
 */
public class JobBean {
	
	/** 执行任务所需的调试器名称 */
	private String schedulerName = "MyScheduler";
	/** 执行任务的类 */
	private Class<? extends Job> jobClazz;
	/** 执行任务的参数集合 */
	private Map<String, Object> params;
	/** 执行时间 */
	private Date exeDate;
	/** 执行任务的名称 */
	private String jobName;
	/** 执行任务的组名 */
	private String jobGroupName = Scheduler.DEFAULT_GROUP;
	
	
	

	public Class<? extends Job> getJobClazz() {
		return jobClazz;
	}

	public void setJobClazz(Class<? extends Job> jobClazz) {
		this.jobClazz = jobClazz;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroupName() {
		return jobGroupName;
	}

	public void setJobGroupName(String jobGroupName) {
		this.jobGroupName = jobGroupName;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public Date getExeDate() {
		return exeDate;
	}

	public void setExeDate(Date exeDate) {
		this.exeDate = exeDate;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
