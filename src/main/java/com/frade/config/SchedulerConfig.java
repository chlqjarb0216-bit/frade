package com.frade.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 💡 클래스 상단에 Slf4j 어노테이션 추가
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(5);
		taskScheduler.setThreadNamePrefix("scheduled-task-");

		// 스케줄러 에러 핸들러 등록
		taskScheduler.setErrorHandler(new CustomSchedulerErrorHandler());
		taskScheduler.initialize();

		taskRegistrar.setTaskScheduler(taskScheduler);
	}

	// 💡 내부 static 클래스에도 로그를 남기기 위해 따로 @Slf4j를 붙여줍니다.
	@Slf4j
	private static class CustomSchedulerErrorHandler implements ErrorHandler {
		@Override
		public void handleError(Throwable t) {
			// 💡 에러 메시지와 함께 스택 트레이스(t)를 통째로 넘겨 로그를 상세히 기록합니다.
			log.error("[스케줄러 에러 알림] 발생 원인: {}", t.getMessage(), t);

			// 필요 시 여기에 슬랙 알림 발송이나 메일 전송 로직 추가 가능
		}
	}
}
