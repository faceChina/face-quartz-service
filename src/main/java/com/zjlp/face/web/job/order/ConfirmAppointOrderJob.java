package com.zjlp.face.web.job.order;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import com.zjlp.face.util.file.PropertiesUtil;
import com.zjlp.face.web.constants.Constants;
import com.zjlp.face.web.job.PersistenceJobServiceLocator;
import com.zjlp.face.web.server.trade.order.bussiness.SalesOrderBusiness;
import com.zjlp.face.web.server.trade.order.domain.SalesOrder;

/**
 * 预约订单自动确认
 * @ClassName: ConfirmAppointOrderJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author zyl
 * @date 2015年4月21日 下午6:08:11
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ConfirmAppointOrderJob implements Job {
	
	private Logger _orderJobLogger =  Logger.getLogger("orderJobExecuteLog");
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_ORDER_CLOSE");
			_orderJobLogger.info("[ConfirmAppointOrderJob] The jobFlag[ConfirmAppointOrderJob] of SWITCH_JOB_ConfirmAppointOrderJob is "+jobFlag);
			if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
				_orderJobLogger.info("[ConfirmAppointOrderJob] Run now .... ");
				String orderNo = (String) context.getJobDetail().getJobDataMap().get("orderNo");
				_orderJobLogger.info("[ConfirmAppointOrderJob] confirm orderNo is : " +orderNo);
				SalesOrderBusiness salesOrderBusiness = PersistenceJobServiceLocator.getSalesOrderBusiness();
				SalesOrder salesOrder = salesOrderBusiness.getSalesOrderByOrderNo(orderNo);
				if (null == salesOrder) {
					_orderJobLogger.warn("[ConfirmAppointOrderJob] can't find salesOrder orderNo is : "+orderNo);
					return;
				}
				_orderJobLogger.info("[ConfirmAppointOrderJob] after close salesOrder States is : " + salesOrder.getStatus());
				if(Constants.BOOKORDER_STATUS_WAIT.equals(salesOrder.getStatus())){
					salesOrderBusiness.confirmAppointOrder(orderNo);
					_orderJobLogger.info("[ConfirmAppointOrderJob] confirm orderNO is : " +orderNo);
				}
				_orderJobLogger.info("[ConfirmAppointOrderJob] Ending now .... ");
			}else{
				_orderJobLogger.warn("[ConfirmAppointOrderJob] The flg[SWITCH_JOB_ConfirmAppointOrderJob] of SWITCH_JOB_ConfirmAppointOrderJob is Closed");
				throw new JobExecutionException("[ConfirmAppointOrderJob] The flg[SWITCH_JOB_ConfirmAppointOrderJob] of SWITCH_JOB_ConfirmAppointOrderJob is Closed");
			}
		} catch (Exception e) {
			_orderJobLogger.error("[ConfirmAppointOrderJob] Have Exception :"+e.getMessage(),e);
		}
	}
}
