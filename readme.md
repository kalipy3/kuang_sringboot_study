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

### 整合mybatis

1. 选上如下东西:  
    ![Image](./img/image_2021-03-15-23-02-47.png)

2. 点击explore按钮  
   ![Image](./img/image_2021-03-15-23-01-25.png)

3. 编写`application.properties`,整合mybatis
    ```
    spring.datasource.username=root
    spring.datasource.password=Abcd1234
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mybatis?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
    # 整合mybatis
    mybatis.type-aliases-package=com.kuang.pojo # mybatis的包扫描
    mybatis.mapper-locations=classpath:mybatis/mapper/*.xml # mybatis的xml的存放路径 可以在resources文件夹下建个mybatis目录，再在mybatis目录下建个mapper目录，在mapper目录下编写.xml配置文件
    ```

4. 编写pojo

5. 编写接口`xxMapper`,比如:
    ```
    //这个注解表示这是一个mybatis的mapper类: Dao
    @Mapper
    @Reqository
    public interface UserMapper {
        List<User> queryUserList();

        User queryUserById(int id);
    }
    ```

6. 编写mybatis xxMapper接口对应的.xml配置文件

7. 编写控制层
    ```
    @RestController
    public class UserController {
        @AutoWired
        private UserMapper userMapper;

        @GetMapping("queryUserList")
        public List<User> userList = userMapper.queryUserList();
        for (User user : userList) {
            System.out.println(user);
        }
        return userList;
    }
    ```

### SpringSecurity

#### 什么是springsecurity?

Spring Security是一个能够为基于Spring的企业应用系统提供声明式的安全访问控制解决方案的安全框架。它提供了一组可以在Spring应用上下文中配置的Bean，充分利用了Spring IoC，DI（控制反转Inversion of Control ,DI:Dependency Injection 依赖注入）和AOP（面向切面编程）功能，为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作。

#### 功能(认证 授权)

* 功能权限

* 访问权限

* 菜单权限

* 拦截器，过滤器:大量原生代码冗余

Spring Security对Web安全性的支持大量地依赖于Servlet过滤器。这些过滤器拦截进入请求，并且在应用程序处理该请求之前进行某些安全处理。 Spring Security提供有若干个过滤器，它们能够拦截Servlet请求，并将这些请求转给认证和访问决策管理器处理，从而增强安全性。根据自己的需要，可以使用适当的过滤器来保护自己的应用程序。

如果使用过Servlet过滤器且令其正常工作，就必须在Web应用程序的web.xml文件中使用<filter> 和<filter-mapping>元素配置它们。虽然这样做能起作用，但是它并不适用于使用依赖注入进行的配置。

FilterToBeanProxy是一个特殊的Servlet过滤器，它本身做的工作并不多，而是将自己的工作委托给Spring应用程序上下文 中的一个Bean来完成。被委托的Bean几乎和其他的Servlet过滤器一样，实现javax.servlet.Filter接口，但它是在Spring配置文件而不是web.xml文件中配置的。

实际上，FilterToBeanProxy代理给的那个Bean可以是javax.servlet.Filter的任意实现。这可以是 Spring Security的任何一个过滤器，或者它可以是自己创建的一个过滤器。但是正如本书已经提到的那样，Spring Security要求至少配置四个而且可能一打或者更多的过滤器。

#### 简介

只需要引入`spring-boot-starter-security`模块，进行少量的配置，即可实现强大的安全管理

记住几个类:

* WebSecurityConfigurerAdapter: 自定义安全策略

* AuthenticationManagerBuilder: 自定义认证策略

* @EnableWebSecurity: 开启WebSecurity模式

Spring Security的两个主要目标是`认证(Authentication)`和`授权(Authorization)`(访问控制)

### [实战]不同角色登录有不同的访问控制权限

#### 导入依赖

![Image](./img/image_2021-03-22-12-59-54.png)

    plugins {
    	id 'org.springframework.boot' version '2.4.4'
    	id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    	id 'java'
    }
    
    group = 'com.example'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = '1.8'

    repositories {
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven { url 'https://maven.aliyun.com/repository/central'}
        maven { url 'https://maven.aliyun.com/repository/google'}
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin'}
        maven { url 'https://maven.aliyun.com/repository/spring'}
        maven { url 'https://maven.aliyun.com/repository/spring-plugin'}
        maven { url 'https://maven.aliyun.com/repository/apache-snapshots'}
    
        mavenLocal()
        mavenCentral()
    }
    
    dependencies {
    	implementation 'org.springframework.boot:spring-boot-starter-security'
    	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    	implementation 'org.springframework.boot:spring-boot-starter-web'
    	implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity5'
    	testImplementation 'org.springframework.boot:spring-boot-starter-test'
    	testImplementation 'org.springframework.security:spring-security-test'
    }
    
    test {
    	useJUnitPlatform()
    }

#### 项目结构如下:

    kalipy@debian ~/b/j/k/d/demo> tree src/
    src/
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── demo
    │   │               ├── config
    │   │               │   └── SecurityConfig.java
    │   │               ├── controller
    │   │               │   └── RouterController.java
    │   │               └── DemoApplication.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    │           ├── index.html
    │           └── views
    │               ├── level1
    │               │   ├── 1.html
    │               │   ├── 2.html
    │               │   └── 3.html
    │               ├── level2
    │               │   ├── 1.html
    │               │   ├── 2.html
    │               │   └── 3.html
    │               ├── level3
    │               │   ├── 1.html
    │               │   ├── 2.html
    │               │   └── 3.html
    │               └── login.html

#### 编写控制层

RouterController.java

    package com.example.demo.controller;
    
    import org.springframework.stereotype.Controller;
    
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    
    /*
     * RouterController.java
     * Copyright (C) 2021 2021-03-22 13:38 kalipy <kalipy@debian>
     *
     * Distributed under terms of the MIT license.
     */
    @Controller
    public class RouterController
    {
        @RequestMapping({"/", "/index"})
        public String index() {
            return "index";
        }
        
        @RequestMapping("/toLogin")
        public String toLogin() {
            return "views/login";
        }
        
        @RequestMapping("/level1/{id}")
        public String level1(@PathVariable("id") int id) {
            return "views/level1/" + id;
        }
        @RequestMapping("/level2/{id}")
        public String level2(@PathVariable("id") int id) {
            return "views/level2/" + id;
        }
        @RequestMapping("/level3/{id}")
        public String level3(@PathVariable("id") int id) {
            return "views/level3/" + id;
        }
    }
    
#### 编写配置类

SecurityConfig.java

    package com.example.demo.config;
    
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
    /*
     * SecurityConfig.java
     * Copyright (C) 2021 2021-03-22 13:15 kalipy <kalipy@debian>
     *
     * Distributed under terms of the MIT license.
     */
    
    //AOP拦截器
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter 
    {
        //授权
        @Override
        public void configure(HttpSecurity http) throws Exception {
            //首页所有人可以访问，功能页只有对应权限的人才能访问
            //请求授权的规则
            http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
    
            //没有权限默认到登录页面
            http.formLogin();
        
            //注销，开启了注销功能，跳到首页
            http.logout().logoutSuccessUrl("/");

            //开启记住我功能 cookie，默认保存两周
            http.rememberMe();
        }
    
        //认证, springboot 2.1.x可以直接使用
        //在spring security 5.0+ 需要对密码加密，不然会报错
        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())//这些数据通常应该从数据库里得到，这里为了方便我们直接从内存里面放入假数据
                .withUser("hanser").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2", "vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2", "vip3")
                .and()
                .withUser("yousa").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1");
        }
    }

#### 测试

浏览器访问`http://127.0.0.1:8080/levelx/y`,会自动跳转到springboot自带的login页面，我们输入hanser或root或yousa的账号进行登录后，不同角色有不同的页面访问权限
