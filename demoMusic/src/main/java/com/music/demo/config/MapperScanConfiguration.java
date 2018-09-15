package com.music.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

//将mapper包下的所有interface类映射
@MapperScan("com.music.demo.mapper")
@Configuration
public class MapperScanConfiguration {
}
