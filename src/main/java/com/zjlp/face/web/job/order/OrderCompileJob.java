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
 * 订单自动 交易完成 任务
 * @ClassName: OrderCompileJob
 * @Description: (这里用一句话描述这个类的作用)
 * @author dzq
 * @date 2014年8月20日 下午6:07:49
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderCompileJob implements Job {

	private Logger _orderJobLogger =  Logger.getLogger("orderJobExecuteLog");
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			String jobFlag = PropertiesUtil.getContexrtParam("SWITCH_JOB_ORDER_COMPILE");
			_orderJobLogger.info("[OrderCompileJob] The jobFlag[OrderCompileJob] of SWITCH_JOB_ORDER_COMPILE is "+jobFlag);
			if (StringUtils.isNotBlank(jobFlag) && "1".equals(jobFlag)) {
				_orderJobLogger.info("[OrderCompileJob] Run now .... ");
				String orderNo = (String) context.getJobDetail().getJobDataMap().get("orderNo");
				_orderJobLogger.info("[OrderCompileJob] Compiling orderNo is : " +orderNo);
				SalesOrderBusiness salesOrderBusiness = PersistenceJobServiceLocator.getSalesOrderBusiness();
				SalesOrder salesOrder = salesOrderBusiness.getSalesOrderByOrderNo(orderNo);
				if (null == salesOrder) {
					_orderJobLogger.warn("[OrderCompileJob] can't find salesOrder orderNo is : "+orderNo);
					return;
				}
				_orderJobLogger.info("[OrderCompileJob] after compiling salesOrder States is : " + salesOrder.getStatus());
				if(Constants.STATUS_RECEIVE.equals(salesOrder.getStatus())){
					salesOrderBusiness.compileAutoOrder(orderNo,0L);
					_orderJobLogger.info("[OrderCompileJob] Compiled orderNO is : " +orderNo);
				}
				_orderJobLogger.info("[OrderCompileJob] Ending now .... ");
			}else{
				_orderJobLogger.warn("[OrderCompileJob] The flg[OrderCompileJob] of SWITCH_JOB_ORDER_COMPILE is Closed");
				throw new JobExecutionException("[OrderCompileJob] The flg[OrderCompileJob] of SWITCH_JOB_ORDER_COMPILE is Closed");
			}
		} catch (Exception e) {
			_orderJobLogger.error("[OrderCompileJob] Have Exception :"+e.getMessage(),e);
		}
	}

}
