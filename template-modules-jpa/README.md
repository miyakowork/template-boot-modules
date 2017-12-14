## Template Modules JPA
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.wuwenbin/template-modules-jpa/badge.svg)](http://search.maven.org/#artifactdetails%7Cme.wuwenbin%7Ctemplate-modules-jpa%7C1.10.2.RELEASE%7Cjar)
---
template modules jpa 帮你轻松完成对数据的操作，无需额外编写dao，一个dao满足你所有需求。支持多数据源。
## 当/（）——+           </bean>
       
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