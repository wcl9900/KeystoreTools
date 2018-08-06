package com.wcl.keytool.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zjh on 2017/8/29.
 */
public class FileCacheEntity implements Serializable{
    private Map<String, FileEntity> fileCacheMap;

    public Map<String, FileEntity> getFileCacheMap() {
        return fileCacheMap;
    }

    public void setFileCacheMap(Map<String, FileEntity> fileCacheMap) {
        this.fileCacheMap = fileCacheMap;
    }
}
