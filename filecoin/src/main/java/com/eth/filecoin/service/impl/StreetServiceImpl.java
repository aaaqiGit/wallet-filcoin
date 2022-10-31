package com.eth.filecoin.service.impl;

import com.eth.filecoin.entity.Street;
import com.eth.filecoin.mapper.StreetMapper;
import com.eth.filecoin.service.IStreetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 街道Service业务层处理
 * 
 * @author aqi
 * @date 2022-10-18
 */
@Service
public class StreetServiceImpl implements IStreetService {
    @Resource
    private StreetMapper streetMapper;

}
