package com.zjlp.face.quartz.proccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zjlp.face.account.dto.WithdrawReq;
import com.zjlp.face.account.dto.WithdrawRsp;
import com.zjlp.face.quartz.client.QuartzSimplePersistenceClient;
import com.zjlp.face.quartz.util.QuarzKeyUtil;
import com.zjlp.face.util.data.ProccessUtil;
import com.zjlp.face.util.file.PropertiesUtil;
import com.zjlp.face.web.job.withdraw.WithdrawResultQueryJob;

/**
 * 提现结果查询任务管理
 * 
 * @ClassName: WithdrawInfoQueryJobManager
 * @Description: (这里用一句话描述这个类的作用)
 * @author lys
 * @date 2015年3月17日 下午4:21:12
 */
@Component
public class WithdrawInfoQueryJobManager {

	private Logger _logger = Logger.getLogger("withdrawClientLog");

	@Autowired
	private QuartzSimplePersistenceClient quartzSimplePersistenceClient;

	/**
	 * 提现查询任务
	 * 
	 * @Title: queryWithdrawJob
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param withdrawRsp
	 *            提现响应数据
	 * @param withdrawReq
	 *            提现请求数据
	 * @param cardId
	 *            银行卡id
	 * @param accountId
	 *            钱包id
	 * @param cellPhone
	 *            手机号码
	 * @param date
	 *            时间
	 * @throws Exception
	 * @date 2015年3月17日 下午4:22:55
	 * @author lys
	 */
	public void queryWithdrawJob(WithdrawRsp withdrawRsp,
			WithdrawReq withdrawReq, Long cardId, Long accountId,
			String cellPhone, Date date) throws Exception {
		// 执行时间
		Date paramDate = ProccessUtil.getTime("TIMER_JOB_WITHDRAW_QUERY");
		// 运行次数
		String repeatCount = PropertiesUtil
				.getContexrtParam("REPEAT_COUNT_WITHDRAW_QUERY");
		// 间隔时间
		String intervalInSeconds = PropertiesUtil
				.getContexrtParam("INTERVAL_SECONDS_WITHDRAW_QUERY");
		_logger.info("[提现查询任务日志] 新建任务参数：时间[" + paramDate + "]，次数["
				+ repeatCount + "]，间隔时间：[" + intervalInSeconds + "]");

		String jobName = QuarzKeyUtil
				.getJobName("withdrawQueryName", paramDate);
		String groupName = "withdrawQuery";
		// 加入Job
		Map<String, Object> jobParamMap = new HashMap<String, Object>();
		jobParamMap.put("withdrawRsp", withdrawRsp);
		jobParamMap.put("withdrawReq", withdrawReq);
		jobParamMap.put("jobName", jobName);
		jobParamMap.put("groupName", groupName);
		jobParamMap.put("accountId", accountId);
		jobParamMap.put("cell", cellPhone);
		jobParamMap.put("date", date);
		jobParamMap.put("cardId", cardId);
		_logger.info("[提现查询任务日志] 新建任务参数：银行卡id：[" + cardId + "]，账户id：["
				+ accountId + "]，流水号：[" + withdrawReq.getTransferSerino() + "]");
		quartzSimplePersistenceClient.addRepeatJob(jobName, groupName,
				WithdrawResultQueryJob.class, paramDate, jobParamMap,
				Integer.valueOf(repeatCount),
				Integer.valueOf(intervalInSeconds));
		_logger.info("[提现查询任务日志] 开启提现查询任务，任务名：[" + jobName + "]，组名["
				+ groupName + "]");

	}
}
