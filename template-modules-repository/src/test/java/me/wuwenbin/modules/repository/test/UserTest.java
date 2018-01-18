package me.wuwenbin.modules.repository.test;


import me.wuwenbin.modules.repository.util.BeanUtils;
import org.junit.Test;

/**
 * Created by wuwenbin on 2017/7/8.
 */
public class UserTest {

    @Test
    public void main() {
//        System.out.println(UserVO.class.getSuperclass().getSuperclass());
        System.out.println(BeanUtils.isPrimitive(long.class));
    }

}
