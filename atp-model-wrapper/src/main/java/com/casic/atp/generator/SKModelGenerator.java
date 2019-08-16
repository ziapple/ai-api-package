package com.casic.atp.generator;

import com.casic.atp.model.Model;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zouping
 * @date 2019-08-13
 * 数据生成器，按以下顺序生成模型
 * seq    name           method           variable               eg.
 * (1)  引入包文件     buildImport        $def_import          import *
 * (2)  定义app工程    buildFlaskApp      $def_app            app = Flask()
 * (3)  定义输入函数   buildInputParser   $def_get_input       def get_input()
 * (4)  定义输出结果   buildOutputParser  $def_get_output      def get_output(data)
 * (5)  定义api函数    buildModelAPI     $def_model_api       def model_api()
 * (5.1)定义模型       buildGetModel     $get_model           model = get_model()
 * (5.2)获取输入数据   buildGetInput     $get_input           get_input()
 * (5.3)加载模型      buildLoadModel    $load_input          load_model(model)
 * (5.4)预测模型      buildPredictModel  $predict_model       predict_model(data)
 * (5.5)返回输出结果  buldGetOutPut     $return_output        return get_output()
 * (6) 主执行文件     buildMain         $def_main            if __main__ == "main"
 */
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
                .buildInputParser()
                .buildOutputParser()
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
     * (1)构建Import部分
     * 目前模型支持两种类型模型导入,joblib,keras
     * 暂不用模板生成，直接引入
     * from sklearn.externals import joblib
     * @return
     */
    public SKModelGenerator buildImport(){
        //自定义引入包,eg. import os
        map.put("def_import", "");
        return this;
    }

    /**
     * (2)构建App对象，不用模板生成
     * app = Flask(__name__)
     * @return
     */
    public SKModelGenerator buildFlaskApp(){ return this; }

    /**
     * (3)创建输入解析器
     * @TODO 增加解析函数代码
     * @return
     */
    public SKModelGenerator buildInputParser(){
        map.put("def_get_input", "");
        return this;
    }

    /**
     * (4)创建输入解析器
     * @return
     */
    public SKModelGenerator buildOutputParser(){
        map.put("def_get_output", "");
        return this;
    }

    /**
     * (5)构建模型API部分，输入输出，解析函数在此定义，模型调用也在此定义,分为五部分
     * (5.1)    model = get_model()  //定义模型==buildGetModel
     * (5.2)    data = get_input()   //获取输入数据==buildGetInput
     * (5.3)    load_model(model)    //加载模型==buildLoadModel
     * (5.4)    predict_model(data)  //预测模型==buildPredictModel
     * (5.5)    return get_output()  //返回输出结果==buldGetOutPut
     * @return
     */
    public SKModelGenerator buildModelAPI(){
        map.put("get_model", buildGetModel());
        map.put("get_input", buildGetInput());
        map.put("load_model", buildLoadModel());
        map.put("predict_model", buildPredictModel());
        map.put("get_output", buldGetOutPut());
        return this;
    }

    /**
     * (6)创建输入解析器
     * 此部分不用生成
     * @return
     */
    public SKModelGenerator buildMain(){
        map.put("def_main", "");
        return this;
    }

    /**
     * (5.1)定义模型==buildGetModel
     * @return
     */
    public String buildGetModel(){
        return "";
    }

    /**
     * 获取输入数据==buildGetInput
     * @return
     */
    public String buildGetInput(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("data = request.get_data()")
                .append("if not data:\n" +
                        "  return jsonify(\"请输入模型参数\")")
                .append("json_data = json.loads(data.decode('utf-8'))");
        return buffer.toString();
    }

    /**
     * （5.3）加载模型==buildLoadModel
     * @return
     */
    public String buildLoadModel(){
        return "";
    }

    /**
     * （5.4）预测模型==buildPrecitModel
     * @return
     */
    public String buildPredictModel(){
        return "";
    }

    /**
     * （5.5）返回输出结果==buldGetOutPut
     * @return
     */
    public String buldGetOutPut(){
        return "";
    }

}
