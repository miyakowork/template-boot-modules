package me.wuwenbin.modules.repository.provider.find.param;

import me.wuwenbin.modules.pagination.sort.Sorting;
import me.wuwenbin.modules.pagination.sort.direction.Direction;
import me.wuwenbin.modules.repository.provider.find.support.Condition;
import me.wuwenbin.modules.repository.provider.find.support.FiledValue;

import java.util.*;

/**
 * created by Wuwenbin on 2017/11/7 at 22:20
 *
 * @author Wuwenbin
 */
public class SelectQuery {

    private List<String> selectTargets;
    private List<Condition> conditions;
    private List<Sorting> sorts;

    private SelectQuery(List<String> selectTargets, List<Condition> conditions, List<Sorting> sorts) {
        this.selectTargets = selectTargets;
        this.conditions = conditions;
        this.sorts = sorts;
    }

    public List<String> getSelectTargets() {
        return selectTargets;
    }

    public void setSelectTargets(List<String> selectTargets) {
        this.selectTargets = selectTargets;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Sorting> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sorting> sorts) {
        this.sorts = sorts;
    }


    public static SelectQuery build(List<String> selectTargets, List<Condition> conditions, List<Sorting> sorts) {
        return new SelectQuery(selectTargets, conditions, sorts);
    }

    public static SelectQuery build(List<Condition> conditions) {
        return build(null, conditions, null);
    }

    public static SelectQuery build(List<String> selectTargets, List<Condition> conditions) {
        return build(selectTargets, conditions, null);
    }

    public static SelectQuery build(FiledValue... filedValues) {
        return build(Condition.buildList(filedValues));
    }

    public static SelectQuery build(Condition... conditions) {
        return build(Condition.buildList(conditions));
    }

    public static SelectQuery build(List<String> selectTargets, Condition... conditions) {
        return build(selectTargets, Arrays.asList(conditions));
    }

    /**
     * 获取where部分的sql语句
     */
    public String getWhereSqlPart() {
        List<Condition> conditions = this.getConditions();
        String sql = " where 1=1";
        for (Condition condition : conditions) {
            sql = sql.concat(condition.getPreJoin().name()).concat(condition.getConstraint().getPart(condition.getField(), condition.getField()));
        }
        return sql;
    }

    /**
     * 获取where语句中对应的参数map，提供能jdbcTemplate查询的参数使用
     *
     * @return
     */
    public Map<String, Object> getParamMap() {
        List<Condition> conditions = getConditions();
        if (conditions != null) {
            Map<String, Object> param = new HashMap<>(conditions.size());
            for (Condition condition : conditions) {
                param.put(condition.getField(), condition.getValue());
            }
            return param;
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * sort方法为额外需要增加排序的方法才使用，否则可以略过
     *
     * @param sortName
     * @param direction
     * @return
     */
    public SelectQuery sort(String sortName, Direction direction) {
        Sorting sorting = new Sorting();
        sorting.setSortName(sortName);
        sorting.setSortDirection(direction);
        this.setSorts(Collections.singletonList(sorting));
        return this;
    }

    public SelectQuery sortAsc(String sortName) {
        Sorting sort = Sorting.asc(sortName);
        this.setSorts(Collections.singletonList(sort));
        return this;
    }

    public SelectQuery sortDesc(String sortName) {
        Sorting sort = Sorting.desc(sortName);
        this.setSorts(Collections.singletonList(sort));
        return this;
    }

    public SelectQuery sort(Sorting... sorts) {
        this.setSorts(Arrays.asList(sorts));
        return this;
    }

}
