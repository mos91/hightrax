package org.hightrax.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan({ "org.hightrax.app.service" })
@ImportResource({ "classpath:applicationContext.xml"})
public class LoggedSpringRootConfig extends SpringRootConfig implements BeanPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(LoggedSpringRootConfig.class);

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
    throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName)
    throws BeansException {

      String beanClassName = bean.getClass().getName();

      if (beanClassName.equals(beanName)){
        logger.info("Register bean of " + bean.getClass());
      } else if (beanName.contains(beanClassName + "#")){
        String proxyBeanName = beanName.substring(beanName.indexOf("#"), beanName.length());
        logger.info("Register proxy " + proxyBeanName + " of " + bean.getClass());
      } else {
        logger.info("Register bean " + beanName + " of " + bean.getClass());
      }

    return bean;
  }
}
