package org.example.zhc.util.zhc.validation.mongo.helper;

public final class Key {
    private final static String COLLECTION_FORMAT = "_col_%s_%s";
    private final static String PRIMARY_FORMAT = "_primary_%d%s%s";
    private final static String QUERY_ALL_FORMAT = "_query_%d%s";

    public static final String PRIMARY_KEY = "primaryKey";
    public static final String DELETE_KEY = "isDel";
    public static final String KEY_KEY = "key";
    public static final String VALUE_KEY = "value";
    public static final String QUERY_ALL_KEY = "queryAllKey";

    public static String getColName(String appId,String regionId){
        return String.format(COLLECTION_FORMAT,appId,regionId);
    }

    public static String getQueryAllValue(Integer logicType, String ownerId){
        return String.format(QUERY_ALL_FORMAT,logicType,ownerId );
    }

    public static String getPrimaryValue(Integer logicType, String ownerId, String key){
        return String.format(PRIMARY_FORMAT,logicType,ownerId,key);
    }
}
