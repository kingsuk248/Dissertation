package com.application.configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("ubuntu")
@PropertySource("classpath:ubuntu.properties")
public class UbuntuConfiguration extends OSConfiguration {
}
