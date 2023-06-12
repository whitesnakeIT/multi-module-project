package com.kapusniak.tomasz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"!test", "!jdbc", "!integration-test"})
//@EnableScheduling
//@EnableAsync
public class SchedulingConfiguration {

}
