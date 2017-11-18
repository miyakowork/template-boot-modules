package me.wuwenbin.modules.repository.test.proxy;

import me.wuwenbin.modules.repository.test.Person;
import me.wuwenbin.modules.repository.test.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by Wuwenbin on 2017/10/28 at 11:20
 */
public interface UserService<S> extends PublicService<S> {

    int age();
    S name();

    void empty();

    List<S> save(String b, S... ss);

    List<S> save1(Map<String, Object> ss);

    List<S> save2(S[] ss, String a);

    List<S> save3(Collection<S> ss);

    List<S> save4(Set<S> ss);

    List<S> save5(List<S> ss);

    <Model extends Person, Te extends User> Map<Model, Te> findPage();

    <Model extends Person> List<Model> findList();

    List<Person> findList2();

    S name(String username, String password, int age);
}
