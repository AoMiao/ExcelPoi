package com.excel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication//不要检测数据库连接
public class ExcelPoiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelPoiApplication.class, args);
	}
}
 