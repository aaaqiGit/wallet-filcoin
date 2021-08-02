package com.eth.filecoin.utils;

import cn.hutool.core.codec.Base32;
import cn.hutool.core.util.StrUtil;
import com.eth.filecoin.admin.Address;
import com.eth.filecoin.exception.AssertException;

public class AddressUtil {

  /**
   * secp256k1地址字节长度
   */
  private final static int addressLength = 21;

  public static Address initAddress(String addressStr) {
    if (StrUtil.isBlank(addressStr)) {
      throw new NullPointerException("addressStr 参数不能为空");
    }
    //去掉拼接的前两位
    String substring = addressStr.substring(2);
    //获取type
    String typeStr = addressStr.substring(1, 2);
    //获取网络类型
    String network = addressStr.substring(0, 1);
    int type = Integer.parseInt(typeStr);
    if (type != 1 && type != 2 && type != 3) {
      throw new AssertException("错误的地址类型");
    }

    byte[] addressBytes = new byte[addressLength];

    switch (type) {
      case 1:
        //secp256k1 地址类型
        addressBytes[0] = (byte) type;
        break;
      case 2:
      case 3:
        throw new AssertException("暂不支持的地址类型");
      default:
        throw new AssertException("错误地址类型");
    }

    System.arraycopy(Base32.decode(substring), 0, addressBytes, 1, 20);
    return Address.builder().address(addressStr)
        .bytes(addressBytes)
        .network(network).build();

  }
}
