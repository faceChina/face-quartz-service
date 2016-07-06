package com.zjlp.face.quartz.util;

import java.util.Date;
import java.util.Random;

import com.zjlp.face.util.date.DateStyle;
import com.zjlp.face.util.date.DateUtil;


public class QuarzKeyUtil {
	
	/**
	 * 获取Job名称
	 * @Title: getJobName 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param name
	 * @param exeDate
	 * @return
	 * @date 2014年8月22日 下午3:57:48  
	 * @author dzq
	 */
	public static String getJobName(String name,Date exeDate){
		StringBuffer jobName = new StringBuffer();
		int t = new Random().nextInt(99999);
		if (t < 10000)t += 10000;
		jobName.append("JOB_").append(name).append("_").append(t);
		if (null != exeDate) {
			String datestr = DateUtil.DateToString(exeDate,DateStyle.YYMMDDHHMMSS);
			jobName.append(datestr);
		}
		jobName.append(t);
		return jobName.toString();
	}
	/**
	 * 获取Triger名称
	 * @Title: getTrigerName 
	 * @Description: (这里用一句话描述这个方法的作用) 
	 * @param date
	 * @return
	 * @date 2014年8月22日 下午3:57:59  
	 * @author dzq
	 */
	public static String getTrigerName(Date date) {
		StringBuffer jobName = new StringBuffer();
		
		int t = new Random().nextInt(99999);
		if (t < 10000)t += 10000;
		if (null != date) {
			String exeDate = DateUtil.DateToString(date,DateStyle.YYMMDDHHMMSS);
			jobName.append(exeDate);
		}
		jobName.append(t);
		return jobName.toString();
	}
	
	public static String getJobDescription(Date exeDate) {
		String datestr = DateUtil.DateToString(exeDate,DateStyle.YYMMDDHHMMSS);
		return datestr;
	}
	
	public static void main(String[] args) {
		System.out.println(getJobName("orderJob",new Date()));
	}
	

	
}
