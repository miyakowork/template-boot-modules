package me.wuwenbin.modules.echarts4.option;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * created by Wuwenbin on 2018/2/28 at 14:57
 */
@Getter
@Setter
public class EchartsEntity implements Serializable {

    /**
     * 一些默认值和固定值
     */
    protected static final String EMPTY = "";
    protected static final String BR = "\\n";
    protected static final Boolean TRUE = Boolean.TRUE;
    protected static final Boolean FALSE = Boolean.FALSE;
}
