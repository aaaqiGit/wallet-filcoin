package com.eth.filecoin.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 省份对象 province
 *
 * @author aqi
 * @date 2022-10-18
 */
@Data
public class Province implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * 省份名称
     */
    private String name;

    private List<City> cities;
}
