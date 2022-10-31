package com.eth.filecoin.service.impl;

import com.eth.filecoin.entity.Area;
import com.eth.filecoin.entity.City;
import com.eth.filecoin.entity.Province;
import com.eth.filecoin.mapper.AreaMapper;
import com.eth.filecoin.mapper.CityMapper;
import com.eth.filecoin.mapper.ProvinceMapper;
import com.eth.filecoin.service.IProvinceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 省份Service业务层处理
 *
 * @author aqi
 * @date 2022-10-18
 */
@Service
public class ProvinceServiceImpl implements IProvinceService {
    @Resource
    private ProvinceMapper provinceMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private AreaMapper areaMapper;

    /**
     * 初始化省市区
     */
    @Override
    public List<Province> queryProvinceCityAreaAll() {
//        List<Province> provinces = provinceMapper.queryProvince();
        List<Province> provinces = Collections.emptyList();
        List<Province> provinceList = new ArrayList<>(16);
        for (Province province : provinces) {
            Province province1 = new Province();
            province1.setName(province.getName());
            // 获取城市数据
            List<City> citys = cityMapper.queryCitys(province.getId());
            List<City> cityList = new ArrayList<>(16);
            for (City city : citys) {
                City city1 = new City();
                city1.setName(city.getName());
                //获取区域数据
                List<Area> areas = areaMapper.queryAreas(city.getId());
                city1.setAreas(areas);
                cityList.add(city1);
            }
            province1.setCities(cityList);
            provinceList.add(province1);
        }
        return provinceList;
    }


}
