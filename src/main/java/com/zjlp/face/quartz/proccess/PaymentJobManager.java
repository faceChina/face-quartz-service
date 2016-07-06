package com.zjlp.face.quartz.proccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zjlp.face.quartz.client.QuartzSimplePersistenceClient;
import com.zjlp.face.quartz.util.QuarzKeyUtil;
import com.zjlp.face.util.data.ProccessUtil;
import com.zjlp.face.util.file.PropertiesUtil;
import com.zjlp.face.web.job.payment.BindPayQueryJob;
import com.zjlp.face.web.job.payment.BindPayRechargeQueryJob;
import com.zjlp.face.web.server.operation.member.domain.MemberCard;
import com.zjlp.face.web.server.user.bankcard.domain.BankCard;
@Component
public class PaymentJobManager {

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;
	
	/**
	 * 绑定支付 定时查询订单
	 * @Title: bindPayQueryOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param transactionSerialNumber
	 * @param bankCard
	 * @throws SchedulerException
	 * @return void
	 * @author phb
	 * @date 2015年6月1日 下午2:52:14
	 */
	public void bindPayQueryOrder(String transactionSerialNumber,BankCard bankCard)throws SchedulerException{
		try {
			// 执行时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_BIND_PAY_QUERY_ORDER");
			// 运行次数
			String repeatCount = PropertiesUtil
					.getContexrtParam("REPEAT_COUNT_BIND_PAY_QUERY_ORDER");
			// 间隔时间
			String intervalInSeconds = PropertiesUtil
					.getContexrtParam("INTERVAL_SECONDS_BIND_PAY_QUERY_ORDER");
			if (StringUtils.isBlank(transactionSerialNumber)) {
				throw new IllegalArgumentException("The param  \"transactionSerialNumber\"  must be not null");
			}
			if(null == bankCard){
				throw new IllegalArgumentException("The param  \"bankCard\"  must be not null");
			}
			//获取配置时间
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("transactionSerialNumber", transactionSerialNumber);
			jobParamMap.put("bankCard", bankCard);
			String jobName = QuarzKeyUtil.getJobName("BindPayQueryOrderJobName", paramDate);
			String jobGroupName ="BindPayQueryOrderJobGroup";
			quartzSimplePersistenceClient.addRepeatJob(jobName, jobGroupName,
					BindPayQueryJob.class, paramDate, jobParamMap,
					Integer.valueOf(repeatCount),
					Integer.valueOf(intervalInSeconds));
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
	
	/**
	 * 绑定支付充值会员卡 定时查询订单
	 * @Title: bindPayRechargeQueryOrder
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param transactionSerialNumber
	 * @param bankCard
	 * @param memberCard
	 * @throws SchedulerException
	 * @return void
	 * @author phb
	 * @date 2015年6月1日 下午2:52:26
	 */
	public void bindPayRechargeQueryOrder(String transactionSerialNumber,BankCard bankCard,MemberCard memberCard)throws SchedulerException{
		try {
			if (StringUtils.isBlank(transactionSerialNumber)) {
				throw new IllegalArgumentException("The param  \"transactionSerialNumber\"  must be not null");
			}
			if(null == bankCard){
				throw new IllegalArgumentException("The param  \"bankCard\"  must be not null");
			}
			if(null == memberCard){
				throw new IllegalArgumentException("The param  \"memberCard\"  must be not null");
			}
			// 执行时间
			Date paramDate = ProccessUtil.getTime("TIMER_JOB_BIND_PAY_QUERY_ORDER");
			// 运行次数
			String repeatCount = PropertiesUtil
					.getContexrtParam("REPEAT_COUNT_BIND_PAY_QUERY_ORDER");
			// 间隔时间
			String intervalInSeconds = PropertiesUtil
					.getContexrtParam("INTERVAL_SECONDS_BIND_PAY_QUERY_ORDER");
			Map<String, Object> jobParamMap  = new HashMap<String, Object>();
			jobParamMap.put("transactionSerialNumber", transactionSerialNumber);
			jobParamMap.put("bankCard", bankCard);
			jobParamMap.put("memberCard", memberCard);
			String jobName = QuarzKeyUtil.getJobName("BindPayRechargeQueryOrderJobName", paramDate);
			String jobGroupName ="BindPayRechargeQueryOrderJobGroup";
			quartzSimplePersistenceClient.addRepeatJob(jobName, jobGroupName,
					BindPayRechargeQueryJob.class, paramDate, jobParamMap,
					Integer.valueOf(repeatCount),
					Integer.valueOf(intervalInSeconds));
		} catch (Exception e) {
			throw new SchedulerException(e);
		}
	}
}
