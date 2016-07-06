package com.zjlp.face.metaq.listener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.metamorphosis.client.extension.spring.DefaultMessageListener;
import com.taobao.metamorphosis.client.extension.spring.MetaqMessage;
import com.zjlp.face.account.dto.WithdrawReq;
import com.zjlp.face.account.dto.WithdrawRsp;
import com.zjlp.face.quartz.proccess.OrderJobManager;
import com.zjlp.face.quartz.proccess.PaymentJobManager;
import com.zjlp.face.quartz.proccess.RedenvelopeJobManager;
import com.zjlp.face.quartz.proccess.WithdrawInfoQueryJobManager;
import com.zjlp.face.util.exception.AssertUtil;
/**
 * MetaQ消息监听
 * @author Administrator
 *
 */
public class DateMessageListener extends DefaultMessageListener<String> {

	private Logger _logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private OrderJobManager orderJobManager;
	
	@Autowired
	private PaymentJobManager paymentJobManager;
	
	@Autowired
	private WithdrawInfoQueryJobManager withdrawInfoQueryJobManager;
	
	@Autowired
	private RedenvelopeJobManager redenvelopeManager;
	/**
	 * 监听服务中的消息
	 * @Title: onReceiveMessages 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param msg
	 * @author Hongbo Peng
	 */
	@Override
	public void onReceiveMessages(MetaqMessage<String> msg) {
		byte[] date = msg.getData();
    	try {
    		String dastr =URLDecoder.decode(new String(date), "UTF-8");
			if ("jobtopic".equals(msg.getTopic())){
				this.messageConsumer(dastr);
	    	}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			_logger.error(msg.getTopic()+":", e);
		}
	}
	
	/**
	 * 消费通知消息
	 * @Title: messageConsumer 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param dastr
	 * @author Hongbo Peng
	 */
	private void messageConsumer(String dastr){
		try {
			AssertUtil.hasLength(dastr, "消息内容为空");
			JSONObject jsonObject = JSONObject.fromObject(dastr);
			AssertUtil.notNull(jsonObject, "转换JSON失败");
			String type = jsonObject.getString("type");
			AssertUtil.hasLength(type, "参数类型为空");
			String data = jsonObject.getString("data");
			AssertUtil.hasLength(data, "参数data为空");
			
			if ( "closeOrder".equals(type) ) {
				orderJobManager.closeOrder(data);
				
			} else if ( "compileOrder".equals(type) ){
				orderJobManager.compileOrder(data);
				
			} else if ( "confirmAppointOrder".equals(type) ){
				orderJobManager.confirmAppointOrder(data);
				
			/*} else if ( "bindPayQueryOrder".equals(type) || "bindPayRechargeQueryOrder".equals(type) ){
				JSONObject json = JSONObject.fromObject(data);
				AssertUtil.notNull(json, "data转换JSON失败");
				String transactionSerialNumber = json.getString("transactionSerialNumber");
				AssertUtil.hasLength(transactionSerialNumber, "transactionSerialNumber为空");
				String bankcardStr = json.getString("bankCard");
				AssertUtil.hasLength(bankcardStr, "bankcardStr为空");
				JSONObject bankJson = JSONObject.fromObject(bankcardStr);
				AssertUtil.notNull(bankJson, "bankcardStr转换JSON失败");
				BankCard bankCard = (BankCard) JSONObject.toBean(bankJson, BankCard.class);
				
				if( "bindPayQueryOrder".equals(type) ){
					paymentJobManager.bindPayQueryOrder(transactionSerialNumber, bankCard);
				} else if( "bindPayRechargeQueryOrder".equals(type) ){
					String memberCardStr = json.getString("memberCard");
					AssertUtil.hasLength(memberCardStr, "memberCardStr为空");
					JSONObject memberCardJson = JSONObject.fromObject(memberCardStr);
					AssertUtil.notNull(memberCardJson, "memberCardStr转换JSON失败");
					MemberCard memberCard = (MemberCard)JSONObject.toBean(memberCardJson, MemberCard.class);
					paymentJobManager.bindPayRechargeQueryOrder(transactionSerialNumber, bankCard, memberCard);
				}*/
			} else if ( "queryWithdrawJob".equals(type) ){
				JSONObject json = JSONObject.fromObject(data);
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
			} else if ("receiveOrder".equals(type)) {
				orderJobManager.receiveOrder(data);
			} else if ("refundRedenvelope".equals(type)) {
				redenvelopeManager.refund(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.error(dastr, e);
		}
	}
}
