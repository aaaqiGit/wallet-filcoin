package com.eth.filecoin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eth.filecoin.entity.DemoEntity;
import com.eth.filecoin.mapper.DemoMapper;
import com.eth.filecoin.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * @author: aqi
 * @Date: 2021/4/23 16:11
 * @Description:
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, DemoEntity> implements DemoService {

  @Override
  public void queryId() {

  }
}
