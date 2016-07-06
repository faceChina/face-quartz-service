package com.zjlp.face.quartz.bean;

/**
 * 
 * @ClassName: SimpleJobBean
 * @Description: (这里用一句话描述这个类的作用)
 * @author dzq
 * @date 2014年8月22日 下午3:45:04
 */
public class SimpleJobBean extends JobBean {
	/** 任务间隔时间 */
	private Integer intervalInSeconds;
	/** 是否循环执行 */
	private boolean isForever;
	/** 运行次数 */
	private Integer repeatCount;

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Integer getIntervalInSeconds() {
		return intervalInSeconds;
	}

	public void setIntervalInSeconds(Integer intervalInSeconds) {
		this.intervalInSeconds = intervalInSeconds;
	}

	public boolean isForever() {
		return isForever;
	}

	public void setForever(boolean isForever) {
		this.isForever = isForever;
	}
}
