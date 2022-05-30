package org.example.zhc.custom.mail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class Table_sendMailTemplate {
	@Data
	public static class Record_sendMailTemplate{
			/**
			* 唯一ID
			*/
					public Integer Key;
			/**
			* 用户id
			*/
					public String userid;
		@Override
		public String toString() {
			return "Table_sendMailTemplate{" +
						", Key='" + Key + '\'' +
						", userid='" + userid + '\'' +
					'}';
		}
	}

//	String fileName = "sendMailTemplate.xlsx";
	String finalJsonPath = "sendMailTemplate.json";

	public List<Record_sendMailTemplate> configs = new ArrayList<>();

    public Map<String, Record_sendMailTemplate> configsMap = new HashMap<>();

    public void setPath(String path){
    	finalJsonPath = path;
	}
	/**
	* 加载数据
	*/
	public void load() {
		for(Map.Entry<String, Object> entry: getJsonObject().entrySet()){
			JSONObject value = (JSONObject)entry.getValue();
			Record_sendMailTemplate record = value.toJavaObject(Record_sendMailTemplate.class);
			configs.add(record);
		}
	}

    /**
     * 加载数据Map
     */
    public void loadMap() {
        for(Map.Entry<String, Object> entry: getJsonObject().entrySet()){
            JSONObject value = (JSONObject)entry.getValue();
            Record_sendMailTemplate record = value.toJavaObject(Record_sendMailTemplate.class);
            configsMap.put(entry.getKey(), record);
        }
    }

	/**
	 * 获取第i个数据
	 * @param i
	 * @return
	 */
	public Record_sendMailTemplate get(int i){
		return configs.get(i);
	}

	/**
	 * 获取数据的长度
	 * @return
	 */
	public int size(){
		return configs.size();
	}

	/**
	 * 根据key值获得数据
	 * @param Key
	 * @return
	 */
	public Record_sendMailTemplate getRecordByKey(int Key){
        if(!configsMap.isEmpty()){
            String key = Key + "";
            return configsMap.get(key);
        }
		return configs.stream()
                .filter(Record_sendMailTemplate -> Record_sendMailTemplate.Key == Key)
                .findFirst().orElse(null);
	}

	/**
	 * 根据key从小到大排序
	 */
	public void ascending(){
		configs.sort((o1, o2) -> o1.Key - o2.Key);
	}

	/**
	 * 根据key从大到小排序
	 */
	public void descending(){
		configs.sort((o1, o2) -> o2.Key - o1.Key);
	}

	/**
	 * 获得key值最大的对象
	 * @return
	 */
	public Record_sendMailTemplate getMaxKey(){
		return configs.stream().max(Comparator.comparingInt(Record_sendMailTemplate::getKey)).orElse(null);
	}

	/**
	 * 获得key最小的对象
	 * @return
	 */
	public Record_sendMailTemplate getMinKey(){
		return configs.stream().min(Comparator.comparingInt(Record_sendMailTemplate::getKey)).orElse(null);
	}

	/**
	 * 策略查找
	 * @param predicate
	 * @return
	 */
	public List<Record_sendMailTemplate> findByFilter(Predicate<? super Record_sendMailTemplate> predicate){
		return configs.stream().filter(predicate).collect(Collectors.toList());
	}

	private JSONObject getJsonObject(){
        String jsonPath =  finalJsonPath;
		String jsonStr = readFile(jsonPath);
		return JSON.parseObject(jsonStr);
	}

	private String readFile(String path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			InputStream inputStream = new FileInputStream(path);
			//设置字符编码为UTF-8，避免读取中文乱码
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
			// 通过BufferedReader进行读取
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr = laststr + tempString;
			}
			//关闭BufferedReader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					//不管执行是否出现异常，必须确保关闭BufferedReader
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return laststr;
	}
}