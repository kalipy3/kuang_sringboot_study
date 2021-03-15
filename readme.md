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
    webjars `localhost:8080/webjars/`  
    public,static,/**,resources `localhost:8080/`  

2. 优先级: resouces > static(默认) > public  

3. 在templates目录下的所有页面，只能通过controller来跳转  

### thymeleaf

1. 导入依赖:`implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'`(通过`https://start.spring.io/`选上spring web和thymeleaf,然后点击expose按钮查看spirng官网自动生成的build.gradle文件中的依赖,把依赖复制到我们的这个项目里)

2. thymeleaf的templates目录解析原理,请查看源码`ThymeleafProperties.java`:
    ```
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_PREFIX = ".html";
    ```

3. 导入依赖后，我们将html放在templates目录下即可使用thymeleaf模板引擎的功能了  

4. 在`.html`文件中使用thymeleaf的时候请注意在`.html`中导入约束`<html xmlns:th=http://www.thymeleaf.org>`  


### mvc配置原理(请看雷神的gradle_xx_springboot_leifenyan)

### 扩展springMvc

#### 自定义视图解析器进行路由跳转
1. src/main/java/com/ly/config/MyMvcConfig.java
    ```
    //如果我们要扩展springmvc，官方建议我们这样做!
    @Configuration
    public class MyMvcConfig implements WebMvcConfigurer {
        //视图跳转
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("kuang").setViewName("test");
        }
    }
    ```

2. 在springboot中，有非常多的xxxConfiguration帮助我们进行扩展配置，只要看到这些东西，我们就要注意一下  

#### 自定义拦截器进行路由拦截
1. src/main/java/com/ly/config/LoginHandlerInterceptor.java
    ```
    public class LoginHandlerInterceptor implements HandlerInterceptor() {
        @Override
        public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
            //登录成功之后，应该有用户的session
            Object loginUser = req.getSession().getAttribute("loginUser");

            if (loginUser==null) {
                req.setAttribute("msg", "没有权限，请先登录");
                req.getRequestDispatcher("/index.html").forward(req, resp);
                return false;
            } else {
                return true;
            }
        }
    }
    ```

2. src/main/java/com/ly/config/MyMvcConfig.java
    ```
    @Configuration
    public class MyMvcConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new LoginHandlerInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/index.html", "/", "/user/login", "/css/*", "/js/**", "/img/**");
        }
    }
    ```

### springboot整合jdbc
1. 选上jdbc和mysql驱动
    ![Image](./img/1.png) 

2. 点击explore按钮
   ![Image](./img/2.png)

3. 把jdbc和mysql驱动依赖复制到我们的项目里
    ```
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
    runtimeOnly 'mysql:mysql-connector-java'
    ```

4. 编写`application.yml`
    ```
    spring:
        datasource:
            username: root
            password: Abcd1234
            url: jdbc:mysql://127.0.0.1:3306/mybatis?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
            driver-class-name: com.mysql.cj.jdbc.Driver
    ```

5. sql单元测试(注意: 1.javacomplete2不能代码提示自动导入import DataSource,这个换个jdk版本就可以了 2.Connection这个包javacomplete2无论如何都不能自动导入，这时只能百度关键字`java import Connection`,然后找到Connection是位于java.sql.包下，然后手动导入import java.sql.Connection)
    ```
    package com.example.demo;
    
    import javax.sql.DataSource;
    import java.sql.Connection;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    
    @SpringBootTest
    class DemoApplicationTests {
        @Autowired
        DataSource dataSource;
    
        @Test
        void contextLoads() throws Exception {
            //打印一下默认的数据源
            System.out.println(dataSource.getClass());
            //获取数据库连接
            Connection conn = dataSource.getConnection();
            System.out.println(conn);
    
            conn.close();
        }
    }
    ```

6. 编写控制层代码
    ```
    package com.example.demo.controller;
    
    import java.util.List;
    import java.util.Map;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RestController;
    
    /*
     * JDBCController.java
     * Copyright (C) 2021 2021-03-15 18:25 kalipy <kalipy@debian>
     *
     * Distributed under terms of the MIT license.
     */
    
    @RestController
    public class JDBCController
    {
       @Autowired
       JdbcTemplate jdbcTemplate;
    
       //查询数据库所有信息
       //没有实体类，数据库中的东西，怎么获取? Map
       @GetMapping("/userList")
       public List<Map<String, Object>> userList() {
            String sql = "select * from user";
            List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
            return list_maps;
       }
    
       @GetMapping("/addUser")
       public String addUser() {
           String sql = "insert into mybatis.user(id, name, pwd) values (4, '小明', '123456')";
           jdbcTemplate.update(sql);
           return "update-ok";
       }
       
       @GetMapping("/updateUser/{id}")
       public String updateUser(@PathVariable("id") int id) {
           String sql = "update mybatis.user set name=?,pwd=? where id=" + id;
           //封装
           Object[] objects = new Object[2];
           objects[0] = "小明2";
           objects[1] = "123456";
           jdbcTemplate.update(sql, objects);
           return "updateUser-ok";
       }
       
       @GetMapping("/deleteUser/{id}")
       public String deleteUser(@PathVariable("id") int id) {
           String sql = "delete from mybatis.user where id = ?";
           jdbcTemplate.update(sql, id);
           return "deleteUser-ok";
       }
    }
    
    ```

7. 测试`gradle bootRun`,访问`127.0.0.1/userList`

### 整合Druid数据源

1. 只需导入`druid`依赖和在`application.yml`中添加一行`type: com.alibaba.druid.pool.DruidDataSource`即可
    ```
    kalipy@debian ~/b/j/k/demo> git diff 
    diff --git a/demo/build.gradle b/demo/build.gradle
    index 702628b..268e594 100644
    --- a/demo/build.gradle
    +++ b/demo/build.gradle
    @@ -29,6 +29,7 @@ dependencies {
         testImplementation 'org.springframework.boot:spring-boot-starter-test'
         
         implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    +    implementation 'com.alibaba:druid:1.1.21'
     }
     
     test {
    diff --git a/demo/src/main/resources/application.yml b/demo/src/main/resources/application.yml
    index 25f0529..3472c90 100644
    --- a/demo/src/main/resources/application.yml
    +++ b/demo/src/main/resources/application.yml
    @@ -4,3 +4,4 @@ spring:
             password: Abcd1234
             url: jdbc:mysql://127.0.0.1:3306/mybatis?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
             driver-class-name: com.mysql.cj.jdbc.Driver
    +        type: com.alibaba.druid.pool.DruidDataSource
    ```
