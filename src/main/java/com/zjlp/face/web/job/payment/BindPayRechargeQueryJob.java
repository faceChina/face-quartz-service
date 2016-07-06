package com.zjlp.face.web.job.payment;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.zjlp.face.web.job.PersistenceJobServiceLocator;
import com.zjlp.face.web.server.operation.member.domain.MemberCard;
import com.zjlp.face.web.server.trade.payment.bussiness.RechargePayBusiness;
import com.zjlp.face.web.server.user.bankcard.domain.BankCard;
/**
 * 绑定支付充值会员卡 查询订单消费 任务
* @ClassName: BindPayRechargeQueryJon 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author phb 
* @date 2015年6月1日 下午2:45:32 
*
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BindPayRechargeQueryJob implements Job {

	private Logger _log = Logger.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			_log.info("绑定支付查询订单开始...");
			JobDataMap jobDataMap =  context.getJobDetail().getJobDataMap();
			String transactionSerialNumber = (String)jobDataMap.get("transactionSerialNumber");
			_log.info(new StringBuffer("绑定支付查询订单流水：").append(transactionSerialNumber));
			BankCard bankCard = (BankCard)jobDataMap.get("bankCard");
			MemberCard memberCard = (MemberCard)jobDataMap.get("memberCard");
			RechargePayBusiness rechargePayBusiness = PersistenceJobServiceLocator.getJobService(RechargePayBusiness.class);
			rechargePayBusiness.queryBindPayAndConsumer(transactionSerialNumber, bankCard,memberCard);
			//任务结束
			context.getScheduler().deleteJob(context.getJobDetail().getKey());
			_log.info("绑定支付查询订单结束...");
		} catch (Exception e) {
			_log.error("绑定支付查询订单处理支付业务发生异常",e);
			throw new JobExecutionException(e);
		}
	}

}
