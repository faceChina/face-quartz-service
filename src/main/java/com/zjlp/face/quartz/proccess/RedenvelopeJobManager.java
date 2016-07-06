package com.zjlp.face.quartz.proccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zjlp.face.quartz.client.QuartzSimplePersistenceClient;
import com.zjlp.face.quartz.util.QuarzKeyUtil;
import com.zjlp.face.util.data.ProccessUtil;
import com.zjlp.face.web.job.order.RedenvelopeRefundJob;

@Component
public class RedenvelopeJobManager {

	private Logger _logger = Logger.getLogger("jobInfoLog");
	
	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	private static final String REFUND_TIME = "TIMER_JOB_REDENVELOPE_REFUND";

	public void refund(String redenvelopeId) throws SchedulerException {
		try {
			_logger.info("当前启动【红包失效退款】定时执行任务："+redenvelopeId);
			if (StringUtils.isBlank(redenvelopeId)) {
				throw new IllegalArgumentException("The param  \"redenvelopeId\"  must be not null");
			}
			Date paramDate = ProccessUtil.getTime(REFUND_TIME);
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("redenvelopeId", redenvelopeId);
			String jobName = QuarzKeyUtil.getJobName("RedenvelopeRefundJobName", paramDate);
			String jobGroupName ="RedenvelopeRefundJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,RedenvelopeRefundJob.class, paramDate,jobParamMap);
			_logger.info("当前启动【红包失效退款】定时执行任务完成，等待完成："+redenvelopeId);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
}
