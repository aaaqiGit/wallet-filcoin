package com.eth.filecoin.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.eth.filecoin.admin.SysFile;
import com.eth.filecoin.common.Result;
import com.eth.filecoin.common.Results;
import com.eth.filecoin.service.SysFileService;
import com.eth.filecoin.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: aqi
 * @Date: 2021/12/22 10:40
 * @Description:
 */
@RestController
@RequestMapping(value = "/api/oss")
@Slf4j
public class AliOssController {
    @Autowired
    private SysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return Results.success(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return Results.failure(e.getMessage());
        }
    }
}
