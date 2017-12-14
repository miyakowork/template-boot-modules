## Template Modules JPA
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.wuwenbin/template-modules-jpa/badge.svg)](http://search.maven.org/#artifactdetails%7Cme.wuwenbin%7Ctemplate-modules-jpa%7C1.10.2.RELEASE%7Cjar)
---
template modules jpa 帮你轻松完成对数据的操作，无需额外编写dao，一个dao满足你所有需求。支持多数据源。
## 当前版本：
   ```xml
       <dependency>
            <groupId>me.wuwenbin</groupId>
            <artifactId>template-modules-jpa</artifactId>
            <version>1.10.2.RELEASE</version>
        </dependency>
   ```
   ---
   ## 更新 - 20171213
   + 删除一些不必要的throws
   + 删除一些不必要的泛型指定
   + 增加查询单列单行，单列多行的方法。只允许为基本类型的包装类和String类型
   ## 更新 - 20171211
   + 取消自定义的函数式接口，改为直接调用JDK8内置的函数式接口。
   + 修改了响应的方法以及类名称，使文意更清晰。具体为```InternalCall.transfer()```方法。表示```内部调用.调用方法```
   ## 更新 - 20171129
   + 从1.9.0版本开始需要JDK8及以上支持。
   + 在DaoFactory中增加用户可自己获取并且注入dao的方法。即不使用注解去动态切换。
   + 修复了在方法内部调用方法的时候，多数据源注解切换无效的BUG。使用方法请使用对应的```Calls.get()```方法。
   + dao增加了2个插入方法，可以传入tableName和pkName，但前提，必须是实体类对应。
   + 新增SQLServer2000以及SQLServer2005+的方法。
   + 新增db2、derby、hsql、informix等支持。
   ## 更新 - 20170214
   + 增加了insertMapAutoGenKeyOutBean方法。
   ---
   ## 入门使用
   1. 配置数据源（此处示例使用DruidDataSource）
       1. xml配置方法（具体属性值配置此处省略，根据开发情况配置）
       
       ```xml
          <bean id="ds1" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close"/>
          <bean id="ds2" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close"/>
       ```
       2. java配置方法（推荐Druid Spring Boot Starter快速配置）
       
       ```java
          @Primary
          @Bean
          @ConfigurationProperties("spring.datasource.druid.one")
          public DataSource dataSourceOne(){
              return DruidDataSourceBuilder.create().build();
          }
       
          @Bean
          @ConfigurationProperties("spring.datasource.druid.two")
          public DataSource dataSourceTwo(){
              return DruidDataSourceBuilder.create().build();
          }
       ```
   2. 配置增加数据源（DataSourceX类）
       1. xml配置方法
       
       ```xml
              <bean id="dsx1" class="me.wuwenbin.modules.jpa.factory.business.DataSourceX">
                  <property name="dataSource" ref="ds1"/>
                  <property name="initDbType" value="Mysql"/><!-- 此处的Mysql为DbType.Mysql,默认Mysql-->
              </bean>
          
              <bean id="dsx2" class="me.wuwenbin.modules.jpa.factory.business.DataSourceX">
                  <property name="dataSource" ref="ds2"/>
                  <property name="initDbType" value="Mysql"/><!-- 此处的Mysql为DbType.Mysql,默认Mysql -->
              </bean>
       ```
       2. java配置方法
       
       ```java
              @Bean
              public DataSourceX dataSourceX(DruidDataSource dataSource) {
                  DataSourceX dataSourceX = new DataSourceX();
                  dataSourceX.setDataSource(dataSource);
                  dataSourceX.setInitDbType(DbType.Mysql);
                  return dataSourceX;
              }
       ```
3. 配置DaoFactory（核心配置）
    1. xml配置
    
    ```xml
            <bean id="daoFactory" class="me.wuwenbin.modules.jpa.factory.DaoFactory">
               <!--多个增强数据源的map集合-->
                <property name="dataSourceMap">
                    <map key-type="java.lang.String">
                        <entry key="tp" value-ref="dsx1"/>
                        <entry key="ct" value-ref="dsx2"/>
                    </map>
                </property>
             <!--默认的增强数据源-->
                <property name="defaultDao" ref="dsx1"/>
            </bean>
    ```
    2. java配置
    
    ```java
           @Bean
           public DaoFactory daoFactory(DataSourceX dataSourceX) {
               DaoFactory daoFactory = new DaoFactory();
               Map<String, DataSourceX> multiDao = new ConcurrentHashMap<>();
               multiDao.put("template_default_dao", dataSourceX);
               daoFactory.setDataSourceMap(multiDao);
               daoFactory.setDefaultDao(dataSourceX);
               return daoFactory;
           }
    ```
4. 开始使用
    1. 使用方法1：在Spring项目的一个Bean中注入DaoFactory即可。
    
    ```java
        @Autowired
         private DaoFactory daoFactory;
     
         public List<Student> findStudentsByCampusNo(String campusNo){
           String sql = "select * from t_stu_info where campus_no = ?";
           return daoFactory.dynamicDao.findListBeanByArray(sql, Student.class, campusNo);
         }
    ```
    2. 使用方法2：在Spring项目的一个Bean中注入AncestorDao即可。
    
    ```java
       private AncestorDao daoTemplate;

       @Autowired
       public void setDaoTemplate(DaoFactory daoFactory){
           this.daoTemplate = daoFactory.dynamicDao; 
       }
    
       public List<Student> findStudentsByCampusNo(String campusNo){
          String sql = "select * from t_stu_info where campus_no = ?";
          return daoTemplate.findListBeanByArray(sql, Student.class, campusNo);
        }
    ```