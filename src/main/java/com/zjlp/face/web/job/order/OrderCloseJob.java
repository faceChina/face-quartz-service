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
 * 订单自动关闭任务
 * @ClassName: OrderCloseJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author dzq
 * @date 2014年8月20日 下午6:08:11
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderCloseJob implements Job {
	
//	private Logger _orderJobLogger  = Logger.getLogger("OrderJobLogger");
	
	private Logger _orderJobLogger =  Logger.getLogger("orderJobExecuteLog");
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_ORDER_CLOSE");
			_orderJobLogger.info("[OrderCloseJob] The jobFlag[OrderCloseJob] of SWITCH_JOB_ORDERCLOSEJOB is "+jobFlag);
			if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
				_orderJobLogger.info("[OrderCloseJob] Run now .... ");
				String orderNo = (String) context.getJobDetail().getJobDataMap().get("orderNo");
				_orderJobLogger.info("[OrderCloseJob] Closing orderNo is : " +orderNo);
				SalesOrderBusiness salesOrderBusiness = PersistenceJobServiceLocator.getSalesOrderBusiness();
				SalesOrder salesOrder = salesOrderBusiness.getSalesOrderByOrderNo(orderNo);
				if (null == salesOrder) {
					_orderJobLogger.warn("[OrderCloseJob] can't find salesOrder orderNo is : "+orderNo);
					return;
				}
				_orderJobLogger.info("[OrderCloseJob] after close salesOrder States is : " + salesOrder.getStatus());
				if(Constants.STATUS_WAIT.equals(salesOrder.getStatus())){
					salesOrderBusiness.closeOrder(orderNo);
					_orderJobLogger.info("[OrderCloseJob] Closed orderNO is : " +orderNo);
				}
				_orderJobLogger.info("[OrderCloseJob] Ending now .... ");
			}else{
				_orderJobLogger.warn("[OrderCloseJob] The flg[SWITCH_JOB_ORDERCLOSEJOB] of SWITCH_JOB_ORDERCLOSEJOB is Closed");
				throw new JobExecutionException("[OrderCloseJob] The flg[SWITCH_JOB_ORDERCLOSEJOB] of SWITCH_JOB_ORDERCLOSEJOB is Closed");
			}
		} catch (Exception e) {
			_orderJobLogger.error("[OrderCloseJob] Have Exception :"+e.getMessage(),e);
		}
	}
}
