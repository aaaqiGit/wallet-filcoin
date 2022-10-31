package com.eth.filecoin.controller;

import static com.eth.filecoin.common.CodeConstants.FIL_ID;
import static com.eth.filecoin.common.CodeConstants.FIL_SECRET;
import static com.eth.filecoin.common.CodeConstants.FIL_TOKEN;

import com.eth.filecoin.admin.AuthenRequest;
import com.eth.filecoin.common.Md5TokenGenerator;
import com.eth.filecoin.common.Result;
import com.eth.filecoin.common.Results;
import com.eth.filecoin.utils.CacheRedis;
import com.eth.filecoin.utils.MD5Util;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: aqi
 * @Date: 2021/5/13 10:13
 * @Description: 公共
 */
@RestController
@RequestMapping("/api/oms/filecoin")
@Api(value = "公共", tags = "公共")
public class CommonController {

    @Autowired
    private Md5TokenGenerator md5TokenGenerator;
    @Autowired
    private CacheRedis cacheRedis;

    @PostMapping("/{filId}/auth")
    public Result auth(@PathVariable String filId, @RequestBody AuthenRequest request) {
        String md5 = MD5Util.md5(FIL_ID + request.getTimestamp() + FIL_SECRET);

        if (filId.equals(FIL_ID) && request.getSign().equals(md5)) {
            String token = md5TokenGenerator.generate(FIL_ID, FIL_SECRET);
            String _token = FIL_TOKEN + FIL_ID + token;
            //  24 小时
            cacheRedis.set(FIL_TOKEN, _token, 86400);
            return Results.success(_token);
        }
        return Results.success();
    }
}
