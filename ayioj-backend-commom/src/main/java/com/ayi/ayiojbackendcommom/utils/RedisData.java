package com.ayi.ayiojbackendcommom.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * className:RedisData
 * Package:com.ayi.ayioj.utils
 * Description: TODO
 *
 * @Date: 2024/1/16 16:49
 * @Author:hjy
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;  //存储过期时间
    private Object data;  //存储对象数据，例如Shop对象
}
