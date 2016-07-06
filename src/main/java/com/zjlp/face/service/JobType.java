package com.zjlp.face.service;

/**
 * 任务类型
 * @ClassName: JobType 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author lys
 * @date 2015年10月27日 下午3:26:04
 */
public enum JobType {

	/** 提现结果查询 */
	WITHDRAW_QUERY("WITHDRAW_QUERY"),
	/** 关闭订单 */
	CLOSE_ORDER("CLOSE_ORDER"),
	/** 完成订单 */
	COMPILE_ORDER("COMPILE_ORDER"),
	/** 订单收货 */
	RECIEVE_ORDER("RECIEVE_ORDER"),
	/** 红包退款 */
	REDENVELOPE_REFUND("REDENVELOPE_REFUND"),
	/** 确认预定订单 */
	CONFIRM_APPOINTORDER("CONFIRM_APPOINTORDER"),
	
	;
	
	private String code;
	
	private JobType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
