#  An fast and convenient api for data-operation
+ 以下所有的实体类都需要有相关注解支持（详细请参考文档）。
+ 本模块中的所有sql语句（除使用@XxxxSQL注解指定sql的之外）参数形式全部为 「filedValue = :paramKey」形式，即冒号形式。
+ 「Repository」模块可以使应用程序访问数据库变得异常简单。简单一两步操作即可。
+ 所有课约束的条件大部分与「spring-data-jpa」符合，请查阅[「spring-data-jpa官方文档」](https://docs.spring.io/spring-data/jpa/docs/2.0.1.RELEASE/reference/html/#jpa.query-methods.query-creation)
+ 用例请查看项目代码：[「码云／GitOSC」](https://gitee.com/wuwenbn/RepositoryTester/blob/master/src/main/java/me/wuwenbin/modules/repodata/repository/UserRepository.java) 
---
## insert语句/save方法命名规则
+ 方法名必须以「save」打头。
+ 方法的返回值必须为「T」或者为「List\<T\>」。
+ 方法的参数类型可以有两种情况。其一：有且只能有一种类型，个数不限（数组或者是不定参数则为此种情形）；其二：可以有多种类、多个参数，要求与sql语句的参数名相同。
+ 方法的参数种类必须为「Map<String,Object>」、「T」、「Collection<T>（或其子接口）」、「T\[](也可为不定参数形式T...)」、「Map<String,Object>\[](同T[],也可为不定参数形式Map<String,Object>...)」其中之一的类型。
+ 插入所有字段已经预定在「IBaseCrudRepository」接口中，请直接使用你的「Repository」接口继承此接口使用「save」方法即可。
+ 需要插入非全部字段请使用「@SaveSQL」指定 sql 或「@Routers」指定插入的字段，参数和返回类型请参考第2、3、4准则。
+ 如果参数为多种类型（即使用@SaveSQL指定sql，并且参数为多个，依照sql语句的参数来），那么方法的返回值只能为int（返回插入的个数，不能返回一个对象）。
+ 自定义方法名须配合注解「@SaveSQL」或「@Routers」来使用。
+ 其他以上未满足条件的，请使用原生「DaoFactory」的「AncestorDao」接口执行sql。
---
## delete语句/delete方法命名规则
+ 方法名必须以「delete」打头。
+ 所有「delete」方法返回值必须为「void」。
+ 依据主键删除的「delete」方法，以及非主键条件的单条件「deleteBy...」方法参数类型只能为「Java基本类型」「数组」「集合」。
+ 不支持使用@Routers情况下的多个参数的方法，因为这种情况下的参数名匹配不到正确的参数值情况。
+ 多条件请使用自定义命名或者「@DeleteSQL」注解。
+ 自定义方法名支持条件约束，如>、<等，但是若参数为多个基本类型参数时，则不支持关键字约束，如Like，Null等。
+ 如果需要支持关键字约束自定义查询，请使用单个参数形式，如Map、T等形式。
---
## select语句/find方法命名规则
+ 除「count...」和「exists」计数方法外，其余的查询方法全部以「find」打头。
+ 自定义的「findBy」方法根据方法的返回值来映射查询结果。
+ 如果自定义方法名中含有「Between」则参数中必须有 「prefixParam」和「suffixParam」两个参数，即前缀为「prefix」，后缀为「suffix」。
+ 无参的方法仅支持「count」统计所有数据数量以及「findAll」查询所有数据两种方法，其余暂不支持。
+ 支持自定义的方法名查询，对应相关的sql操作。
+ 智能根据方法的参数以及方法的返回类型做出相应的操作。
+ sql参数形式请根据方法参数种类来赋值。? -> Array ,  : -> Map/JavaBean
---
## update语句/update方法命名规则
+ 所有方法均为「update」开头。
+ 方法参数可以为「Map」或「T」或多个基本类型。
+ 方法的返回值必须为「int」。
+ 「@Modify」必须和「updateByXxx」配合使用，方法的参数必须为「Map」或者「T」。
+ 指定的sql语句请配合适当的参数类型（冒号/问号，基本类型/非基本类型）。
+ 参数为多种多个时，sql语句参数形式为冒号形式。
---
##分页方法
+ 分页方法是结合「pagination」模块使用的，目前已经实现BootstrapTable和LayTable两大Table的前后台稳定交互。
+ 包含两个方法，定义在「IPageAndSortRepository」中。
