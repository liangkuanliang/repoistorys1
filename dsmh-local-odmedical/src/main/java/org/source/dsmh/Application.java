package org.source.dsmh;

import java.util.concurrent.ThreadPoolExecutor;

import org.source.dsmh.service.RecordLogToUnifiedDb;
import org.source.dsmh.service.impl.RecordLogToUnifiedDbImp;
import org.source.dsmh.utils.config.ThreadPoolConfig;
import org.source.dsmh.utils.mongodb.SpringContextUtil;
import org.source.dsmh.utils.redis.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableDubboConfiguration
@ComponentScan(basePackages={"org.source.dsmh"})
@MapperScan(basePackages= {"org.source.dsmh.dao"})
public class Application {

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	/**
	 * 异步线程池
	 * 
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(ThreadPoolConfig.getCorePoolSize()); // 线程池维护线程的最少数量
		taskExecutor.setMaxPoolSize(ThreadPoolConfig.getMaxPoolSize()); // 线程池维护线程的最大数量
		taskExecutor.setQueueCapacity(ThreadPoolConfig.getQueueCapacity()); // 缓存队列
		taskExecutor.setKeepAliveSeconds(ThreadPoolConfig.getKeepAliveSeconds()); // 允许的空闲时间
		// 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略
		// ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return taskExecutor;
	}
	
	@Bean
	public SpringContextUtil getSpringContextUtil() {
		return new SpringContextUtil();
	}
	
	@Bean
	public RecordLogToUnifiedDb getRecordLogToUnifiedDb() {
		return new RecordLogToUnifiedDbImp();
	}
	
	@Bean
	public RedisOperator getRedisOperator() {
		return new RedisOperator();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}