package com.application.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Profile("windows")
@PropertySource("classpath:windows.properties")
@Component
@Configurable
public class WindowsConfiguration extends OSConfiguration {
}
