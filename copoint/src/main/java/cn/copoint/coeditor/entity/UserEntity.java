package cn.copoint.coeditor.entity;


import java.io.Serializable;

public class UserEntity implements Serializable
{

    private Long id;


    private String name;

//    private String authorID;

//    private int age;
//
//    private String address;

    private String pwd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

//    public String getAuthorID()
//    {
//        return this.authorID;
//    }
//    public void setAuthorID(String authorID)
//    {
//        this.authorID=authorID;
//    }
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }



}
