package com.frade.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class ApiScheduler {

	@Scheduled(cron = "0 40 8 * * *")
	public void preMarketTask() {
		System.out.println("test");
	}

}
