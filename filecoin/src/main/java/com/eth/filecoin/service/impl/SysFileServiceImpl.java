package com.eth.filecoin.service.impl;

import com.eth.filecoin.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author aqi
 * @date 2022/10/31 15:04
 */
@Service
public class SysFileServiceImpl implements SysFileService {
    @Autowired
    private AliOSSUtil aliOSSUtil;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String route = aliOSSUtil.catalogue + "/" + file.getResource().getFilename();
        aliOSSUtil.putObjectByte(file.getBytes(),route);
        return aliOSSUtil.url + route;
    }
}
