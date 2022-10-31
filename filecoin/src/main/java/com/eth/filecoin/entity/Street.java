package com.eth.filecoin.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * 街道对象 street
 *
 * @author aqi
 * @date 2022-10-18
 */
@Data
public class Street implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * 街道名称
     */
    private String name;

    /**
     * 区域id
     */
    private Integer pid;
}
