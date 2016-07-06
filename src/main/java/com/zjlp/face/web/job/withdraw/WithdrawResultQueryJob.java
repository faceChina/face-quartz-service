package com.zjlp.face.web.job.withdraw;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.zjlp.face.account.dto.WithdrawReq;
import com.zjlp.face.account.dto.WithdrawRsp;
import com.zjlp.face.account.service.WithdrawService;
import com.zjlp.face.util.calcu.CalculateUtils;
import com.zjlp.face.web.component.sms.SmsProccessor;
import com.zjlp.face.web.constants.Constants;
import com.zjlp.face.web.job.PersistenceJobServiceLocator;
import com.zjlp.face.web.server.trade.account.business.WithdrawBusiness;

/**
 * 提现查询任务
 * @ClassName: WithdrawResultQueryJob 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author lys
 * @date 2015年3月17日 下午4:19:03
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WithdrawResultQueryJob implements Job {

	/** 没有该提现结果 */
	public static final Integer WD_STATE_NORECORD = 10;
	private Logger _logger  = Logger.getLogger("withdrawClientLog");
	
	/**
	 * WithdrawRsp withdrawRsp, WithdrawReq withdrawReq, Long recordId, Long cardId,
			Account account, Date date
	 */
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			_logger.info("[提现查询任务日志] 开始执行任务");
			/** 获取运行参数 */
			JobDataMap jobDataMap =  context.getJobDetail().getJobDataMap();
			Long cardId = (Long)jobDataMap.get("cardId");
			Long accountId = (Long)jobDataMap.get("accountId");
			String phone = (String)jobDataMap.get("cell");
			WithdrawReq withdrawReq = (WithdrawReq)jobDataMap.get("withdrawReq");
			WithdrawReq queryReq = new WithdrawReq();
			queryReq.setServiceType(Constants.SERVICE_QUERY_WD);
			queryReq.setTransferSerino(withdrawReq.getTransferSerino());
			queryReq.setAccountId(accountId);
			queryReq.setTransferAmount(withdrawReq.getTransferAmount());
			/** 查询任务开启 */
			String price = CalculateUtils.converPennytoYuan(withdrawReq.getTransferAmount());
			WithdrawService withdrawService = PersistenceJobServiceLocator.getJobService(WithdrawService.class);
			WithdrawBusiness withdrawBusiness = PersistenceJobServiceLocator.getJobService(WithdrawBusiness.class);
			WithdrawRsp rsp = withdrawService.transferResultQuery(queryReq);
			if (Constants.WD_STATE_SUCC.equals(rsp.getStates())) {
				
				withdrawBusiness.afterSuccess(withdrawReq, rsp, cardId);
				//短信提醒
				SmsProccessor.sendWithdrawMessage(Constants.WD_STATE_SUCC, phone, withdrawReq.getReciveAccountNo(), 
						withdrawReq.getBankName(), price);
				//任务结束
				context.getScheduler().deleteJob(context.getJobDetail().getKey());
				_logger.info("[提现查询任务日志] 流水号："+withdrawReq.getTransferSerino()+"查询任务执行结束，执行结果：成功");
			} else if (Constants.WD_STATE_FAIL.equals(rsp.getStates())) {
				
				withdrawBusiness.afterFail(withdrawReq, rsp.getErrorMsg(), accountId);
				//短信提醒
				SmsProccessor.sendWithdrawMessage(Constants.WD_STATE_FAIL, phone, withdrawReq.getReciveAccountNo(), 
						withdrawReq.getBankName(), price);
				//任务结束
				context.getScheduler().deleteJob(context.getJobDetail().getKey());
				_logger.info("[提现查询任务日志] 流水号："+withdrawReq.getTransferSerino()+"查询任务执行结束，执行结果：失败");
			} else if (WD_STATE_NORECORD.equals(rsp.getStates())) {
				//短信提醒
				SmsProccessor.sendWithdrawNoRecordMsg(withdrawReq.getTransferSerino(), price);
				//任务结束
				context.getScheduler().deleteJob(context.getJobDetail().getKey());
				_logger.info("[提现查询任务日志] 流水号："+withdrawReq.getTransferSerino()+"查询任务执行结束，执行结果：该记录不存在，需要人工处理。");
			} else { //再次执行
				_logger.info("[提现查询任务日志] 提现任务须再次进行查询，流水号："+withdrawReq.getTransferSerino());
			}
		} catch (Exception e) {
			_logger.info("[提现查询任务日志] 查询提现结果发生异常", e);
			throw new JobExecutionException(e);
		}
	}

}
