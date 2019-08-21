package com.casic.atp.model;

import java.io.File;

/**
 * @author zouping
 * 模型运行的容器环境和一些默认的参数
 * 该环境主要运行服务和执行python机器学习算法，此环境有两个服务
 * 1. 默认接收shell命令的环境服务，端口为5000，运行atp-shell-api的python工程，通过api可以远程执行环境下的shell和python命令
 * (1)/shell/<cmd>,执行linux命令
 * (2)/python/<file_path>,执行某个python文件
 * 2. 运行算法封装的API服务，通过atp-shell-api的api远程将算法工程打包上传至环境，启动API服务，默认运行端口为5001
 * (1) 如果API服务正在运行，会自动重启服务
 * 一个项目（模型）对应一个运行环境
 */
public class ATPEnvironment {
    //动态生成的环境IP
    private String ip = "localhost";
    //默认的环境端口
    public static String PORT_DEFAULT = "5000";
    //生成的api访问端口，默认为5001
    public static String PORT_API = "5001";
    //API服务运行的路径，默认在atp-shell-api/model/${modelName}_server.py文件
    private String appFilePath;
    //模型临时保存的文件目录，默认在atp-portal-web/model-apps下
    public static String tmpDir = "/model-apps";
    //模型在容器环境的根目录
    public static String APPRoot = "/opt/atp-shell-api";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 返回工程根目录
     * @return
     */
    public static String getRoot(){
        return new File(new File(Thread.currentThread().getContextClassLoader()
                .getResource("").getPath()).getParent()).getParent();
    }

    /**
     * 获取shell命令url
     * @return
     */
    public String getShellUrl(){
        return "http://" + this.getIp() + ":" + this.PORT_DEFAULT;
    }

    /**
     * 获取api命令url
     * @return
     */
    public String getAPIUrl(){
        return "http://" + this.getIp() + ":" + this.PORT_API;
    }

    /**
     * 发布工程文件
     * @return
     */
    public String getDeployCmd(){
        return getShellUrl() + "/upload_app";
    }

    /**
     * 启动服务工程
     * @param pythonFile
     * @return
     */
    public String getStartCmd(String pythonFile){
        return getShellUrl() + "/python/nohup/" + pythonFile;
    }
}
