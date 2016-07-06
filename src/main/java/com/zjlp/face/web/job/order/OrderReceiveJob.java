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
 * 订单自动 已收货 任务
 * @ClassName: OrderReceiveJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author talo
 * @date 2015年10月20日 下午19:20:22
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderReceiveJob implements Job {

	private Logger _orderJobLogger =  Logger.getLogger("orderJobExecuteLog");
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_ORDER_RECEIVE");
			_orderJobLogger.info("[OrderReceiveJob] The jobFlag[OrderReceiveJob] of SWITCH_JOB_ORDER_RECEIVE is "+jobFlag);
			if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
				_orderJobLogger.info("[OrderReceiveJob] Run now .... ");
				String orderNo = (String) context.getJobDetail().getJobDataMap().get("orderNo");
				_orderJobLogger.info("[OrderReceiveJob] Receive orderNo is : " +orderNo);
				SalesOrderBusiness salesOrderBusiness = PersistenceJobServiceLocator.getSalesOrderBusiness();
				SalesOrder salesOrder = salesOrderBusiness.getSalesOrderByOrderNo(orderNo);
				if (null == salesOrder) {
					_orderJobLogger.warn("[OrderReceiveJob] can't find salesOrder orderNo is : "+orderNo);
					return;
				}
				_orderJobLogger.info("[OrderReceiveJob] after Receive salesOrder States is : " + salesOrder.getStatus());
				if(Constants.STATUS_SEND.equals(salesOrder.getStatus())){
					salesOrderBusiness.receiveAutoOrder(orderNo,0L);
					_orderJobLogger.info("[OrderReceiveJob] Receive orderNO is : " +orderNo);
				}
				_orderJobLogger.info("[OrderReceiveJob] Ending now .... ");
			}else{
				_orderJobLogger.warn("[OrderReceiveJob] The flg[OrderReceiveJob] of SWITCH_JOB_ORDER_RECEIVE is Closed");
				throw new JobExecutionException("[OrderReceiveJob] The flg[OrderReceiveJob] of SWITCH_JOB_ORDER_RECEIVE is Closed");
			}
		} catch (Exception e) {
			_orderJobLogger.error("[OrderReceiveJob] Have Exception :"+e.getMessage(),e);
		}
	}

}
