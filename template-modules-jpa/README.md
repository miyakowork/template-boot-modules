
## 文档
- [入门指南](https://github.com/miyakowork/template-modules-dao/wiki/入门)
- [主要方法](https://github.com/miyakowork/template-modules-dao/wiki/主要方法API)

---
##  1.2.0更新
+ 增加了insertMapAutoGenKeyOutBean方法。
## 更新 - 20171129
+ 从1.9.0版本开始需要JDK8及以上支持。
+ 在DaoFactory中增加用户可自己获取并且注入dao的方法。即不使用注解去动态切换。
+ 修复了在方法内部调用方法的时候，多数据源注解切换无效的BUG。使用方法请使用对应的```Calls.get()```方法。
+ dao增加了2个插入方法，可以传入tableName和pkName，但前提，必须是实体类对应。
+ 新增SQLServer2000以及SQLServer2005+的方法。
+ 新增db2、derby、hsql、informix等支持。