package com.eth.filecoin.service;



import com.eth.filecoin.entity.Province;

import java.util.List;

/**
 * 省份Service接口
 *
 * @author aqi
 * @date 2022-10-18
 */
public interface IProvinceService {

    /**
     * 初始化省市区
     * @return 结果
     */
    List<Province> queryProvinceCityAreaAll();


}
