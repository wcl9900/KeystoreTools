package com.wcl.keytool.entity;

import java.io.Serializable;

/**
 * Created by zjh on 2017/8/29.
 */
public class FileEntity implements Serializable{
    private String alias;
    private String password;

    public FileEntity(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
