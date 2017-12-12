package me.wuwenbin.modules.sql.factory;


import me.wuwenbin.modules.sql.exception.DeleteSQLConditionsNullException;
import me.wuwenbin.modules.sql.exception.InsertColumnNullException;
import me.wuwenbin.modules.sql.exception.TableNameNullException;
import me.wuwenbin.modules.sql.exception.UpdateColumnNullException;
import me.wuwenbin.modules.sql.util.SQLBuilderUtils;

/**
 * 不根据实体类,根据一些参数生成一些常见SQL
 * Created by wuwenbin on 2017/1/9.
 *
 * @author wuwenbin
 * @since 1.1.0
 */
public class SQLTextBuilder {


    private final String SPACE = " ";
    private final String FROM = SPACE + "FROM" + SPACE;
    private final String AND = SPACE + "AND" + SPACE;
    private final String WHERE = SPACE + "WHERE" + SPACE;
    private final String selectPre = "SELECT" + SPACE;
    private final String insertPre = "INSERT INTO" + SPACE;

    /**
     * 无条件统计一个表
     *
     * @param tableName 表名
     * @return
     */
    public String countAll(String tableName) throws TableNameNullException {
        return countAndByColumns(tableName);
    }

    /**
     * AND条件统计一张表的记录
     *
     * @param tableName 表名
     * @param columns   where条件需要and统计的列名
     * @return
     * @throws TableNameNullException
     */
    public String countAndByColumns(String tableName, String... columns) throws TableNameNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            StringBuilder sb = new StringBuilder(selectPre);
            sb.append("COUNT(*)").append(FROM).append(tableName);
            if (columns != null && columns.length > 0) {
                sb.append(WHERE).append("1=1");
                for (String column : columns) {
                    sb.append(AND).append(column).append(" = ?");
                }
            }
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }

    /**
     * OR条件统计一张表的记录
     *
     * @param tableName 表名
     * @param columns   where条件需要or统计的列名
     * @return
     * @throws TableNameNullException
     */
    public String countOrByColumns(String tableName, String... columns) throws TableNameNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            StringBuilder sb = new StringBuilder(selectPre);
            sb.append("COUNT(*)").append(FROM).append(tableName);
            if (columns != null && columns.length > 0) {
                sb.append(WHERE).append("1<>1");
                String OR = SPACE + "OR" + SPACE;
                for (String column : columns) {
                    sb.append(OR).append(column).append(" = ?");
                }
            }
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }

    /**
     * AND条件查询所选择的字段
     *
     * @param tableName        表名
     * @param selectColumns    选择要查询的字段routers
     * @param conditionColumns and条件的routers
     * @return
     * @throws TableNameNullException
     */
    public String selectPartByColumnsAnd(String tableName, String[] selectColumns, String... conditionColumns) throws TableNameNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            StringBuilder sb = new StringBuilder(selectPre);
            if (selectColumns != null && selectColumns.length > 0) {
                for (int i = 0; i < selectColumns.length; i++) {
                    String selectColumn = selectColumns[i];
                    sb.append(tableName).append(".").append(selectColumn).append(", ");
                }
            } else {
                sb.append(tableName).append(".* ");
            }
            sb.append(FROM).append(tableName);
            assembleConditionSQL(tableName, sb, conditionColumns);
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }

    /**
     * AND条件查询所有字段
     *
     * @param tableName 表名
     * @param columns   条件字段
     * @return
     * @throws TableNameNullException
     */
    public String selectAllByColumns(String tableName, String... columns) throws TableNameNullException {
        return selectPartByColumnsAnd(tableName, null, columns);
    }

    /**
     * 无条件查询所有
     *
     * @param tableName 表名
     * @return
     * @throws TableNameNullException
     */
    public String selectAll(String tableName) throws TableNameNullException {
        return selectAllByColumns(tableName);
    }

    /**
     * 插入所有字段
     *
     * @param tableName   表名
     * @param columnCount 总共有多少列
     * @return
     * @throws TableNameNullException
     */
    public String insertAll(String tableName, int columnCount) throws TableNameNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            StringBuilder sb = new StringBuilder(insertPre);
            sb.append(tableName).append(" VALUES(");
            columnCount = columnCount <= 0 ? 1 : columnCount;
            for (int i = 0; i < columnCount; i++) {
                sb.append("?, ");
            }
            sb.append(")");
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }

    /**
     * 插入指定字段
     *
     * @param tableName 表名
     * @param columns   插入的字段
     * @return
     * @throws TableNameNullException
     * @throws InsertColumnNullException
     */
    public String insertColumns(String tableName, String... columns) throws TableNameNullException, InsertColumnNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            StringBuilder sb = new StringBuilder(insertPre);
            sb.append(tableName);
            if (columns != null && columns.length > 0) {
                sb.append("(");
                StringBuilder values = new StringBuilder("(");
                for (String column : columns) {
                    sb.append(column).append(", ");
                    values.append("?, ");
                }
                sb.append(") ").append(" VALUES").append(values).append(")");
            } else {
                throw new InsertColumnNullException();
            }
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }

    /**
     * 根据指定列条件更新指定列
     *
     * @param tableName        表名
     * @param updateColumns    指定要更新的列
     * @param conditionColumns 指定where条件的列
     * @return
     * @throws TableNameNullException
     * @throws UpdateColumnNullException
     */
    public String updateColumnsByColumnArray(String tableName, String[] updateColumns, String[] conditionColumns) throws TableNameNullException, UpdateColumnNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            String updatePre = "UPDATE" + SPACE;
            StringBuilder sb = new StringBuilder(updatePre);
            sb.append(tableName);
            if (updateColumns != null && updateColumns.length > 0) {
                sb.append(" SET ");
                for (String updateColumn : updateColumns) {
                    sb.append(updateColumn).append(" = ?, ");
                }
            } else {
                throw new UpdateColumnNullException();
            }
            if (conditionColumns != null && conditionColumns.length > 0) {
                sb.append(WHERE);
                for (String conditionColumn : conditionColumns) {
                    sb.append(conditionColumn).append(" = ?").append(AND);
                }
            }
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }

    /**
     * 根据指定的列条件删除表中的记录
     *
     * @param tableName
     * @param columns
     * @return
     * @throws TableNameNullException
     * @throws DeleteSQLConditionsNullException
     */
    public String deleteByColumns(String tableName, String... columns) throws TableNameNullException, DeleteSQLConditionsNullException {
        if (tableName == null || "".equals(tableName)) {
            throw new TableNameNullException();
        } else {
            String deletePre = "DELETE FROM" + SPACE;
            StringBuilder sb = new StringBuilder(deletePre);
            sb.append(tableName);
            if (columns != null && columns.length > 0) {
                sb.append(WHERE);
                for (String column : columns) {
                    sb.append(column).append(" = ?").append(AND);
                }
            } else {
                throw new DeleteSQLConditionsNullException();
            }
            return SQLBuilderUtils.dealSQL(sb.toString());
        }
    }
    //=================通用部分===============//

    /**
     * 组装查询部分SQL
     *
     * @param tableName
     * @param sb
     * @param column
     */
    private void assembleConditionSQL(String tableName, StringBuilder sb, String[] column) {
        if (column != null && column.length > 0) {
            sb.append(WHERE).append("1=1");
            for (String conditionColumn : column) {
                sb.append(AND).append(tableName).append(".").append(conditionColumn).append(" = ?");
            }
        }
    }
}
