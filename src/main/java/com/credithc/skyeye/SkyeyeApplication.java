package com.credithc.skyeye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目入口.
 *
 * @author feifei.liu
 * @version 1.0
 * @date 2017/6/8
 */
@EnableScheduling
@SpringBootApplication
public class SkyeyeApplication extends SpringBootServletInitializer {
	private final Logger logger = LoggerFactory.getLogger(SkyeyeApplication.class);

	public static void main(String[] args) {
//		SpringApplication.run(SkyeyeApplication.class, args);
		SpringApplication sa = new SpringApplication(SkyeyeApplication.class);
//		sa.setBannerMode(Banner.Mode.OFF);
		sa.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SkyeyeApplication.class);
	}


}
