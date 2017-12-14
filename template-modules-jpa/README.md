## Druid Spring Boot Starter
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.wuwenbin/template-modules-jpa/badge.svg)](http://search.maven.org/#artifactdetails%7Cme.wuwenbin%7Ctemplate-modules-jpa%7C1.10.2.RELEASE%7Cjar)
---
template modules jpa 帮你轻松
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
1. 配置数据源
    1. 
