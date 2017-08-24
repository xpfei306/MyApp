package xpfei.myapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Description: 用户信息
 * Author: xpfei
 * Date:   2017/07/27
 */
@Entity
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 8364158478786424902L;
    @Id
    private long userid;
    private String username;
    private String address;
    private int age;
    private String headimage;
    private String name;
    private String phonenum;
    private String psw;
    private String sex;

    @Generated(hash = 1464713746)
    public UserInfo(long userid, String username, String address, int age,
                    String headimage, String name, String phonenum, String psw,
                    String sex) {
        this.userid = userid;
        this.username = username;
        this.address = address;
        this.age = age;
        this.headimage = headimage;
        this.name = name;
        this.phonenum = phonenum;
        this.psw = psw;
        this.sex = sex;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public long getUserid() {
        return this.userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeadimage() {
        return this.headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return this.phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPsw() {
        return this.psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
