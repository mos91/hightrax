package org.hightrax.app.config;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
 
@EnableWebMvc
@Configuration
@ComponentScan({ "org.hightrax.app.web" })
public class SpringWebConfig extends WebMvcConfigurerAdapter {

  private final static Logger logger = LoggerFactory.getLogger(SpringWebConfig.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(new WebRequestInterceptor() {
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
		});
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
 
}
