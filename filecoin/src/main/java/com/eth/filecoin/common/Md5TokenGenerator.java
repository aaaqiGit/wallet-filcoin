package com.eth.filecoin.common;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author zhangqi
 * @create: 2018-11-16 15:17
 */
@Component
public class Md5TokenGenerator implements TokenGenerator {

    @Override
    public String generate(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
