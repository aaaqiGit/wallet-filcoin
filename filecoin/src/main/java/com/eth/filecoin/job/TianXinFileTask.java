//package com.eth.filecoin.job;
//
//import com.eth.filecoin.service.TianXinFilecoinService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import static com.eth.filecoin.utils.LocalDateUtils.getLocalDateTimeStr;
//
//
///**
// * @author ian
// * <p>
// * 后面会接 rocketMq 跑定时任务，job 直接消费 mq 和业务系统解耦；
// */
//@Component
//@Configuration
//@EnableScheduling
//@Slf4j
//public class TianXinFileTask {
//
//    @Autowired
//    private TianXinFilecoinService tianXinFilecoinService;
//
//    /**
//     * 充值 5min/次
//     */
//    @Scheduled(cron = "0 0/5 * * * ?")
//    private void filRechargeTask() {
//        log.info("天芯充值定时开始: " + getLocalDateTimeStr());
//        tianXinFilecoinService.filRechargeTask();
//        log.info("天芯充值定时结束: " + getLocalDateTimeStr());
//    }
//
//    /**
//     * 提现 10min/次
//     */
////    @Scheduled(cron = "0 0 0/5 * * ?")
//    //    @Scheduled(cron = "0/3 * * * * ?")
//    @Scheduled(cron = "0 0/10 * * * ?")
//    private void filTakeTask() {
//        log.info("天芯提现定时开始: " + getLocalDateTimeStr());
//        tianXinFilecoinService.filTakeTask();
//        log.info("天芯提现定时结束: " + getLocalDateTimeStr());
//    }
//
//
//    /**
//     * 归集同步提现表 1天/次  00:10:00 开始
//     * <p>
//     * 0 1-5 0 * * ? ，1min/次 一共 5 次,  1h 后还得再来一次
//     * 0 5/10 0-2 * * ?
//     * 0 0/1 * * * ? 归集 1天/次  00:00:00 开始
//     * 0 0 4 1/1 * ?  每天 凌晨 4 点
//     */
////    @Scheduled(cron = "0 1-5 0 * * ?")
////    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = "0 0 4 1/1 * ?")
//    private void filTake() {
//        log.info("天芯归集第二步定时开始: " + getLocalDateTimeStr());
//        tianXinFilecoinService.filTake();
//        log.info("天芯归集第二步定时结束: " + getLocalDateTimeStr());
//    }
//
//    /**
//     * 归集同步推送地址, 天芯 old
//     * 每天 凌晨 00:00 - 04:00  间推送
//     * 2021-05-19 00:00:00
//     * 2021-05-19 00:10:00
//     * 2021-05-19 00:20:00
//     * 2021-05-19 00:30:00
//     */
//    @Scheduled(cron = "0 0/10 0 1/1 * ?")
//    private void filFocusSynch() {
//        log.info("天芯归集同步定时开始: " + getLocalDateTimeStr());
//        tianXinFilecoinService.filFocusSynch();
//        log.info("天芯归集同步定时结束: " + getLocalDateTimeStr());
//    }
//}
