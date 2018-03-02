package me.wuwenbin.modules.echarts4.option.property;

import lombok.Getter;
import lombok.Setter;
import me.wuwenbin.modules.echarts4.option.EchartsEntity;
import me.wuwenbin.modules.echarts4.option.constant.Target;

/**
 * created by Wuwenbin on 2018/2/28 at 14:57
 */
@Getter
@Setter
public class Title extends EchartsEntity {
    private boolean show;
    private String text;
    private String link;
    private Target target = Target.blank;
}
