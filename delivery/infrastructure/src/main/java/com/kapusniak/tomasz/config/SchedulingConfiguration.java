package com.kapusniak.tomasz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile({"!test", "!jdbc", "!integration-test"})
@EnableScheduling
@EnableAsync
public class SchedulingConfiguration {

}
