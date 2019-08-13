package com.casic.atp.generator;

import com.casic.atp.model.Model;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SKModelGenerator implements ModelGenerator {
    // 模型对象
    private Model model;
    // 模型构建Map
    Map<String, Object> map = new HashMap<String, Object>();

    /**
     * 生成SKLearn的模型
     * @param model
     */
    public void generate(Model model){
        //根据模型配置生成可执行的python文件
        this.model = model;
        ModelAppWriter appWriter = new ModelAppWriter();
        //构建模型APP文件
        this.buildImport()
                .buildFlaskApp()
                .buildModelAPI()
                .buildMain();
        try {
            appWriter.write(map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }


    /**
     * 构建Import部分
     * 目前模型支持两种类型模型导入,joblib,keras
     * 暂不用模板生成，直接引入
     * from sklearn.externals import joblib
     * @return
     */
    public SKModelGenerator buildImport(){
        return this;
    }

    /**
     * 构建App对象，不用模板生成
     * app = Flask(__name__)
     * @return
     */
    public SKModelGenerator buildFlaskApp(){
        return this;
    }

    /**
     * 构建模型API部分，输入输出，解析函数在此定义，模型调用也在此定义
     * 分为三部分
     * 1.生成输入代码
     * 2.生成读取模型文件代码，预测代码
     * 3.生成输出代码
     * @return
     */
    public SKModelGenerator buildModelAPI(){
        return buildModelAPI_Input().buildModelAPI_LoadModel().buildModelAPI_Output();
    }

    public SKModelGenerator buildModelAPI_Input(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("")
                .append("")
                .append("");
        map.put("data_input", buffer.toString());
        return this;
    }

    public SKModelGenerator buildModelAPI_LoadModel(){
        return this;
    }

    public SKModelGenerator buildModelAPI_Output(){
        return this;
    }

    /**
     * 构建主函数
     * @return
     */
    public SKModelGenerator buildMain(){
        return this;
    }
}
