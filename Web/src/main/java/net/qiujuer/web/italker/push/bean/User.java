package net.qiujuer.web.italker.push.bean;

/**
 * Created by savypan
 * On 2021/3/21 19:57
 */
public class User {
    private String name;
    private int sex;

    public User() {

    }

    public User(String name, int sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
