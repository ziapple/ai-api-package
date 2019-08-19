package com.casic.atp.model;


import com.casic.atp.generator.KerasModelGenerator;
import com.casic.atp.generator.ModelGenerator;
import com.casic.atp.generator.SKModelGenerator;
import com.casic.atp.util.HttpUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 模型服务
 * 1. createEnvironment, 启动模型容器运行环境，返回容器对象
 * 2. enterEnvironment, 进入容器运行环境
 * 3. saveModel(), 持久化保存模型
 * 4. generate(),生成模型app运行工程，默认生成在atp-portal-web根目录下的model-apps/${modelName}_server.py文件
 * 5. deploy(), 发布app工程到环境
 * 6. start(), 启动app工程
 */
@Service
public class ATPModelService {
    //模拟模型持久化
    Map<String, ATPModel> modelDB = new HashMap<String, ATPModel>();

    /**
     * 启动模型容器运行环境，返回容器对象
     * @return ATPEnvironment 容器的ip等信息
     */
    public ATPEnvironment createEnvironment(){
        //TODO 启动环境
        ATPEnvironment environment = new ATPEnvironment();
        environment.setIp("localhost");
        return environment;
    }

    /**
     * 保存模型定义,持久化存储
     * TODO, 保存到数据库
     */
    public void saveModel(ATPModel model){
        modelDB.put(model.getName(), model);
    }

    /**
     * 从数据库获取模型定义
     * @param modelName
     */
    public ATPModel getModel(String modelName){
        return modelDB.get(modelName);
    }

    /**
     * 生成沙盒
     * 生成模型调用的python文件， 该文件为flask工程，直接提供模型调用API服务
     * @param model
     */
    public void generate(ATPModel model){
        ModelGenerator generator = null;
        if(model.getType().equals(ATPModel.MODEL_JOBLIB))
            generator = new SKModelGenerator();
        else if(model.getType().equals(ATPModel.MODEL_KERAS))
            generator = new KerasModelGenerator();
        generator.generate(model);
    }

    /**
     * 打包工程进容器
     * 调用ip:port/upload/
     * @param modelName 模型文件
     */
    public void deploy(String modelName) throws IOException {
        ATPModel model = getModel(modelName);
        String url = model.getEnvironment().getDeployCmd();
        // 绝对路径
        String filePath = ATPEnvironment.getRoot() + model.getLocalFilePath();
        HttpUtils.upload( url, filePath);
        //更新远程执行文件路径
        model.setFilePath("model/" + model.getAppFileName());
        modelDB.put(model.getName(), model);
    }

    /**
     * 启动工程
     * @param filePath
     */
    public void start(String modelName) throws Exception {
        ATPModel model = getModel(modelName);
        //处理成get能够传递的参数
        String filePath = model.getFilePath().replaceAll("/", "!");
        HttpUtils.get(model.getEnvironment().getStartCmd(filePath));
    }
}