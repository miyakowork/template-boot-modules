## 更新日志
> 1.0.0.RELEASE
>>·初始版本发布，支持BootstrapTable和LayTable，同时支持第三方dataTable扩展
>
> 1.1.0.RELEASE
>>·配合官方layui2.1.0的升级，laytable支持表格排序监听
>>
>>·官方更新支持自定义返回格式和请求参数名的自定义，此处我们还是使用默认的。不适用自定义。
>
> 1.1.1.RELEASE
>>.增加了Sorting的量静态快速生成方法

## 基本使用文档
> 基本使用方法步骤如下：
- 0、继承 `TableQuery` 类（如果使用`BootstrapTableQuery` 或 `LayTableQuery` 则不需要此步骤，直接下一步）
- 1、新建你的表格查询字段对应的BO类，继承对应的 `TableQuery`,例如
> ```java
> @QueryTable(name = "t_system")
> public class TestBo extends BootstrapTableQuery {
>     private String username;
>     @QueryColumn(operator = Operator.EQ,column = "l_d")
>     private String loginDate;
>     @QueryColumn(operator = Operator.BETWEEN_AND)
>     private String regDate;
>    public String getUsername() {
>       return username;
>    }
>     public void setUsername(String username) {
>      this.username = username;
>   }
>     public String getLoginDate() {
>      return loginDate;
>     }
>     public void setLoginDate(String loginDate) {
>      this.loginDate = loginDate;
>     }
>     public String getRegDate() {
>       return regDate;
>     }
>     public void setRegDate(String regDate) {
>         this.regDate = regDate;
>     }
 > }
>```
- 2、使用 `Pagination` 中的静态方法分别获取sql以及参数Map，以供service调用