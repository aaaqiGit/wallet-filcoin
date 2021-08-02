
# 区块链钱包

## 参考 github 开源项目

## 功能
1. 归集
2. 备付
3. 转账


## 第三方对接接口文档
### 鉴权接口

```url
POST http://localhost:8088/api/oms/filecoin/{filId}/auth
```

### 使用场景
用于获取 token，每次调用业务接口的 HTTP 请求都需要写入此 token 作为校验

### 备注
token 有效期为 24 小时，过期后需再次获取；建议定时更新 token，比如每 20 小时更新一次
所有请求 token 及其他参数都写在 header 中

### 请求数据

#### path 域

|  名称   | 类型 |  可空  |默认  |描述  |
| :-------: | :---------: | :---------: |:---------: |:---------: |
|  filId   |    String    | N | | 提供方给予 |

#### body 域(JSON格式)
|  名称   | 类型 |  可空  |默认  |描述  |
| :-------: | :---------: | :---------: |:---------: |:---------: |
|  filKey   |    String    | N |提供方给予 | 调用方专用 key  |
|  timestamp   |    String    | N | 当前时间 | Unix 毫秒时间戳 |
|  sign   |    String    | N |MD5-32(filKey+timestamp+filSecret) | MD5 加密，32 位小写 |


#### 返回数据:

200 返回样例:
```
{
    "code": 200,
    "data": "FIL_TOKEN_2221c5d1d9ebd2fa24cd229da7be955c406",
    "msg": "操作成功"
}
```

1001 返回样例:
```
{
    "msg": "token verification fail",
    "code": 1001,
    "data": ""
}
```

















