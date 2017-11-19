# 「Repository」 - An fast and convenient api for data in 「template」
+ 以下所有的实体类都需要有相关注解支持（详细请参考文档）。
+ 本模块中的所有sql语句（除使用@XxxxSQL注解指定sql的之外）参数形式全部为 「param = :paramKey」形式，即冒号形式。
+ 「Repository」模块可以使应用程序访问数据库变得异常简单。简单一两步操作即可。
---
## insert语句/save方法
### 命名规则
+ 方法名必须以「save」打头。
+ 方法的返回值必须为「T」或者为「List\<T\>」。
+ 方法的参数类型可以有两种情况。其一：有且只能有一种类型，个数不限；其二：可以有多种类、多个参数，要求与sql语句的参数名相同。
+ 方法的参数种类必须为「Map<String,Object>」、「T」、「Collection<T>（或其子接口）」、「T\[](也可为不定参数形式T...)」、「Map<String,Object>\[](同T[],也可为不定参数形式Map<String,Object>...)」其中之一的类型。
+ 插入所有字段已经预定在「IBaseCrudRepository」接口中，请直接使用你的「Repository」接口继承此接口使用「save」方法即可。
+ 需要插入非全部字段请使用「@SaveSQL」指定 sql 或「@Routers」指定插入的字段，参数和返回类型请参考第2、3、4准则。
+ 如果参数为多种类型（即使用@SaveSQL指定sql，并且参数为多个，依照sql语句的参数来），那么方法的返回值只能为int（返回插入的个数，不能返回一个对象）。
+ 其他以上未满足条件的，请使用原生「DaoFactory」的「AncestorDao」接口执行sql。
### 支持方法简例
+ 请查看测试项目代码：[「码云／GitOSC」](https://gitee.com/wuwenbn/RepositoryTester/blob/master/src/main/java/me/wuwenbin/modules/repodata/repository/UserRepository.java)
---
## delete语句/delete方法命名规则
1. 方法名必须以「delete」打头。
2. 所有「delete」方法返回值必须为「void」。
3. 方法的参数有且只能有一种类型。类型必须为「Map」、「T」、「Collection」、「数组」其中之一。
4. 根据主键删除/批量删除记录的方法已经预定义在「IBaseCrudDataRepo」接口中，请直接使用。
5. 如果不是依据主键删除，请使用「deleteBy...」或「deleteBy$Router...」方法。
6. 如果为「deleteBy...」字段来命名的，多个请使用「And」或「Or」其中之一来作为连接。eg：「deleteByRoleAndUserId」。
7. 如果为「deleteBy$Router...」方法，多个router的为「And」并列，无「Or」的用法，如需则参考第8条。
8. 如果需要自定义删除语句请使用「delete$BySql...」，并且在此方法上使用@DeleteSQL指定相关sql语句。eg：「delete$BySqlXXX」(xxx一般为唯一标识)。
9. 自定义方法中的「And」「Or」只能出现一种，不能同时出现。
---
## select语句/find方法命名规则
1. 除「count...」和「exists」计数方法外，其余的查询方法全部以「find」打头。
2. 非统计全部数据，请使用「countBy$RouterXXX」或「count$BySqlXXX」。
3. 「countBy$RouterAndXXX」多个条件为“and”，「countBy$RouterOrXXX」多个条件为“or”。
4. 「count$BySqlXXX」需要在方法上指定「@CountSQL」注解来标明相关sql。
5. 「count...」方法的参数类型只能为「Map」或「T」，方法返回值只能为「long」。
6. 也可使用内置的「count」方法，通过自定义「SelectQuery」来做查询统计。
7. 「exists」方法参数类型仅能有「PK」和「SelectQuery」类型。
8. 所有「findOne」和「findAll」方法均为预定义的（使用SelectQuery查询或Pk主键字段查询），不可自定义方法查询。
9. 另外「findBy...」方法支持大部分的类似「spring-data-jpa」的关键字，具体支持请看「Constraint」。详情见[「spring-data-jpa官方文档」](https://docs.spring.io/spring-data/jpa/docs/2.0.1.RELEASE/reference/html/#jpa.query-methods.query-creation)
10. 「findBy...」方法还支持「findBy$Router...」查询，所支持的条件为“And”。
11. 自定义的「findBy」方法根据方法的返回值来映射查询结果。
12. 支持自定义sql，请使用「find$BySql...」方法，参数为「Map」或「T」，返回值为「T」或者「List<T>」。
13. 如果自定义方法名中含有「Between」则参数中必须有 「prefixParam」和「suffixParam」两个参数，即前缀为「prefix」，后缀为「suffix」。
---
## update语句/update方法命名规则
1. 所有方法均为「update...」开头。
2. 非指定的sql的「update」方法包含三种，其中两种为开发者自定义，分别为「updateRouterABC」（依据主键字段来）和「updateRouterADByRouterC」。
3. 返回值有「T」、「boolean」、「void」三种，其中「T」为返回更新后的实体，失败则返回null。
4. 方法参数只能为「Map」或「T」。
5. 额外的一种方法为指定sql的update方法「updateBySqlXXX」可自定义更新的sql语句，参数类型和返回值参考以上的几条规则。
---
##分页方法
1. 包含两个方法，定义在「IPageAndSortDataRepo」中。
