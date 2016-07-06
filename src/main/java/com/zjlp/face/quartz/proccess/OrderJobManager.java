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
import com.zjlp.face.web.job.order.ConfirmAppointOrderJob;
import com.zjlp.face.web.job.order.OrderCloseJob;
import com.zjlp.face.web.job.order.OrderCompileJob;
import com.zjlp.face.web.job.order.OrderReceiveJob;

@Component
public class OrderJobManager {
	
	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	private Logger _logger = Logger.getLogger("jobInfoLog");
	
	/**
	 * 自动关闭订单
	 * @Title: closeOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param orderNo
	 * @date 2014年8月20日 上午9:25:04
	 * @author dzq
	 */
	public void closeOrder(String orderNo)throws SchedulerException{
		try {
			_logger.info("当前启动【关闭订单】定时执行任务："+orderNo);
			if (StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\"  must be not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_ORDER_CLOSE");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("orderNo", orderNo);
			String jobName = QuarzKeyUtil.getJobName("OrderCloseJobName", paramDate);
			String jobGroupName ="OrderCloseJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,OrderCloseJob.class, paramDate,jobParamMap);
			_logger.info("当前启动【关闭订单】定时执行任务完成，等待完成："+orderNo);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
	/**
	 * 自动完成订单
	 * @Title: compileOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param orderNo
	 * @date 2014年8月20日 下午4:03:19
	 * @author dzq
	 */
	public void compileOrder(String orderNo)throws SchedulerException {
		try {
			_logger.info("当前启动【自动完成订单】定时执行任务："+orderNo);
			if (StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\" must be  not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_ORDER_COMPILE");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("orderNo", orderNo);
			String jobName = QuarzKeyUtil.getJobName("OrderCompileJobName", paramDate);
			String jobGroupName ="OrderCompileJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,OrderCompileJob.class, paramDate,jobParamMap);
			_logger.info("当前启动【自动完成订单】定时执行任务完成，等待执行："+orderNo);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}

	public void confirmAppointOrder(String orderNo) throws SchedulerException {
		try {
			_logger.info("当前启动【自动完成预约订单】定时执行任务："+orderNo);
			if (StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\"  must be not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_ORDER_CLOSE");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("orderNo", orderNo);
			/*String jobName = QuarzKeyUtil.getJobName("OrderCloseJobName", paramDate);*/
			String jobName = QuarzKeyUtil.getJobName("ConfirmAppointOrderJobName", paramDate);
			String jobGroupName ="ConfirmAppointOrderJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,ConfirmAppointOrderJob.class, paramDate,jobParamMap);
			_logger.info("当前启动【自动完成预约订单】定时执行任务完成："+orderNo);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
	/**
	 * 订单自动收货
	 * @Title: receiveOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param orderNo
	 * @date 2015年10月13日 下午7:20:13
	 * @author talo
	 */
	public void receiveOrder(String orderNo)throws SchedulerException {
		try {
			_logger.info("当前启动【订单自动收货】定时执行任务："+orderNo);
			if (StringUtils.isBlank(orderNo)) {
				throw new IllegalArgumentException("The param  \"orderNo\" must be  not null");
			}
			//获取配置时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_ORDER_RECEIVE");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("orderNo", orderNo);
			String jobName = QuarzKeyUtil.getJobName("OrderReceiveJobName", paramDate);
			String jobGroupName ="OrderReceiveJobGroup";
			quartzSimplePersistenceClient.addJob(jobName,jobGroupName,OrderReceiveJob.class, paramDate,jobParamMap);
			_logger.info("当前启动【订单自动收货】定时执行任务完成，等待执行："+orderNo);
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
}
