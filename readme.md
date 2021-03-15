```
readme.md

:Author: kalipy
:Email: kalipy@debian
:Date: 2021-03-15 15:07
```

### springboot源码阅读

1. 方法一(失败，因为springboot的依赖有很多父子依赖，这样下载的并不是真正的源码):  
    通过gradle的build.gradle中添加:`testImplementation 'org.springframework.boot:spring-boot-starter-web:2.4.3:sources'`


2. 方法二(通过github下载,ok):  
    下载地址: `https://github.com/spring-projects/spring-boot/tree/2.4.x`

### p14-15静态资源探究

#### 根据源码理解静态资源目录

1. `vim (fzf)`找到`WebMvcAutoConfiguration.java`并打开,找到如下方法:  
    ```
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    	super.addResourceHandlers(registry);
    	if (!this.resourceProperties.isAddMappings()) {
    		logger.debug("Default resource handling disabled");
    		return;
    	}
    	ServletContext servletContext = getServletContext();
    	addResourceHandler(registry, "/webjars/**", "classpath:/META-INF/resources/webjars/");
    	addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
    		registration.addResourceLocations(this.resourceProperties.getStaticLocations());
    		if (servletContext != null) {
    			registration.addResourceLocations(new ServletContextResource(servletContext, SERVLET_LOCATION));
    		}
    	});
    }
    ```

#### 总结：

1. 在springboot中，我们可以用一下方式处理静态资源  
    ** webjars `localhost:8080/webjars/`  
    ** public,static,/**,resources `localhost:8080/`  

2. 优先级: resouces > static(默认) > public  

3. 在templates目录下的所有页面，只能通过controller来跳转  
