package com.zjlp.face.factory;

import net.sf.json.JSONObject;

import org.springframework.util.Assert;

import com.zjlp.face.domain.ResultData;

public class ResultFactory {
	//成功标志
	private static final String FLAG_TRUE = "TRUE";
	//失败标志
	private static final String FLAG_FALSE = "FALSE";
	public enum Code {
		/** 任务持久化成功 */
		SUCC("0000", "任务持久化成功"),
		/** 参数错误 */
		ERR_PARAM("0001", "参数错误"),
		/** 自定义错误 */
		CUSTOM("0002", ""),
		;
		//编码code
		private String code;
		//信息
		private String info;
		
		private Code(String code, String info) {
			this.code = code;
			this.info = info;
		}
	}
	public static ResultData createResult(Code code) {
		Assert.notNull(code);
		ResultData result = Code.SUCC.equals(code) ? new ResultData(FLAG_TRUE)
				: new ResultData(FLAG_FALSE);
		result.setCode(code.code);
		result.setInfo(code.info);
		return result;
	}
	public static String createJsonResult(Code code){
		ResultData result = createResult(code);
		return JSONObject.fromObject(result).toString();
	}
	public static ResultData createErrResult(String info) {
		Assert.notNull(info);
		ResultData result = new ResultData(FLAG_FALSE);
		result.setCode(Code.CUSTOM.code);
		result.setInfo(info);
		return result;
	}
	public static String createJsonErrResult(String info){
		ResultData result = createErrResult(info);
		return JSONObject.fromObject(result).toString();
	}
	
}
