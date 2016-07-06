package com.zjlp.face.domain;

/**
 * 返回响应结果
 * @ClassName: ResultData 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author lys
 * @date 2015年10月28日 上午11:57:53
 */
public class ResultData {

	//响应标志
	private String flag;
	//成功或错误编码
	private String code;
	//信息说明
	private String info;
	
	public ResultData() {
	}
	public ResultData(String flag){
		this.flag = flag;
	}
	public ResultData(String flag, String code, String info) {
		this.flag = flag;
		this.code = code;
		this.info = info;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
