package org.example.zhc.custom.mail;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.FileUtil.reader;

public class MailApp {
    private static final String IOS_TABLE = "韩信补发IOS.ignore";
    private static final String IOS_LOG = "ios0519.ignore";
    private static final String ANDROID_TABLE = "韩信补发安卓.ignore";
    private static final String ANDROID_LOG = "android0519.ignore";
    private static final Pattern successPattern = Pattern.compile("SEND SUCCESS!.+(1000004_\\d+)");
    @Test
    public void readUserId() throws IOException {
        Set<String> allUser = new HashSet<>();
        Table_sendMailTemplate table = new Table_sendMailTemplate();
        table.setPath(ANDROID_TABLE);
        table.load();
        List<Table_sendMailTemplate.Record_sendMailTemplate> records = table.configs;
        for(Table_sendMailTemplate.Record_sendMailTemplate record: records){
            allUser.add(record.userid);
        }
        Set<String> allSendUser = new HashSet<>();
        List<String> repeatedUser = new ArrayList<>();
        BufferedReader br = reader(ANDROID_LOG);
        String line;
        while ((line= br.readLine())!=null){
            Matcher matcher = successPattern.matcher(line);
            if(matcher.find()){
                String userId = matcher.group(1);
                if(!allSendUser.add(userId)){
                    repeatedUser.add(userId);
                }
            }
        }
        allUser.removeAll(allSendUser);
        System.out.println("repeated user: " + repeatedUser.size() + repeatedUser);
        System.out.println("lost user: "+ allUser.size() + allUser);
    }
}
