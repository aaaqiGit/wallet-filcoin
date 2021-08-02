package com.eth.filecoin.exception;


import com.eth.filecoin.common.SystemCode;

/**
 * 业务断言错误返回类
 * @author liujuncai@78dk.com
 * @date 2018/11/23 17:02
 **/
public class AssertException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2154706374789597636L;
	/**
     * 返回码
     */
    private String code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;

    public AssertException(String code, String msg){
        this.code = code;
        this.msg = msg;
        this.data = "";
    }
    public AssertException(String code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
  public AssertException(String code){
    this.code = code;
    this.msg = SystemCode.FAILURE.getDesc();
    this.data = "";
  }

  public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
