package org.hightrax.app.interceptors;

import org.hightrax.app.config.SpringWebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
@Profile("development")
public class AppWebRequestInterceptor implements LoggingInterceptor {

  private final static Logger logger = LoggerFactory.getLogger(SpringWebConfig.class);

  @Override
  public void preHandle(WebRequest request) throws Exception {
    logger.info("preHandle web request : " + request.getContextPath());
  }

  @Override
  public void postHandle(WebRequest request, ModelMap model) throws Exception {
    logger.info("postHandle web request : " + request.getContextPath());
    logger.info("model :" + model);
  }

  @Override
  public void afterCompletion(WebRequest request, Exception ex) throws Exception {
    logger.info("afterCompletion web request : " + request);
    if (ex != null){
      logger.error("Throwing exception :", ex);
    }
  }
}
