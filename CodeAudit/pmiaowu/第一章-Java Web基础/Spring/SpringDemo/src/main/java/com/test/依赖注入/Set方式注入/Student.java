// 第二步
// 在./SpringDemo/src/main/java/com/test/依赖注入/Set方式注入/创建个Student.java
// 目录: ./SpringDemo/src/main/java/com/test/依赖注入/Set方式注入/
// 文件名: Student.java
package com.test.依赖注入.Set方式注入;

import java.util.*;

public class Student {
    private String name;
    private Person person;
    private String[] arr;
    private List<String> myList;
    private Map<String, String> myMap;
    private Set<String> mySet;
    private String wife;
    private Properties myPro;

    public void setName(String name) {
        this.name = name;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }

    public void setMySet(Set<String> mySet) {
        this.mySet = mySet;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public void setMyPro(Properties myPro) {
        this.myPro = myPro;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + this.name + "', " +
                "person=" + this.person.toString() + ", " +
                "arr=" + Arrays.toString(this.arr) + ", " +
                "myList=" + this.myList + ", " +
                "myMap=" + this.myMap + ", " +
                "mySet=" + this.mySet + ", " +
                "wife='" + this.wife + "', " +
                "myPro=" + this.myPro + "" +
                "}";
    }
}