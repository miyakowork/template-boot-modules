package me.wuwenbin.modules.repository.provider.select.page.support.fake.column;

import me.wuwenbin.modules.pagination.util.StringUtils;
import me.wuwenbin.modules.repository.provider.select.page.support.AbstractPageExtra;

import java.util.UUID;
import java.util.function.UnaryOperator;

/**
 * 伪列功能的查询
 * 根据表的某个字段排序后，要对这些数据加上序列，这个时候序号常常不是我们建表时设置好的自增的主键id，我们可以使用伪列的办法来排序
 * created by Wuwenbin on 2018/2/7 at 12:51
 */
public class FakeColPageExtra extends AbstractPageExtra {

    private String fakeColName;
    private String fakeTableName;

    public String getFakeColName() {
        return fakeColName;
    }

    public void setFakeColName(String fakeColName) {
        this.fakeColName = fakeColName;
    }

    public String getFakeTableName() {
        return fakeTableName;
    }

    public void setFakeTableName(String fakeTableName) {
        this.fakeTableName = fakeTableName;
    }

    public FakeColPageExtra(String fakeColName, String fakeTableName) {
        this.fakeColName = fakeColName;
        this.fakeTableName = fakeTableName;
    }

    @Override
    public boolean isSupportFakeColumn() {
        return true;
    }

    @Override
    public UnaryOperator<String> fakeColumnDeal() {
        String fakeColumn = "@fakecol:=@fakecol+1 AS " + fakeColName;
        if (StringUtils.isEmpty(fakeTableName)) {
            this.fakeTableName = "___t___" + UUID.randomUUID().toString().replace("-", "");
        }
        String fakeTable = "(SELECT @fakecol:=0) " + this.fakeTableName;
        return (s) -> {
            s = "SELECT " + fakeColumn + ",".concat(s.substring(7));
            int fromIndex = s.toLowerCase().indexOf("from");
            s = s.substring(0, fromIndex - 1).concat(" from ").concat(fakeTable).concat(",").concat(s.substring(fromIndex + 5));
            return s;
        };
    }

}
