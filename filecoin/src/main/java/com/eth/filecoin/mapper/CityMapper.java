package com.eth.filecoin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eth.filecoin.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 城市Mapper接口
 *
 * @author aqi
 * @date 2022-10-18
 */
@Mapper
public interface CityMapper extends BaseMapper<City> {
    /**
     * 根据省查询所有市
     *
     * @param id
     * @return
     */
    List<City> queryCitys(@Param("id") Integer id);
}
