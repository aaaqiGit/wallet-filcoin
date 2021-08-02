package com.eth.filecoin.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eth.filecoin.admin.RpcPar;
import com.eth.filecoin.common.FilecoinCnt;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import net.dreamlu.mica.http.BaseAuthenticator;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.ResponseSpec;
import okhttp3.Authenticator;

/**
 * @Auther: zhangqi
 * @Date: 2021/4/20 20:06
 * @Description:
 */
public class JWTUtil {

  /**
   * 过期时间5分钟
   */
  private static final long EXPIRE_TIME = 5 * 60 * 1000;

  /**
   * 校验token是否正确
   *
   * @param token     密钥
   * @param secret    密码
   * @param projectId 项目 id
   * @return 是否正确
   */
  public static boolean verify(String token, String projectId, String secret) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm)
//          .withClaim("username", username)
          .withIssuer("PROJECTID")
          .build();
      DecodedJWT jwt = verifier.verify(token);
      return true;
    } catch (Exception exception) {
      return false;
    }
  }

  /**
   * 获得token中的信息无需secret解密也能获得
   *
   * @return token中包含的用户名
   */
  public static String getUsername(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      return jwt.getClaim("projectId").asString();
    } catch (JWTDecodeException e) {
      return null;
    }
  }

  /**
   * 生成签名,5min后过期
   *
   * @param projectId 项目 id
   * @param secret    密码
   * @return 加密的token
   */
  public static String sign(String projectId, String secret) {
    Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
    Algorithm algorithm = Algorithm.HMAC256(secret);
    // 附带username信息
    return JWT.create()
//        .withClaim("username", username)
//        .withIssuer(PROJECTID)
        .withJWTId("PROJECTID")
        .withExpiresAt(date)
        .sign(algorithm);
  }

  public static void main(String[] args) {

//    Optional<String> opt = HttpRequest.post(URI.create("https://www.baidu.com"))
//        .bodyString("Important stuff")
//        .formBuilder()
//        .add("a", "b")
//        .execute()
//        .onSuccessOpt(ResponseSpec::asString);
//    System.out.println("result: " + opt.get());
//

    Authenticator authenticator = new BaseAuthenticator("1rPvg4me17b661GZtUkJb9AwlHJ","843e065a5f1802b8826943ccfd98700d");
    List<Object> params = new ArrayList<>();
    params.add("f1acsnnhhtibfjhfpjxr74zykoejqe6u6zq4z64ca");
    RpcPar par = RpcPar.builder().id(1)
        .jsonrpc("2.0")
        .method(FilecoinCnt.GET_BALANCE)
        .params(params).build();
    Optional<String> resultNJ = HttpRequest.post(URI.create(
        "https://1rPvg4me17b661GZtUkJb9AwlHJ:843e065a5f1802b8826943ccfd98700d@filecoin.infura.io"))
        .addHeader("Content-Type","application/json")
        .authenticator(authenticator)
        .bodyJson(par)
        .execute()
        .onSuccessOpt(ResponseSpec::asString);
    System.out.println("result: " + resultNJ.get());

////    String token = sign("ian", SECRET);
////    System.out.println(token);
//
//    String tokenFlag = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImV4cCI6MTYxODkyODUzOSwidXNlcm5hbWUiOiJpYW4ifQ.1neDQ7URSyFNwPeBuEKvb1KHvfj7wzn5xJ9UVF4qQJM";
//    boolean flag = verify(tokenFlag, "ian", SECRET);
////    boolean flag = verify(token, "ian", SECRET);
//    System.out.println(flag);
//
//    String username = getUsername(tokenFlag);
//    System.out.println(username);
  }
}
