package com.zjlp.face.web.job;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.zjlp.face.web.server.trade.order.bussiness.SalesOrderBusiness;

@Component
public class PersistenceJobServiceLocator implements ApplicationContextAware{
	
    private static ApplicationContext applicationContext;

    @SuppressWarnings("static-access")
	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public static <T> T getJobService(Class<T> clazz) {
    	return applicationContext.getBean(clazz);
    }
    
    public static <T> T getJobService(String beanId, Class<T> clazz) {
    	return applicationContext.getBean(beanId, clazz);
    }
	
	/** 接口有多个实现类时,必须写入BeanId */
	public static SalesOrderBusiness getSalesOrderBusiness(){
		return applicationContext.getBean("salesOrderBusiness", SalesOrderBusiness.class);
	}

}