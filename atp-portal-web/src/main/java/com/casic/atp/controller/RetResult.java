package com.casic.atp.controller;

/**
 * @Description: 返回对象实体
 * @author zouping
 */
public class RetResult<T> {
   private final static String SUCCESS = "success";

   public int code;
 
   private String msg;
 
   private T data;
 
   public RetResult<T> setCode(RetCode retCode) {
      this.code = retCode.code;
      return this;
   }
 
   public int getCode() {
      return code;
   }
 
   public RetResult<T> setCode(int code) {
      this.code = code;
      return this;
   }
 
   public String getMsg() {
      return msg;
   }
 
   public RetResult<T> setMsg(String msg) {
      this.msg = msg;
      return this;
   }
 
   public T getData() {
      return data;
   }
 
   public RetResult<T> setData(T data) {
      this.data = data;
      return this;
   }

   public static <T> RetResult<T> OK() {
      return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS);
   }

   public static <T> RetResult<T> OK(T data) {
      return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setData(data);
   }

   public static <T> RetResult<T> ERROR(String message) {
      return new RetResult<T>().setCode(RetCode.FAIL).setMsg(message);
   }

   public static <T> RetResult<T> RES(int code, String msg) {
      return new RetResult<T>().setCode(code).setMsg(msg);
   }

   public static <T> RetResult<T> RES(int code, String msg, T data) {
      return new RetResult<T>().setCode(code).setMsg(msg).setData(data);
   }
}