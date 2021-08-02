package com.eth.filecoin.utils;

import java.text.MessageFormat;
import org.springframework.stereotype.Component;

/**
 * @author hyk
 * @date 2020/12/30
 * @description
 */

@Component
public class RedisKeyGenerator {

  private static final String PLAN_SPLIT_2_TICKET_LOCK = "lock:planSplitToTicket:{0}";
  private static final String PLAN_QUERY_TICKET_LOCK = "lock:queryTicket:{0}";
  private static final String PLAN_SEND_TICKET_LOCK = "lock:sendTicket:{0}";

  /**
   * 构建拆票锁定key
   */
  public String buildPlanSplitToTicketKey(Long planId) {
    // 使用 MessageFormat 格式化数值型一般先toString，不然会有千分符等多余符号
    return MessageFormat.format(PLAN_SPLIT_2_TICKET_LOCK, planId.toString());
  }

  public String buildQueryTicketKey(Long planId) {
    return MessageFormat.format(PLAN_QUERY_TICKET_LOCK, planId.toString());
  }

  public String buildSendTicketKey(Long planId) {
    return MessageFormat.format(PLAN_SEND_TICKET_LOCK, planId.toString());
  }
}
