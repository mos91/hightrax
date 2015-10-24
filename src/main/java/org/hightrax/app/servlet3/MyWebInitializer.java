package org.hightrax.app.servlet3;

import ch.qos.logback.access.servlet.TeeFilter;
import ch.qos.logback.classic.ViewStatusMessagesServlet;
import org.hightrax.app.config.LoggedSpringRootConfig;
import org.hightrax.app.config.SpringRootConfig;
import org.hightrax.app.config.SpringWebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.Properties;

public class MyWebInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

  private final static String SPRING_ACTIVE_PROFILES = "spring.profiles.active";

  private final static Logger logger = LoggerFactory.getLogger(MyWebInitializer.class);

  private final Properties properties;

  private final ApplicationMode applicationMode;

  private String beanProfile;

  private final boolean isDevelopment;

  private final String loggingServletUrl;

  public MyWebInitializer() throws IOException {
    super();
    properties = new Properties();
    properties.load(new ClassPathResource("application.properties").getInputStream());
    applicationMode = ApplicationMode.valueOf(
      properties.getProperty(ApplicationProperties.APPLICATION_MODE));
    loggingServletUrl = properties.getProperty(ApplicationProperties.LOGGING_SERVLET_URL);
    isDevelopment = (applicationMode == ApplicationMode.DEVELOPMENT)? true : false;
  }

  private void registerLoggingServlet(ServletContext servletContext){
    final ServletRegistration servletRegistration = servletContext.addServlet("ViewStatusMessages",
      ViewStatusMessagesServlet.class);
    servletRegistration.addMapping(loggingServletUrl);

    logger.info("Register status messages servlet on " + servletContext.getContextPath()
      + loggingServletUrl);
  }

  private void registerTeeFilter(ServletContext servletContext){
    FilterRegistration.Dynamic registration = servletContext.addFilter("TeeFilter", new TeeFilter());
    registration.addMappingForUrlPatterns(null, false, getServletName());
    registration.setAsyncSupported(isAsyncSupported());

    logger.info("Register Logback TeeFilter ");
  }

  private void registerSecurityFilterChain(ServletContext servletContext) {
    FilterRegistration.Dynamic registration =
      servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());
    registration.addMappingForUrlPatterns(null, false, "/*");

    logger.info("Register springSecurityFilterChain");
  }

  private void registerSpringLog4jListener(ServletContext servletContext) {
    servletContext.addListener(new Log4jConfigListener());

    logger.info("Register " + Log4jConfigListener.class);
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    if (isDevelopment){
      registerSpringLog4jListener(servletContext);
    }

    super.onStartup(servletContext);
    logger.info("--------------------------<Application Settings>--------------------------");
    logger.info(ApplicationProperties.APPLICATION_MODE + " : " + applicationMode);

    beanProfile = applicationMode.getProfileName();

    logger.info("Setting active bean profile :'" + beanProfile + "'");

    String activeSpringProfiles = System.getProperty("spring.profiles.active");
    if (activeSpringProfiles != null && !activeSpringProfiles.isEmpty()){
      activeSpringProfiles += "," + beanProfile;
      System.setProperty(SPRING_ACTIVE_PROFILES, activeSpringProfiles);
    } else {
      System.setProperty(SPRING_ACTIVE_PROFILES, beanProfile);
    }

    if (isDevelopment) {
      registerLoggingServlet(servletContext);
      registerTeeFilter(servletContext);
    }

    logger.info("--------------------------</Application Settings>--------------------------");
  }

  @Override
	protected Class<?>[] getRootConfigClasses() {
    Class<?> rootContextClass;
    if (isDevelopment){
      rootContextClass = LoggedSpringRootConfig.class;
    } else {
      rootContextClass = SpringRootConfig.class;
    }

		return new Class[] { rootContextClass };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
