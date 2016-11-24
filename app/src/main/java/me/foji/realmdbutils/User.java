package me.foji.realmdbutils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Scott Smith  @Date 2016年11月2016/11/23日 14:59
 */
public class User extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
