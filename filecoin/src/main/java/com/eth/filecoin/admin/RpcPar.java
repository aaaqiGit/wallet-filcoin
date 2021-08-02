package com.eth.filecoin.admin;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcPar implements Serializable {
    private static final long serialVersionUID = 487079608019000310L;
    private String jsonrpc;
    private Integer id;
    private String method;
    private List<Object> params;
}
