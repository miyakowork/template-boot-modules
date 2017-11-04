> 以下所有的实体类都需要有@SQLTable以及字段有需要的都需要@SQLColumn注解支撑
---
## insert语句/save方法命名规则
1. 方法名必须以「save」打头
2. 方法的返回值必须为「T」或者为「List\<T\>」
3. 方法的参数有且只能有一种类型
4. 方法的参数种类必须为「Map<String,Object>」、「T」、「Collection<T>（或其子接口）」、「T[](也可为不定参数形式T...)」、「Map<String,Object>[](同T[],也可为不定参数形式Map<String,Object>...)」其中之一的类型
5. 插入所有字段已经预定在「IBaseCrudDataRepo」接口中，请直接使用
6. 需要插入非全部字段请使用「save$Router+router」的标识来命名，如：save$RouterABC()，参数和返回类型请参考第2、3、4准则。
---
## delete语句/delete方法命名规则
1. 方法名必须以「delete」打头
2. 