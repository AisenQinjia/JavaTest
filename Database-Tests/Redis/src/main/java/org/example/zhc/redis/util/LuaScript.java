package org.example.zhc.redis.util;

import org.example.FileUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lua脚本获取
 */
public final class LuaScript {
    private LuaScript(){}
    public static String STATEFUL_GET_SCRIPT;
    public static String statefulGet() {
        if(STATEFUL_GET_SCRIPT == null){
            STATEFUL_GET_SCRIPT = FileUtil.readResourceFile("statefulget.lua");
        }
        return STATEFUL_GET_SCRIPT;
    }

    public static String SORTED_SET_GET_SCRIPT;
    public static String zGet(){
        if(SORTED_SET_GET_SCRIPT == null){
            SORTED_SET_GET_SCRIPT = FileUtil.readResourceFile("sortedset/get.lua");
        }
        return SORTED_SET_GET_SCRIPT;
    }



}
