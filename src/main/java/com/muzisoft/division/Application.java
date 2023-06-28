package com.muzisoft.division;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

@EnableJpaAuditing(dateTimeProviderRef = "localDateTimeProvider")
@SpringBootApplication
@RequiredArgsConstructor
public class Application implements ApplicationListener {

	private final ApplicationContext applicationContext;

	public int port;
	public String ip;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		try {
			this.ip = InetAddress.getLocalHost().getHostAddress();
			this.port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public String hostAddress() {
		return "http://" + ip + ":" + port;
	}

}
