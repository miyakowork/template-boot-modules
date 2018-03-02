package me.wuwenbin.modules.echarts4.option.property;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.wuwenbin.modules.echarts4.option.EchartsEntity;
import me.wuwenbin.modules.echarts4.option.constant.Target;

/**
 * created by Wuwenbin on 2018/2/28 at 14:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Title extends EchartsEntity {
    private boolean show;
    private String text;
    private String link;
    @Builder.Default
    private Target target = Target.blank;
}
