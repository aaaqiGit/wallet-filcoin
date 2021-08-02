package com.eth.filecoin.admin;

import java.io.Serializable;
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
public class Address implements Serializable {
    private static final long serialVersionUID = 3063602660728197524L;
    private String network;
    private String address;
    private byte[] bytes;

}
