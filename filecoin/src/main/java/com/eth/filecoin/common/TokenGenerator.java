package com.eth.filecoin.common;

import org.springframework.stereotype.Component;

/**
 * @author zhangqi
 * @create: 2018-11-16 15:17
 */
@Component
public interface TokenGenerator {

    public String generate(String... strings);

}
