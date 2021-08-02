package com.eth.filecoin.admin.result;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendResult implements Serializable {
    private static final long serialVersionUID = -7170795133277476936L;
    /**
     * 交易CID
     */
    @ApiModelProperty(value = "cid", required = true, example = "")
    private String cid;
}
