## Template Modules Repository
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.wuwenbin/template-modules-repository/badge.svg)](http://search.maven.org/#artifactdetails%7Cme.wuwenbin%7Ctemplate-modules-repository%7C1.10.16.RELEASE%7Cjar)
---
template modules repository 轻松实现大部分的sql操作，省去了写大量sql的代码以及大量service实现类的代码，只需定义接口即可完成所需的大部分操作。
## 当前版本：
   ```xml
       <dependency>
            <groupId>me.wuwenbin</groupId>
            <artifactId>template-modules-repository</artifactId>
            <version>1.11.5-SNAPSHOT</version>
        </dependency>
   ```
   ---
   ## 新增 - 20180426
   + 增加repository扫描的包为多个
   ## 新增 - 20180424
   + 增加对update方法返回类型为int[]类型的方法参数的额外一种判断，基础类型对象的数组（包括String）
   ## 修复 - 20180411
   + 对删除方法的参数个数判断做了进一步改进，现在可以为空了
   + 更新操作可以使用原生接口update方法快速操作了
   + 支持多数据源操作了
   ## 新增 - 20180226
   + 针对 sql 模块做出的新增 @SQLPk 注解做相关代码优化    
   ## 改进 - 20180220
   + 将 IBaseCrudRepository 划分为更细的4个层面，分别作为单独的接口
   ## 新增 - 20180207
   + 增加 PageRepository 额外查询列的功能（当前仅支持伪列的功能，其它功能后续跟进）
   ## 新增 - 20180201
   + 细化 Repository 的接口功能，PageAndSort 划为单独的接口（以前只继承IPageAndSort接口的需要多继承一个IBaseCrudRepository接口）
   + 增加@OrderBys注解，可以支持多个排序字段了
   ## 新增 - 20170114
   + 给@Repository注解增加了@Component，现在IDEA可以正确的识别@Autowired注入了
   ## 新增/修复 - 20180103
   + 新增find方法可以返回List&lt;Map&lt;String, Object&gt;&gt;的类型的方法
   + 修复save方法在保存无主键的表的时候的插入问题
   ## 修复 - 20180101
   + 修复当表不存在主键的时候，抛出的PkFieldNotExistException存在的异常处理
   + 修复findXxxByXxx的生成sql有误的问题（驼峰转下划线出问题）
   ## 修复 - 20171228
   + 修复自定义的find方法的sql语句参数占位符类型错误的问题
   + 修复注册Repository的Bean过早的问题（解决诸如DaoFactory的nullPointerException的错误）
   + 修复find方法多个参数的时候，使用@Routers注解的时候不识别注解的情况
   + 修复 Not unique table/alias的错误
   ## 新增 - 20171225
   + 新增查找返回类型为基础集合（如 List<String>）的判定，适用于查找集合中只有一列的情况。
       请使用使用注解 @PrimitiveCollection 在方法上标识（此处标识的原因是因为java的泛型机制导致不能直接获取到集合的泛型类型） 
   + 修改返回类型为基础类型的方法逻辑为  map/iterator -> findPrimitiveByXxx();/findListPrimitiveByXxx();
   ## 新增 - 20171224
   + 新增批量更新的方法
   + 新增 int[] 返回值的判定，可实现批量更新的方法
   ## 修复 - 20171223
   + 修复一个查询方法的BUG （array -> map）。
   ## 更新 - 20171217
   + 增加select语句的order by，使用@OrderBy注解
   + 重新构建UpdateProvider代码
   + 删除重复功能的注解，使用统一的@SQL注解
   + 新增@SaveSQL注解，简单实现插入操作
   
  
   ---
   ## 入门操作方法概要
### 说明
+ 以下把【String】类型也归为基本类型，此处是为了更好的判别返回方法类型
+ 需要JDK8 以上
###  An fast and convenient api for data-operation
+ 以下所有的实体类都需要有相关注解支持（详细请参考文档）。
+ 本模块中的所有sql语句（除使用@XxxxSQL注解指定sql的之外）参数形式全部为 「filedValue = :paramKey」形式，即冒号形式。
+ 「Repository」模块可以使应用程序访问数据库变得异常简单。简单一两步操作即可。
+ 所有课约束的条件大部分与「spring-data-jpa」符合，请查阅[「spring-data-jpa官方文档」](https://docs.spring.io/spring-data/jpa/docs/2.0.1.RELEASE/reference/html/#jpa.query-methods.query-creation)
+ 用例请查看项目代码：[「码云／GitOSC」](https://gitee.com/wuwenbn/RepositoryTester/) 
+ 代码会自动根据参数类型和方法的返回类型执行相应的方法，注意「sql」中参数的类型（冒号或问号）即可。
+ 若方法上存在对应的注解，则注解优先，方法名在此处则作为无意义的标识。
+ 自定义方法名请遵循驼峰命名规则，若是有非等于条件的约束参数，则一并使用「Equal」、「Like」等词汇写入方法名即可，详情见「Constraint」类。
+ 方法参数多个时候SQL语句参数形式一定为「？」形式。
---
### insert语句/save方法命名规则
+ 方法名必须以「save」打头。
+ 批量插入的时候返回值则只能为「int[]」。
+ 如果多个基本类型参数作为方法的参数，则方法的返回值一定为「int」。

---
### delete语句/delete方法命名规则
+ 方法名必须以「delete」打头。
+ 所有「delete」方法返回值必须为「void」。
+ 依据主键删除的「delete」方法，以及非主键条件的单条件「deleteBy...」方法参数类型只能为「Java基本类型」「数组」「集合」。
+ 不支持使用@Routers情况下的多个参数的方法，因为这种情况下的参数名匹配不到正确的参数值情况。
+ 多条件请使用自定义命名或者「@DeleteSQL」注解。
+ 自定义方法名支持条件约束，如>、<等，但是若参数为多个基本类型参数时，则不支持关键字约束，如Like，Null等。
+ 如果需要支持关键字约束自定义查询，请使用单个参数形式，如Map、T等形式。
+ 「@DeleteSQL」自定义sql语句可以省略「delete from \[table\]」前缀。
---
### select语句/find方法命名规则
+ 除「count...」和「exists」计数方法外，其余的查询方法全部以「find」打头。
+ 自定义的「findBy」方法根据方法的返回值来映射查询结果。
+ 如果自定义方法名中含有「Between」则参数中必须有 「prefixParam」和「suffixParam」两个参数，即前缀为「prefix」，后缀为「suffix」。
+ 无参的方法仅支持「count」统计所有数据数量以及「findAll」查询所有数据两种方法，其余暂不支持。
+ 支持自定义的方法名查询，对应相关的sql操作。
+ 智能根据方法的参数以及方法的返回类型做出相应的操作。
+ sql参数形式请根据方法参数种类来赋值。? -> Array ,  : -> Map/JavaBean
+ 多个条件/结果注解（非SQL类注解）判断的优先级：@PrimitiveCollection =  @Primitive > @ListMap > @OrderBy（@Rand随机语句除外）
---
### update语句/update方法命名规则
+ 所有方法均为「update」开头。
+ 方法参数可以为「Map」或「T」或多个基本类型。
+ 方法的返回值必须为「int」。
+ 「@Modify」必须和「updateByXxx」配合使用，方法的参数必须为「Map」或者「T」。
+ 指定的sql语句请配合适当的参数类型（冒号/问号，基本类型/非基本类型）。
+ 参数为多种多个时，sql语句参数形式为冒号形式。
---
###分页方法
+ 分页方法是结合「pagination」模块使用的，目前已经实现BootstrapTable和LayTable两大Table的前后台稳定交互。
+ 包含两个方法，定义在「IPageAndSortRepository」中。
