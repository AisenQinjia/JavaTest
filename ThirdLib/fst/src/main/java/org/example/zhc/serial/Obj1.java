package org.example.zhc.serial;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class Obj1 implements Serializable {
    String name;
    int age;
    long id;
    Map<String,String> map;
    List<String> listStr;
}
