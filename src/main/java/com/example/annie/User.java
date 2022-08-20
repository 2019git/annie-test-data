package com.example.annie;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wzj
 * @date 2022/8/20 14:23
 */
@Getter
@Setter
public class User implements Serializable {

    private String userId;

    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
