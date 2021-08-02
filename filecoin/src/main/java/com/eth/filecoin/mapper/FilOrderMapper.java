package com.eth.filecoin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eth.filecoin.entity.FilOrder;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilOrderMapper extends BaseMapper<FilOrder> {
    int deleteByPrimaryKey(String id);

    int insert(FilOrder record);

    int insertSelective(FilOrder record);

    FilOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FilOrder record);

    int updateByPrimaryKey(FilOrder record);

    FilOrder selectCid(String hash);

    List<FilOrder> selectByFromAddress(String from);

    List<FilOrder> selectByTo(String to);
}