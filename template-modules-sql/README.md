## Template Modules SQL
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.wuwenbin/template-modules-sql/badge.svg)](http://search.maven.org/#artifactdetails%7Cme.wuwenbin%7Ctemplate-modules-sql%7C1.10.5.RELEASE%7Cjar)
---
template modules sql 帮你轻松完成对数据的操作，无需额外编写dao，一个dao满足你所有需求。支持多数据源（多数据源也只需定义一个dao即可）。
## 当前版本：
   ```xml
       <dependency>
            <groupId>me.wuwenbin</groupId>
            <artifactId>template-modules-sql</artifactId>
            <version>1.10.15-SNAPSHOT</version>
        </dependency>
   ```
   ---
   ## 新增 - 20171228
   + 增加一个获取指定routers的Field的方法
   + 增加sql语句占位符参数的选择（冒号/问号）
   ## 更新 - 20171217
   + 现在可以把Router.Default 和没有@SQLColumn注解的属性字段当做同一类了，即都可以选中，而不再是仅选择Router.Default字段
   ## 更新 - 20171116
  + 增加了判断是否需要囊括父类的字段注解标识@MappedSuper
  ## 更新 - 20171006
  + 增加了一个@GenralType注解判断主键的自增类型  
  ## 更新 - 20170831
  + 增加了Router的常量默认定义个数从A~Z
