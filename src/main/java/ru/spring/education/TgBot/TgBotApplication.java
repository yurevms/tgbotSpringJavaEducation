package ru.spring.education.TgBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
@EnableTransactionManagement
@SpringBootApplication
public class TgBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgBotApplication.class, args);
	}

}
