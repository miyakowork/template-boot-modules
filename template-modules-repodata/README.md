> 以下所有的实体类都需要有@SQLTable以及字段有需要的都需要@SQLColumn注解支撑
---
## insert语句/save方法命名规则
1. 方法名必须以「save」打头。
2. 方法的返回值必须为「T」或者为「List\<T\>」。
3. 方法的参数有且只能有一种类型。
4. 方法的参数种类必须为「Map<String,Object>」、「T」、「Collection<T>（或其子接口）」、「T[](也可为不定参数形式T...)」、「Map<String,Object>[](同T[],也可为不定参数形式Map<String,Object>...)」其中之一的类型。
5. 插入所有字段已经预定在「IBaseCrudDataRepo」接口中，请直接使用。
6. 需要插入非全部字段请使用「save$Router+router」的标识来命名，如：save$RouterABC()，参数和返回类型请参考第2、3、4准则。
7. 如果需要自定义插入语句请使用「save$BySql...」，并且在此方法上使用@SaveSQL指定相关sql语句。eg：「save$BySqlXXX」(xxx一般为唯一标识)。
---
## delete语句/delete方法命名规则
1. 方法名必须以「delete」打头。
2. 所有「delete」方法返回值必须为「void」。
3. 方法的参数有且只能有一种类型。类型必须为「Map」、「T」、「Collection」、「数组」其中之一。
4. 根据主键删除/批量删除记录的方法已经预定义在「IBaseCrudDataRepo」接口中，请直接使用。
5. 如果不是依据主键删除，请使用「deleteBy...」或「deleteBy$Router...」方法。
6. 如果为「deleteBy...」字段来命名的，多个请使用「And」或「Or」其中之一来作为连接。eg：「deleteByRoleAndUserId」。
7. 如果为「deleteBy$Router...」方法，多个router也请使用「And」(可以省略)或「Or」其中之一来作为连接关键词。eg：「deleteBy$RouterAB」或「deleteBy$RouterAOrB」。
8. 如果需要自定义删除语句请使用「delete$BySql...」，并且在此方法上使用@DeleteSQL指定相关sql语句。eg：「delete$BySqlXXX」(xxx一般为唯一标识)。
9. 自定义方法中的「And」「Or」只能出现一种，不能同时出现。
