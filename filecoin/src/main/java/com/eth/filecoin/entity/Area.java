package com.eth.filecoin.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 区域对象 area
 *
 * @author aqi
 * @date 2022-10-18
 */
@Getter
@Setter
public class Area implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 城市id
     */
    private Integer pid;
}
