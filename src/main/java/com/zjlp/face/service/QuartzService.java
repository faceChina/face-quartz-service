package com.zjlp.face.service;

import com.zjlp.face.exception.QuartzException;

/**
 * 开启任务服务业务接口
 * @ClassName: QuartzService 
 * @Description: (这里用一句话描述这个类的作用) 
 * @author lys
 * @date 2015年10月28日 上午11:52:32
 */
public interface QuartzService {

	/**
	 * 开启任务持久化任务
	 * @Title: beginWithdrawJob 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param requestJson 任务参数
	 * @return
	 * @throws QuartzException
	 * @date 2015年10月29日 下午2:01:52  
	 * @author lys
	 */
	String beginJob(String requestJson) throws QuartzException;
	
}
