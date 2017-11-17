package me.wuwenbin.modules.repository.provider.find.param;

import me.wuwenbin.modules.pagination.sort.Sorting;
import me.wuwenbin.modules.pagination.sort.direction.Direction;
import me.wuwenbin.modules.repository.provider.find.support.Condition;
import me.wuwenbin.modules.repository.provider.find.support.Param;

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


    public static SelectQuery defaultQuery(Param... params) {
        SelectQuery query = new SelectQuery();
        query.setSelectTargets(null);
        query.setConditions(Condition.defaultList(params));
        query.setSorts(null);
        return query;
    }

    public static SelectQuery simpleQuery(Condition... conditions) {
        SelectQuery query = new SelectQuery();
        query.setSelectTargets(null);
        query.setConditions(Condition.list(conditions));
        query.setSorts(null);
        return query;
    }

    public static SelectQuery query(List<String> routers, Condition... conditions) {
        SelectQuery selectQuery = new SelectQuery();
        selectQuery.setSelectTargets(routers);
        selectQuery.setConditions(Arrays.asList(conditions));
        selectQuery.setSorts(null);
        return selectQuery;
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
