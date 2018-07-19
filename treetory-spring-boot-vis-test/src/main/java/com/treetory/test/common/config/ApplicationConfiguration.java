package com.treetory.test.common.config;

import javax.servlet.Filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@ComponentScan(basePackages = {"com.treetory.test.mvc", "com.treetory.test.common.util"}, 
useDefaultFilters = false,
includeFilters = {
            @ComponentScan.Filter(value = Controller.class),
            @ComponentScan.Filter(value = Service.class),
            @ComponentScan.Filter(value = Repository.class),
            @ComponentScan.Filter(value = Component.class)
            }
)
@Configuration
@EnableAsync
@EnableWebMvc
@EnableScheduling
@Import(value={BaseDatabaseConfig.class, BaseMyBatisConfig.class})
public class ApplicationConfiguration implements InitializingBean, ApplicationListener<ApplicationEvent>, WebMvcConfigurer {

    @Autowired
    private ApplicationContext appContext;
    
    @Bean(name = "templateResolver")
    @Description("Thymeleaf template resolver serving HTML")
    public ITemplateResolver templateResolver() {
    	// SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
    	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
    @Bean(name = "templateEngine")
    @Description("Thymeleaf template engine with Spring integration")
    public ISpringTemplateEngine templateEngine() {
    	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    	templateEngine.setTemplateResolver((ITemplateResolver)appContext.getBean("templateResolver"));
    	templateEngine.addDialect(new LayoutDialect());
    	return templateEngine;
    }
    
    @Bean(name = "thymeleafViewResolver")
    @Description("Thymeleaf view resolver")
    public ViewResolver thymeleafViewResolver() {
    	ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    	viewResolver.setCharacterEncoding("UTF-8");
    	viewResolver.setTemplateEngine((ISpringTemplateEngine)appContext.getBean("templateEngine"));
    	return viewResolver;
    }
    
    /**
     * REST 요청 시, 한글로 된 body 를 받을 때 한글 깨짐 방지
     */
    @Bean
    @Description("Prevent the broken euckr character set.")
    public Filter charactertEncodingFilter() {
    	CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    	characterEncodingFilter.setEncoding("UTF-8");
    	characterEncodingFilter.setForceEncoding(true);
    	return characterEncodingFilter;
    }
    
    @Override
    @Description("Every URI for requesting view is registerd in here.")
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/dashboard").setViewName("dashboard");
    	
    	// The examples of vis.js
    	registry.addViewController("/").setViewName("index");
    	registry.addViewController("/timeline/basicUsage").setViewName("/timeline/basicUsage");
    	registry.addViewController("/timeline/dataSerialization").setViewName("/timeline/dataHandling/dataSerialization");
    	registry.addViewController("/timeline/loadExternalData").setViewName("/timeline/dataHandling/loadExternalData");
    	registry.addViewController("/timeline/customSnappingOfItems").setViewName("/timeline/editing/customSnappingOfItems");
    	registry.addViewController("/timeline/editingItems").setViewName("/timeline/editing/editingItems");
    	registry.addViewController("/timeline/editingItemsCallbacks").setViewName("/timeline/editing/editingItemsCallbacks");
    	registry.addViewController("/timeline/individualEditableItems").setViewName("/timeline/editing/individualEditableItems");
    	registry.addViewController("/timeline/itemsAlwaysDraggable").setViewName("/timeline/editing/itemsAlwaysDraggable");
    	registry.addViewController("/timeline/overrideEditingItems").setViewName("/timeline/editing/overrideEditingItems");
    	registry.addViewController("/timeline/tooltipOnItemChange").setViewName("/timeline/editing/tooltipOnItemChange");
    	registry.addViewController("/timeline/updateDataOnEvent").setViewName("/timeline/editing/updateDataOnEvent");
    	registry.addViewController("/timeline/groups").setViewName("/timeline/groups/groups");
    	registry.addViewController("/timeline/groupsEditable").setViewName("/timeline/groups/groupsEditable");
    	registry.addViewController("/timeline/groupsOrdering").setViewName("/timeline/groups/groupsOrdering");
    	registry.addViewController("/timeline/nestedGroups").setViewName("/timeline/groups/nestedGroups");
    	registry.addViewController("/timeline/subgroups").setViewName("/timeline/groups/subgroups");
    	registry.addViewController("/timeline/verticalItemsHide").setViewName("/timeline/groups/verticalItemsHide");
    	registry.addViewController("/timeline/animateWindow").setViewName("/timeline/interaction/animateWindow");
    	registry.addViewController("/timeline/clickToUse").setViewName("/timeline/interaction/clickToUse");
    	registry.addViewController("/timeline/eventListeners").setViewName("/timeline/interaction/eventListeners");
    	registry.addViewController("/timeline/limitMoveAndZoom").setViewName("/timeline/interaction/limitMoveAndZoom");
    	registry.addViewController("/timeline/navigationMenu").setViewName("/timeline/interaction/navigationMenu");
    	registry.addViewController("/timeline/rollingMode").setViewName("/timeline/interaction/rollingMode");
    	registry.addViewController("/timeline/setSelection").setViewName("/timeline/interaction/setSelection");
    	registry.addViewController("/timeline/backgroundAreas").setViewName("/timeline/items/backgroundAreas");
    	registry.addViewController("/timeline/backgroundAreasWithGroups").setViewName("/timeline/items/backgroundAreasWithGroups");
    	registry.addViewController("/timeline/expectedVsActualTimesItems").setViewName("/timeline/items/expectedVsActualTimesItems");
    	registry.addViewController("/timeline/htmlContents").setViewName("/timeline/items/htmlContents");
    	registry.addViewController("/timeline/itemOrdering").setViewName("/timeline/items/itemOrdering");
    	registry.addViewController("/timeline/pointItems").setViewName("/timeline/items/pointItems");
    	registry.addViewController("/timeline/rangeOverflowItem").setViewName("/timeline/items/rangeOverflowItem");
    	registry.addViewController("/timeline/tooltip").setViewName("/timeline/items/tooltip");
    	registry.addViewController("/timeline/visibleFrameTemplateContent").setViewName("/timeline/items/visibleFrameTemplateContent");
    	registry.addViewController("/timeline/customTimeBars").setViewName("/timeline/other/customTimeBars");
    	registry.addViewController("/timeline/customTimeBarsTooltip").setViewName("/timeline/other/customTimeBarsTooltip");
    	registry.addViewController("/timeline/dataAttributes").setViewName("/timeline/other/dataAttributes");
    	registry.addViewController("/timeline/dataAttributesAll").setViewName("/timeline/other/dataAttributesAll");
    	registry.addViewController("/timeline/drag&drop").setViewName("/timeline/other/drag&drop");
    	registry.addViewController("/timeline/functionLabelFormats").setViewName("/timeline/other/other/functionLabelFormats");
    	registry.addViewController("/timeline/groupsPerformance").setViewName("/timeline/other/groupsPerformance");
    	registry.addViewController("/timeline/hidingPeriods").setViewName("/timeline/other/hidingPeriods");
    	registry.addViewController("/timeline/horizontalScroll").setViewName("/timeline/other/horizontalScroll");
    	registry.addViewController("/timeline/localization").setViewName("/timeline/other/localization");
    	registry.addViewController("/timeline/performance").setViewName("/timeline/other/performance");
    	registry.addViewController("/timeline/rtl").setViewName("/timeline/other/rtl");
    	registry.addViewController("/timeline/stressPerformance").setViewName("/timeline/other/stressPerformance");
    	registry.addViewController("/timeline/timezone").setViewName("/timeline/other/timezone");
    	registry.addViewController("/timeline/usingReact").setViewName("/timeline/other/usingReact");
    	registry.addViewController("/timeline/verticalScroll").setViewName("/timeline/other/verticalScroll");
    	registry.addViewController("/timeline/axisOrientation").setViewName("/timeline/axisOrientation");
    	registry.addViewController("/timeline/customCss").setViewName("/timeline/customCss");
    	registry.addViewController("/timeline/gridStyling").setViewName("/timeline/gridStyling");
    	registry.addViewController("/timeline/itemClassNames").setViewName("/timeline/itemClassNames");
    	registry.addViewController("/timeline/itemTemplates").setViewName("/timeline/itemTemplates");
    	registry.addViewController("/timeline/weekStyling").setViewName("/timeline/weekStyling");
    }
    
    @Override
    @Description("Every resources for requesting from view is registerd in here.")
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/resources/**", "/css/**", "/images/**", "/js/**", "/lib/**", "/fonts/**")
    	.addResourceLocations(
    			"classpath:/resources/",
    			"classpath:/static/css/",
    			"classpath:/static/images/",
    			"classpath:/static/js/",
    			"classpath:/static/lib/",
    			"classpath:/static/fonts/"
    			)
    	.setCachePeriod(600).resourceChain(true).addResolver(new PathResourceResolver());
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
           	
    	switch (event.getClass().getSimpleName()) {
    	
    	case "ContextRefreshedEvent" :
    		break;
    	case "ServletWebServerInitializedEvent" :
    		break;
    	case "ApplicationStartedEvent" :
    		break;
    	case "ApplicationReadyEvent":
    		break;
    	case "ContextClosedEvent" :
    		break;
    	}
    	
    }
    
}
