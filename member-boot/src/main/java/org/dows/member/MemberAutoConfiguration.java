package org.dows.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.dows.member.mapper")
@ComponentScan(basePackages = {"org.dows.member.mapper", "org.dows.member.service"})
public class MemberAutoConfiguration {
}

