package com.zjlp.face.service.impl;

import java.util.Date;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zjlp.face.account.dto.WithdrawReq;
import com.zjlp.face.account.dto.WithdrawRsp;
import com.zjlp.face.exception.QuartzException;
import com.zjlp.face.factory.ResultFactory;
import com.zjlp.face.factory.ResultFactory.Code;
import com.zjlp.face.quartz.proccess.OrderJobManager;
import com.zjlp.face.quartz.proccess.PaymentJobManager;
import com.zjlp.face.quartz.proccess.RedenvelopeJobManager;
import com.zjlp.face.quartz.proccess.WithdrawInfoQueryJobManager;
import com.zjlp.face.service.JobType;
import com.zjlp.face.service.QuartzService;
import com.zjlp.face.util.exception.AssertUtil;
import com.zjlp.face.util.exception.BaseException;

/**
 * 
 * @ClassName: QuartzServiceImpl 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author lys
 * @date 2015年10月28日 上午11:51:02
 */
@Service("quartzService")
public class QuartzServiceImpl implements QuartzService {

	@Autowired
	private WithdrawInfoQueryJobManager withdrawInfoQueryJobManager;
	@Autowired
	private OrderJobManager orderJobManager;
	@Autowired
	private PaymentJobManager paymentJobManager;
	@Autowired
	private RedenvelopeJobManager redenvelopeManager;

	@Override
	public String beginJob(String requestJson) throws QuartzException {
		
		try {
			if (StringUtils.isEmpty(requestJson)) {
				return ResultFactory.createJsonResult(Code.ERR_PARAM);
			}
			
			JSONObject jsonObject = JSONObject.fromObject(requestJson);
			AssertUtil.notNull(jsonObject, "转换JSON失败");
			String type = jsonObject.getString("type");
			
			
			String data = jsonObject.getString("data");
			
			if (JobType.WITHDRAW_QUERY.getCode().equals(type)) {
				
				this.doWithdrawJob(data);
				
			} else if (JobType.CLOSE_ORDER.getCode().equals(type)) {
				
				this.doCloseOrderJob(data);
				
			} else if (JobType.COMPILE_ORDER.getCode().equals(type)) {
				
				this.doCompileOrderJob(data);
				
			} else if (JobType.CONFIRM_APPOINTORDER.getCode().equals(type)) {
				
				this.doConfirmAppointOrderJob(data);
				
			} else if (JobType.RECIEVE_ORDER.getCode().equals(type)) {
				
				this.doConfirmOrderJob(data);
				
			} else if (JobType.REDENVELOPE_REFUND.getCode().equals(type)) {
				
				this.doRedBugRefundJob(data);
			}
			
			return ResultFactory.createJsonResult(Code.SUCC);
			
		} catch (BaseException e) {
			return ResultFactory.createJsonErrResult(e.getMessage());
		} catch (Exception e) {
			return ResultFactory.createJsonErrResult(e.getMessage());
		}
	}
	
	private void doWithdrawJob(String requestJson) throws Exception{
		
		JSONObject json = JSONObject.fromObject(requestJson);
		AssertUtil.notNull(json, "data转换JSON失败");
		
		String withdrawRspStr = json.getString("withdrawRsp");
		AssertUtil.hasLength(withdrawRspStr, "withdrawRspStr为空");
		JSONObject withdrawRspJson = JSONObject.fromObject(withdrawRspStr);
		AssertUtil.notNull(withdrawRspJson, "withdrawRspStr转JSON失败");
		WithdrawRsp withdrawRsp = (WithdrawRsp)JSONObject.toBean(withdrawRspJson, WithdrawRsp.class);
		
		String withdrawReqStr = json.getString("withdrawReq");
		AssertUtil.hasLength(withdrawReqStr, "withdrawReqStr为空");
		JSONObject withdrawReqObj = JSONObject.fromObject(withdrawReqStr);
		AssertUtil.notNull(withdrawReqObj,"withdrawReqStr转换JSON失败");
		WithdrawReq withdrawReq = (WithdrawReq)JSONObject.toBean(withdrawReqObj,WithdrawReq.class);
		
		Long cardId = JSONNull.getInstance().equals(json.get("cardId")) ? null : json.getLong("cardId");
		Long accountId = json.getLong("accountId");
		String cellPhone = json.getString("cellPhone");
		Long dateLong = json.getLong("date");
		Date date = new Date(dateLong);
		withdrawInfoQueryJobManager.queryWithdrawJob(withdrawRsp, withdrawReq, cardId, accountId, cellPhone, date);
	}
	
	private void doCloseOrderJob(String orderNo) throws SchedulerException{
		orderJobManager.closeOrder(orderNo);
	}
	
	private void doCompileOrderJob(String orderNo) throws SchedulerException{
		orderJobManager.compileOrder(orderNo);
	}
	
	private void doConfirmAppointOrderJob(String orderNo) throws SchedulerException{
		orderJobManager.confirmAppointOrder(orderNo);
	}
	
	private void doConfirmOrderJob(String orderNo) throws SchedulerException{
		orderJobManager.receiveOrder(orderNo);
	}
	
	private void doRedBugRefundJob(String redenvelopeId) throws SchedulerException{
		redenvelopeManager.refund(redenvelopeId);
	}

}
