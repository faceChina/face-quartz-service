package com.zjlp.face.quartz.client.impl;

import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjlp.face.quartz.bean.CronJobBean;
import com.zjlp.face.quartz.client.QuartzCronPersistenceClient;
import com.zjlp.face.quartz.scheduler.CronSchedulePersistence;

@Service
public class QuartzCronPersistenceClientImpl implements QuartzCronPersistenceClient{
	
	@Autowired
	private CronSchedulePersistence cronSchedulePersistence;

	@Override
	public void addJob(Class<? extends Job> clazz, String cronExpression)
			throws SchedulerException {
		try {
			this.addJob(null, null, clazz, cronExpression, null);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	@Override
	public void addJob(Class<? extends Job> clazz, String cronExpression,
			Map<String, Object> jobParamMap) throws SchedulerException {
		try {
			this.addJob(null, null, clazz, cronExpression, jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	@Override
	public void addJob(String groupName, Class<? extends Job> clazz,
			String cronExpression, Map<String, Object> jobParamMap)
			throws SchedulerException {
		try {
			this.addJob(null, groupName, clazz, cronExpression, jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	@Override
	public void addJob(String jobName, String groupName,
			Class<? extends Job> clazz, String cronExpression,
			Map<String, Object> jobParamMap) throws SchedulerException {
		try {
			try {
				if (null == clazz) {
					throw new IllegalArgumentException("类加载参数错误");  
				}
				//验证表达式
				if (!CronExpression.isValidExpression(cronExpression)) {  
					throw new IllegalArgumentException("表达式不合法!");  
				}
				CronJobBean jobBean = new CronJobBean();
				jobBean.setJobClazz(clazz);
				jobBean.setCronExpression(cronExpression);
				jobBean.setParams(jobParamMap);
				jobBean.setJobGroupName(groupName);
				jobBean.setJobName(jobName);
				cronSchedulePersistence.scheduleJob(jobBean);
			} catch (Exception e) {
				throw new SchedulerException(e);
			}
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	@Override
	public void deleteJob(String jobName, String jobGroupName)
			throws SchedulerException {
		try {
			
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
//	private boolean isValidExpression(final CronExpression cronExpression) {  
//	  CronTriggerImpl trigger = new CronTriggerImpl();  
//	  trigger.setCronExpression(cronExpression);  
//	  Date date = trigger.computeFirstFireTime(null);  
//	  return date != null && date.after(new Date());  
//	}  

}
