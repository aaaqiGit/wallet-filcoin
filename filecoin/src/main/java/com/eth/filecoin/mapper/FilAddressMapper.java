package com.eth.filecoin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eth.filecoin.entity.FilAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilAddressMapper extends BaseMapper<FilAddress> {
    int deleteByPrimaryKey(String id);

    @Override
    int insert(FilAddress record);

    int insertSelective(FilAddress record);

    FilAddress selectByPrimaryKey(String id);

    FilAddress selectByAddress(String address);

    int updateByPrimaryKeySelective(FilAddress record);

    int updateByPrimaryKey(FilAddress record);
}