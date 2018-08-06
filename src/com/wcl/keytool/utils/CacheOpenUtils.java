package com.wcl.keytool.utils;

import com.wcl.keytool.entity.FileCacheEntity;
import com.wcl.keytool.entity.FileEntity;

import java.io.*;
import java.util.HashMap;

/**
 * Created by zjh on 2017/8/29.
 */
public class CacheOpenUtils {

    private static String pathname = System.getProperty("user.dir") + "/i";

    public static void put(String fileMd5, String alias, String password){
        File file = new File(pathname);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileCacheEntity o;
            if(file.length() == 0){
                o = new FileCacheEntity();
                o.setFileCacheMap(new HashMap<>());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(o);
                objectOutputStream.close();
            }

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            o = (FileCacheEntity) objectInputStream.readObject();
            objectInputStream.close();
            o.getFileCacheMap().put(fileMd5, new FileEntity(alias, password));

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static FileEntity get(String md5){
        File file = new File(pathname);
        ObjectInputStream objectInputStream = null;
        try {
            if(!file.exists()){
                return null;
            }
            if(file.length() == 0) return null;
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            FileCacheEntity o =  (FileCacheEntity) objectInputStream.readObject();
            if(o != null && o.getFileCacheMap() != null){
                return o.getFileCacheMap().get(md5);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
