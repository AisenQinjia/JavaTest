package org.example.zhc.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author aisen
 */
@Slf4j
@Component
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, EnvironmentAware {
    private ConfigurableEnvironment environment;
    static String CUSTOM_PROPERTIES = "custom.properties";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("postProcessBeanFactory enter");
        if (environment.getPropertySources().contains(CUSTOM_PROPERTIES)) {
            //already initialized
            return;
        }
        log.info("postProcessBeanFactory init property");
        CompositePropertySource composite = new CompositePropertySource(CUSTOM_PROPERTIES);
        Properties customPp = new Properties();
        customPp.setProperty("key1","val2,ff");
        composite.addPropertySource(new PropertiesPropertySource("ttt", customPp));
        environment.getPropertySources().addFirst(composite);
    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = (ConfigurableEnvironment) environment;
    }
}
