package com.eth.filecoin.entity;



import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 城市对象 city
 * 
 * @author aqi
 * @date 2022-10-18
 */
@Data
public class City implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** 城市名称 */
    private String name;

    /** 省份id */
    private Integer pid;

    private List<Area> areas;
}
