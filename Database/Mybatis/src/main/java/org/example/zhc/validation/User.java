package org.example.zhc.validation;

public class User {
    String owner_id;
    String key;
    byte[] value;
    public User(String owner_id,String key, byte[] value){
        this.owner_id = owner_id;
        this.key = key;
        this.value = value;
    }
}
