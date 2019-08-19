package com.casic.atp.controller;

/**
 * @Description: 响应码枚举，参考HTTP状态码的语义
 * @author zouping
 */
public enum RetCode {
 
   // 成功
   SUCCESS(200),
 
   // 失败
   FAIL(400),
 
   // 未认证（签名错误）
   UNAUTHORIZED(401),
 
   // 接口不存在
   NOT_FOUND(404),
 
   // 服务器内部错误
   INTERNAL_SERVER_ERROR(500);
 
   public int code;
 
   RetCode(int code) {
      this.code = code;
   }
}