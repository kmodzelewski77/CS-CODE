package com.test.kmodzelewski;

import com.test.kmodzelewski.service.EventProcessor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.test.kmodzelewski.data")
@ComponentScan(basePackages = {"com.test.kmodzelewski.globalevent","com.test.kmodzelewski.processors"})
@EnableAsync
@Configuration
public class KmodzelewskiApplication  {

	public static void main(String[] args) {

		if ( args.length !=1  )
		{
			LoggerFactory.getLogger(KmodzelewskiApplication.class ).error("Please provide single parameter");
			System.exit(100);
		}
		ConfigurableApplicationContext context = SpringApplication.run(KmodzelewskiApplication.class, args);
		EventProcessor eventProcessor = context.getBean(EventProcessor.class);
		eventProcessor.processFile( args[0]);
		/*int exit = SpringApplication.exit(context);
		System.exit(exit);*/
	}

	@Bean(name="collectTaskExecutor")
	public Executor getCollectTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(7);
		executor.setMaxPoolSize(128);
		executor.setThreadNamePrefix("CollectTask-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(10);
		executor.setKeepAliveSeconds(10);
		executor.initialize();
		return executor;
	}

}
