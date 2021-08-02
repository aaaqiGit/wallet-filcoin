package com.eth.filecoin.controller;

import com.eth.filecoin.admin.EasySendDto;
import com.eth.filecoin.admin.FilAddressDto;
import com.eth.filecoin.admin.result.WalletResult;
import com.eth.filecoin.common.Result;
import com.eth.filecoin.common.Results;
import com.eth.filecoin.service.FilecoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: aqi
 * @Date: 2021/4/23 16:57
 * @Description:
 */
@Slf4j
@Api(value = "filecoin", tags = "filecoin钱包")
@RestController
@RequestMapping(value = "/api/oms/filecoin")
public class FilecoinWalletController {

    @Autowired
    private FilecoinService filecoinService;

    @ApiOperation(value = "添加钱包")
    @PostMapping("/createWallet")
    public Result<WalletResult> createWallet(@RequestBody @Valid FilAddressDto request) {
        WalletResult result = filecoinService.createWallet(request);
        return Results.success(result);
    }

    @ApiOperation(value = "提现")
    @PostMapping("/take")
    public Result take(@RequestBody @Valid EasySendDto request) {
        Boolean take = filecoinService.take(request);
        if (take) {
            return Results.success(true);
        }
        return Results.failure("余额不足");
    }

}
