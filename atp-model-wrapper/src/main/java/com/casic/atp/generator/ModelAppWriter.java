package com.casic.atp.generator;

import com.casic.atp.model.ATPEnvironment;
import com.casic.atp.model.ATPModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static freemarker.template.TemplateExceptionHandler.*;

/**
 * 服务化封装APP工程FreeMarker模板生成器
 * 1. 默认在web工程model-apps目录下生成model_api_server.py工程文件
 * 2. 通过@ModeGenerator调用@ModelAPPWriter.write()方法生成
 */
public class ModelAppWriter {
    // 创建Configuration实例，指定版本
    public static Configuration configuration = new Configuration(Configuration.getVersion());

    static{
        try {
            // 指定configuration对象模板文件存放的路径,默认classpath路径
            configuration.setDirectoryForTemplateLoading(new File(Thread.currentThread().getContextClassLoader().getResource("").getFile()));
            // 设置config的默认字符集，一般是UTF-8
            configuration.setDefaultEncoding("UTF-8");
            // 设置错误控制器
            configuration.setTemplateExceptionHandler(RETHROW_HANDLER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Map map, String modelName) throws IOException, TemplateException {
        // 获取模版
        Template template = configuration.getTemplate("model_api_server.ftl");
        // 创建一个Writer对象，指定生成的文件保存的路径及文件名
        String modelAppsDir = ATPEnvironment.getRoot() + ATPEnvironment.tmpDir;
        if(!new File(modelAppsDir).exists()){
            new File(modelAppsDir).mkdir();
        }
        System.out.println("modeAppDir=" + modelAppsDir);
        Writer writer = new FileWriter(new File(modelAppsDir + "/" + modelName + "_server.py"));
        template.process(map, writer);
    }



    public static void main(String[] args){
        //模拟Model
        ATPModel model = new ATPModel();
        model.setDefGetInput("def get_input(data):\n" +
                "    predict_data = list(data.values())[0]  # 默认获取第一项数据\n" +
                "    return np.array(predict_data)");
        model.setType(ATPModel.MODEL_JOBLIB);
        model.setAppFilePath("iris.model");
        model.setName("iris");
        ModelAppWriter writer = new ModelAppWriter();
        try {
            writer.write(model.toMap(), model.getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }


}
