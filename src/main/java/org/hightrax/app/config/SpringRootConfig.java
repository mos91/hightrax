package org.hightrax.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan({ "org.hightrax.app.service" })
@ImportResource({
  "classpath:applicationContext.xml"})
public class SpringRootConfig {
}
