package com.eth.filecoin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.eth.filecoin.entity.DemoEntity;


public interface DemoService extends IService<DemoEntity> {

  void queryId();
}
