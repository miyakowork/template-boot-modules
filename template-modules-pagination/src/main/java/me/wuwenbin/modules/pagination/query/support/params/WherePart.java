package me.wuwenbin.modules.pagination.query.support.params;

import java.util.Map;
import java.util.TreeMap;

/**
 * created by Wuwenbin on 2017/8/30 at 20:18
 */
public class WherePart {
    private String partSql;
    private Map<String, Object> mapParameter;

    public WherePart() {
        this.partSql = "";
        this.mapParameter = new TreeMap<>();
    }

    public String getPartSql() {
        return partSql;
    }

    public void setPartSql(String partSql) {
        this.partSql = partSql;
    }

    public Map<String, Object> getMapParameter() {
        return mapParameter;
    }

    public void setMapParameter(Map<String, Object> mapParameter) {
        this.mapParameter = mapParameter;
    }

    public void setParamsValue(String fieldName, Object fieldValue) {
        getMapParameter().put(fieldName, fieldValue);
    }

}
