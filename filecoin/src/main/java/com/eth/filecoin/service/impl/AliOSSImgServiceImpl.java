//package com.eth.filecoin.service.impl;
//
//import com.aqi.file.utils.AliOSSUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * @author aqi
// * @date 2022/10/25 14:39
// */
//@Service
//@Primary
//public class AliOSSImgServiceImpl implements ISysFileService{
//
//    @Autowired
//    private AliOSSUtil aliOSSUtil;
//
//    @Override
//    public String uploadFile(MultipartFile file) throws Exception {
//        String route = aliOSSUtil.catalogue + "/" + file.getResource().getFilename();
//        aliOSSUtil.putObjectByte(file.getBytes(),route);
//        return aliOSSUtil.url + route;
//    }
//}
