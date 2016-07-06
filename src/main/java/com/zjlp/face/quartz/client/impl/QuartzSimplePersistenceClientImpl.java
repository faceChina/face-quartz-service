package com.zjlp.face.quartz.client.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjlp.face.quartz.bean.SimpleJobBean;
import com.zjlp.face.quartz.client.QuartzSimplePersistenceClient;
import com.zjlp.face.quartz.scheduler.SimpleRepeatSchedulePersistence;
import com.zjlp.face.quartz.scheduler.SimpleSchedulePersistence;


@Service
public class QuartzSimplePersistenceClientImpl implements QuartzSimplePersistenceClient {

	@Autowired
	private SimpleSchedulePersistence simpleSchedulePersistence;
	
	@Autowired
	private SimpleRepeatSchedulePersistence simpleRepeatSchedulePersistence;
	

	@Override
	public void addJob(Class<? extends Job> clazz, Date date)
			throws SchedulerException {
		try {
			this.addJob(null, null, clazz, date, null);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	@Override
	public void addJob(Class<? extends Job> clazz, Date date,
			Map<String, Object> jobParamMap) throws SchedulerException {
		try {
			this.addJob(null, null, clazz, date, jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
	@Override
	public void addJob(String groupName, Class<? extends Job> clazz, Date date,
			Map<String, Object> jobParamMap) throws SchedulerException {
		try {
			this.addJob(null, groupName, clazz, date, jobParamMap);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	@Override
	public void addJob(String jobName, String groupName,
			Class<? extends Job> clazz, Date date,
			Map<String, Object> jobParamMap) throws SchedulerException {
		try {
			if (null == clazz) {
				throw new IllegalArgumentException("类加载参数错误");  
			}
			if (!this._isValidExpression(date)) {  
				throw new IllegalArgumentException("时间参数错误");  
			}
			SimpleJobBean jobBean = new SimpleJobBean();
			jobBean.setJobClazz(clazz);
			jobBean.setExeDate(date);
			jobBean.setParams(jobParamMap);
			jobBean.setJobGroupName(groupName);
			jobBean.setJobName(jobName);
			simpleSchedulePersistence.scheduleJob(jobBean);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
	@Override
	public void addRepeatJob(String jobName, String groupName,
			Class<? extends Job> clazz, Date date,
			Map<String, Object> jobParamMap, Integer repeatCount,
			Integer intervalInSeconds) throws SchedulerException {
		try {
			if (null == clazz) {
				throw new IllegalArgumentException("类加载参数错误");  
			}
			if (!this._isValidExpression(date)) {  
				throw new IllegalArgumentException("时间参数错误");  
			}
			if (null == repeatCount || 0>= repeatCount) {
				throw new IllegalArgumentException("运行次数 参数错误");  
			}
			if (null == repeatCount || 0>= repeatCount) {
				throw new IllegalArgumentException("运行次数 参数错误");  
			}
			if (null == intervalInSeconds || 0 >= intervalInSeconds) {
				throw new IllegalArgumentException("间隔时间 参数错误");  
			}
			SimpleJobBean jobBean = new SimpleJobBean();
			jobBean.setJobClazz(clazz);
			jobBean.setExeDate(date);
			jobBean.setParams(jobParamMap);
			jobBean.setJobGroupName(groupName);
			jobBean.setJobName(jobName);
			jobBean.setRepeatCount(repeatCount);
			jobBean.setIntervalInSeconds(intervalInSeconds);
			simpleRepeatSchedulePersistence.scheduleJob(jobBean);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
		
	}

	@Override
	public void deleteJob(String jobName, String groupName) throws SchedulerException {
		try {
			if (StringUtils.isBlank(jobName)) {
				throw new IllegalArgumentException("任务名称参数为空!");  
			}
			if (StringUtils.isBlank(groupName)) {
				throw new IllegalArgumentException("任务组名参数为空!");  
			}
			simpleSchedulePersistence.deleteJob(jobName,groupName);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}  
	
  
    private boolean _isValidExpression(final Date startTime) {  
        SimpleTriggerImpl trigger = new SimpleTriggerImpl();  
        trigger.setStartTime(startTime);  
        Date date = trigger.computeFirstFireTime(null);  
        return date != null && date.after(new Date());  
    }


}
