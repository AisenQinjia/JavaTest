package org.example.zhc.util.zhc.spring.redis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class StreamListenerExample implements StreamListener<String, MapRecord<String, String, byte[]>> {
    @Override
    public void onMessage(MapRecord<String, String, byte[]> entries) {
        log.info("entries: stream:{} id:{} val:{} ",entries.getStream(),entries.getId(),entries.getValue());
    }
}
