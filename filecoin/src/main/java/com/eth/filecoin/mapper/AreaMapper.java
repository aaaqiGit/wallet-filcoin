package com.eth.filecoin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eth.filecoin.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 区域Mapper接口
 *
 * @author aqi
 * @date 2022-10-18
 */
public interface AreaMapper extends BaseMapper<Area> {
    /**
     * 市 id 查询所有区
     *
     * @param id
     * @return
     */
    List<Area> queryAreas(@Param("id") Integer id);
}
