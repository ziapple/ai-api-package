package com.casic.atp.model;


import com.casic.atp.generator.SKModelGenerator;
import com.casic.atp.util.HttpUtils;

/**
 * 模型服务
 */
public class ModelService {

    /**
     * 获取模型文件
     * 1. 从atp-shell-api中获取模型文件
     * @param ip:容器的ip地址
     */
    public String getModel(String ip){
        // HttpUtils.sendHttpsGet(ip + /get_model");
        return "iris.model";
    }


    /**
     * 保存模型,持久化存储
     */
    public void saveModel(Model model){

    }

    /**
     * 生成沙盒
     * 生成模型调用的python文件， 该文件为flask工程，直接提供模型调用API服务
     * @param model
     */
    public void generate(Model model){
        SKModelGenerator generator = new SKModelGenerator();
        generator.generate(model);
    }

    /**
     * 打包工程进容器
     * 调用ip:port/upload/
     * @param filePath 要打包的工程文件路径
     */
    public void deploy(String ip, String filePath){
        // HttpUtils.sendHttpsPost(ip + "/upload", null, null, false);

    }

    /**
     * 启动工程
     * @param filePath
     */
    public void start(String ip, String filePath){
        HttpUtils.sendHttpsPost(ip + "/upload", null, null, false);
    }
}
