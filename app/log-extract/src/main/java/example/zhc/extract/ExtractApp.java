package example.zhc.extract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.example.zhc.util.FileUtil;

import java.io.IOException;
import java.util.*;

public class ExtractApp {

    static final String IOS_JSON = "ios.json";
    static final String IOS_CSV = "ios.csv";

    static final String ANDROID_JSON = "android.json";
    static final String ANDROID_CSV = "android.csv";

    static final String regex = ".*setUserMailTags_";
    static final String HEADER = "用户id,渠道号,模板邮件,未知邮件id\n";
    static final String metaPath = "templateIdMap.json";
    static Map<String,Map<Long,Integer>> meta;

    public static void main(String[] args) throws IOException {
        toCsv(IOS_JSON,IOS_CSV,"ios");
        toCsv(ANDROID_JSON,ANDROID_CSV,"android");
    }

    private static void toCsv(String jsonPath,String csvPath,String channel) throws IOException {
        String s = FileUtil.readCharacterFileToStr(jsonPath,false);
        String metaStr = FileUtil.readCharacterFileToStr(metaPath,false);
        meta = JSON.parseObject(metaStr, new TypeReference<Map<String,Map<Long,Integer>>>() {});
        String[] strs = s.split("\n");
        Map<String,MailTagReq>  ownerReqMap = new HashMap<>();
        for(String str: strs){
            JSONObject jsonObject = JSON.parseObject(str);
            String content = jsonObject.getString("content");
            System.out.println("content: " + content);
            MailTagReq mailTagReq = JSON.parseObject(content.replaceFirst(regex, ""), MailTagReq.class);
            MailTagReq preReq = ownerReqMap.get(mailTagReq.userId);
            if(preReq!=null){
                ownerReqMap.put(mailTagReq.userId,preReq.join(mailTagReq));
            }else{
                ownerReqMap.put(mailTagReq.userId,mailTagReq);
            }

        }
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER);
        for (MailTagReq req: ownerReqMap.values()){
            req.check(channel);
            sb.append(req.toCsvString());
            sb.append("\n");
        }
        FileUtil.write2File(csvPath,sb.toString());
    }
    static class MailTagReq {
        public String appId;
        public String ownerId;
        public String regionId;
        public Map<Long, Map<String,Object>> tagMap;
        public String userId;

        private List<String> templateIds = new ArrayList<>();
        private List<String> unknownMailIds = new ArrayList<>();

        public MailTagReq join(MailTagReq pre){
            for(Map.Entry<Long,Map<String,Object>> entry: pre.tagMap.entrySet()){
                tagMap.computeIfAbsent(entry.getKey(), k -> entry.getValue());
            }
            return this;
        }

        public void check(String channel){
            Set<Long> mailIds = tagMap.keySet();
            Map<Long, Integer> ios = meta.get(channel);
            for(Long mailId:mailIds){
                Integer template = ios.get(mailId);
                if(template!=null){
                    templateIds.add(String.valueOf(template) );
                }else{
                    unknownMailIds.add(String.valueOf(mailId));
                }
            }
        }

        public String toCsvString(){
            return String.join(",",userId,ownerId,getTemIds(),getUnknownMailIds());
        }

        private String getTemIds(){
            return String.join("|",templateIds);
        }

        private String getUnknownMailIds(){
            return String.join("|",unknownMailIds);
        }
    }

}
