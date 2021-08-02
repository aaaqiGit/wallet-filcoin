package com.eth.filecoin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eth.filecoin.entity.FilTake;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilTakeMapper extends BaseMapper<FilTake> {
    int deleteByPrimaryKey(String id);

//    @Override
//    int insert(FilTake record);

    int insertSelective(FilTake record);

    FilTake selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FilTake record);

    int updateByPrimaryKey(FilTake record);
}