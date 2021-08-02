package com.eth.filecoin.admin;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: aqi
 * @Date: 2021/4/29 09:49
 * @Description:
 */
@Data
public class FilfoxDto implements Serializable {

    private static final long serialVersionUID = -1233747774400120496L;
    private Integer totalCount;

    private String methods;

    private FilfoxInfo message;

}
