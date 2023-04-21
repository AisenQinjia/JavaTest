package org.example.zhc.serial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 广播消息通用结构体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastMessage<T extends Serializable> implements Serializable {
    /**
     * 消息所属的event
     */
    String event;
    /**
     * 消息
     */
    T msg;
}
