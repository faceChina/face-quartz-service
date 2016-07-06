package com.zjlp.face.web.job.order;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.zjlp.face.util.file.PropertiesUtil;
import com.zjlp.face.web.job.PersistenceJobServiceLocator;
import com.zjlp.face.web.server.operation.redenvelope.business.RedenvelopeBusiness;
import com.zjlp.face.web.server.operation.redenvelope.domain.SendRedenvelopeRecord;

/**
 * 
 * @ClassName: RedenvelopeRefundJob 
 * @Description: 红包过期自动退款任务
 * @author cbc
 * @date 2015年10月20日 下午5:14:42
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RedenvelopeRefundJob implements Job {

	private Logger logger =  Logger.getLogger("redenvelopeJobExecuteLog");
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_REDENVELOPE_REFUND");
			logger.info("[RedenvelopeRefundJob] The jobFlag[RedenvelopeRefundJob] of SWITCH_JOB_REDENVELOPE_REFUND is "+jobFlag);
			if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
				logger.info("[RedenvelopeRefundJob] Run now .... ");
				String redenvelopeIdStr = (String) context.getJobDetail().getJobDataMap().get("redenvelopeId");
				Long redenvelopeId = Long.valueOf(redenvelopeIdStr);
				logger.info("[RedenvelopeRefundJob] Refunding redenvelope is : " +redenvelopeId);
				RedenvelopeBusiness redenvelopeBusiness = PersistenceJobServiceLocator.getJobService("redenvelopeBusiness", RedenvelopeBusiness.class);
				SendRedenvelopeRecord record = redenvelopeBusiness.getSendRocordById(redenvelopeId);
				if (null == record) {
					logger.warn("[RedenvelopeRefundJob] can't find redenvelope redenvelopeId is : "+redenvelopeId);
					return;
				}
				Long money = redenvelopeBusiness.sumHasReceiveMoney(redenvelopeId);
				if (money.compareTo(record.getAmount()) == -1) {
					redenvelopeBusiness.refund(redenvelopeId);
					logger.info("[RedenvelopeRefundJob] Refund redenvelopeId is : " +redenvelopeId);
				}
				logger.info("[RedenvelopeRefundJob] Ending now .... ");
			} else {
				logger.warn("[RedenvelopeRefundJob] The flg[SWITCH_JOB_REDENVELOPE_REFUND] of SWITCH_JOB_REDENVELOPE_REFUND is Closed");
				throw new JobExecutionException("红包过期退款自动任务开关未打开");
			}
		} catch (Exception e) {
			logger.error("[RedenvelopeRefundJob] Have Exception :"+e.getMessage(),e);
		}
	}

}
