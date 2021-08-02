package com.eth.filecoin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eth.filecoin.entity.FilFocus;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilFocusMapper extends BaseMapper<FilFocus> {
    int deleteByPrimaryKey(String id);

    @Override
    int insert(FilFocus record);

    int insertSelective(FilFocus record);

    FilFocus selectByPrimaryKey(String id);

    List<FilFocus> selectByFromAddress(String fromAddress);

    int updateByPrimaryKeySelective(FilFocus record);

    int updateByPrimaryKey(FilFocus record);
}