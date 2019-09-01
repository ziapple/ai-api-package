package com.casic.atp.graph.config;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Field;
import java.util.Map;

@WebListener
public class ContextListener implements ServletContextListener
{
    private ContextLoader contextLoader;

    /**
     * Embedded Tocmat启动的时候不会启动ContextLoader.initWebApplicationContext,初始化加进去
     * 生产环境去掉
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.contextLoader = createContextLoader();
        WebApplicationContext  applicationContext = (WebApplicationContext)servletContextEvent.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        ReflectionUtils.doWithFields(ContextLoader.class, new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException{
                if(field.getName().equals("currentContextPerThread")){
                    ReflectionUtils.makeAccessible(field);
                    Map<ClassLoader, WebApplicationContext> currentContextPerThread = (Map<ClassLoader, WebApplicationContext>)ReflectionUtils.getField(field, contextLoader);
                    currentContextPerThread.put(ccl, applicationContext);
                }
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //do something while contextDestroyed
    }

    protected ContextLoader createContextLoader() {
        return new ContextLoader();
    }
}