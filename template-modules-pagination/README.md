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