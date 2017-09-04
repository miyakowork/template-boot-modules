package me.wuwenbin.modules.jdbc.factory;

import me.wuwenbin.modules.jdbc.ancestor.AncestorDao;
import me.wuwenbin.modules.jdbc.exception.DataSourceKeyNotExistException;
import me.wuwenbin.modules.jdbc.factory.business.DataSourceX;
import me.wuwenbin.modules.jdbc.factory.business.DbType;
import me.wuwenbin.modules.jdbc.factory.support.KeyContextHolder;
import me.wuwenbin.modules.jdbc.posterity.h2.H2Template;
import me.wuwenbin.modules.jdbc.posterity.mysql.MysqlTemplate;
import me.wuwenbin.modules.jdbc.posterity.oracle.OracleTemplate;
import me.wuwenbin.modules.jdbc.posterity.postgresql.PostgreSqlTemplate;
import me.wuwenbin.modules.jdbc.posterity.sqlite.SqliteTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

/**
 * Dealing something while spring is initializing
 * <p>
 * Created by wuwenbin on 2017/1/2.
 *
 * @author wuwenbin
 * @since 1.0.0
 */
@Component
public class DaoFactory implements InitializingBean {

    /**
     * multi datasource,including datasource and initDbType
     * using map to collect
     */
    private Map<String, DataSourceX> dataSourceMap;

    /**
     * default <tt>dao</tt>
     * <p>
     * before using it,define <tt>@Autowired DaoFactory daoFactory</tt> is necessary
     * using method:<tt>daFactory.defaultDao.xxxxxx()</tt> to execute it
     */
    public AncestorDao defaultDao;

    /**
     * generate <tt>daoMap</tt> by dataSources' map,the key refers to the key of dataSourceX
     */
    public Map<String, AncestorDao> daoMap = new Hashtable<>();

    /**
     * this default value is equals to defaultDao
     * use this param commonly,it is enough
     */
    public AncestorDao dynamicDao;


    public void setDataSourceMap(Map<String, DataSourceX> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    /**
     * 线程同步即可，此方法是不会再程序运行中调用，初始化即调用
     *
     * @param dataSourceX
     */
    public synchronized void setDefaultDao(DataSourceX dataSourceX) {
        if (dataSourceX.getInitDbType() == DbType.H2) {
            this.defaultDao = new H2Template(dataSourceX.getDataSource());
        } else if (dataSourceX.getInitDbType() == DbType.Oracle) {
            this.defaultDao = new OracleTemplate(dataSourceX.getDataSource());
        } else if (dataSourceX.getInitDbType() == DbType.Sqlite) {
            this.defaultDao = new SqliteTemplate(dataSourceX.getDataSource());
        } else if (dataSourceX.getInitDbType() == DbType.Postgresql) {
            this.defaultDao = new PostgreSqlTemplate(dataSourceX.getDataSource());
        } else {
            this.defaultDao = new MysqlTemplate(dataSourceX.getDataSource());
        }
        this.dynamicDao = this.defaultDao;
    }


    /**
     * 根据当前线程中的变量来确定切换的dynamicDao
     *
     * @return
     * @throws DataSourceKeyNotExistException
     */
    public synchronized void determineTargetDao() throws DataSourceKeyNotExistException {
        String currentThreadKey = KeyContextHolder.getKey();
        this.dynamicDao = getAncestorDaoByKey(currentThreadKey);
    }

    /**
     * init some values
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSourceMap != null && dataSourceMap.size() > 0 && !dataSourceMap.isEmpty()) {
            for (String key : dataSourceMap.keySet()) {
                if (daoMap == null || !daoMap.containsKey(key))
                    daoMap.put(key, getAncestorDaoByKey(key));
            }
        }
    }

    /**
     * get dao by key you set just now
     *
     * @param #key the key of dataSource you set
     * @return {@link AncestorDao}
     * @throws DataSourceKeyNotExistException
     */
    private AncestorDao getAncestorDaoByKey(String key) throws DataSourceKeyNotExistException {
        if (dataSourceMap.containsKey(key)) {
            DataSourceX dataSourceX = this.dataSourceMap.get(key);
            if (dataSourceX == null || dataSourceX.getDataSource() == null) {
                throw new IllegalStateException("不能以key [" + key + "] 来设置目标Dao");
            }
            if (dataSourceX.getInitDbType() == DbType.H2) {
                return new H2Template(dataSourceX.getDataSource());
            } else if (dataSourceX.getInitDbType() == DbType.Oracle) {
                return new OracleTemplate(dataSourceX.getDataSource());
            } else if (dataSourceX.getInitDbType() == DbType.Sqlite) {
                return new SqliteTemplate(dataSourceX.getDataSource());
            } else if (dataSourceX.getInitDbType() == DbType.Postgresql) {
                return new PostgreSqlTemplate(dataSourceX.getDataSource());
            } else {
                return new MysqlTemplate(dataSourceX.getDataSource());
            }
        } else throw new DataSourceKeyNotExistException();
    }


}
