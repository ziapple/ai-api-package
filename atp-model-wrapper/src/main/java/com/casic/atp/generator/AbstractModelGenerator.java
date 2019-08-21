package com.casic.atp.generator;

import com.casic.atp.model.ATPModel;
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
public class AbstractModelGenerator implements ModelGenerator {
    // 模型对象
    protected ATPModel model;
    // 模型构建Map
    protected Map<String, Object> map = new HashMap<String, Object>();

    /**
     * 生成SKLearn的模型
     * @param model
     */
    public void generate(ATPModel model) throws IOException, TemplateException {
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

        appWriter.write(map, model.getName());
        //更新模型服务本地文件路径
        model.setAppFilePath(model.getTmpAPPFilePath());
    }


    /**
     * (1)构建Import部分
     * 目前模型支持两种类型模型导入,joblib,keras
     * 暂不用模板生成，直接引入
     * from sklearn.externals import joblib
     * @return
     */
    private AbstractModelGenerator buildImport(){
        if(buildImportLine()!=null)
            map.put("def_import", buildImportLine());
        return this;
    }

    public String buildImportLine(){
        return null;
    }

    /**
     * (2)构建App对象，不用模板生成
     * app = Flask(__name__)
     * @return
     */
    private AbstractModelGenerator buildFlaskApp(){
        if(buildFlaskAppLine()!=null)
            map.put("def_app", buildFlaskAppLine());
        return this;
    }

    public String buildFlaskAppLine(){
        return null;
    }

    /**
     * (3)创建输入解析器
     * @TODO 增加解析函数代码
     * @return
     */
    private AbstractModelGenerator buildInputParser(){
        if(buildInputParserLine()!=null)
            map.put("def_get_input", buildInputParserLine());
        return this;
    }

    public String buildInputParserLine(){
        return  model.getDefGetInput();
    }

    /**
     * (4)创建输入解析器
     * @return
     */
    private AbstractModelGenerator buildOutputParser(){
        if(this.buildOutputParserLine()!=null)
            map.put("def_get_output", this.buildOutputParserLine());
        return this;
    }

    public String buildOutputParserLine(){
        return  model.getDefGetOutput();
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
    private AbstractModelGenerator buildModelAPI(){
        if(buildGetModel()!=null)
            map.put("get_model", buildGetModel());
        if(buildGetInput()!=null)
            map.put("get_input", buildGetInput());
        if(buildLoadModel()!=null)
            map.put("load_model", buildLoadModel());
        if(buildPredictModel()!=null)
            map.put("predict_model", buildPredictModel());
        if(buldGetOutPut()!=null)
            map.put("get_output", buldGetOutPut());
        return this;
    }

    /**
     * (5.1)定义模型==buildGetModel
     * @return
     */
    public String buildGetModel(){
        return null;
    }

    /**
     * 获取输入数据==buildGetInput
     * @return
     */
    public String buildGetInput(){ return null; }

    /**
     * （5.3）加载模型==buildLoadModel
     * 获取模型在服务器环境中的绝对路径
     * @return
     */
    public String buildLoadModel(){
        return "model = joblib.load('" +  model.getEnvironment().APPRoot + "/" + model.getModelFilePath() + "')";
    }

    /**
     * （5.4）预测模型==buildPrecitModel
     * @return
     */
    public String buildPredictModel(){  return null;  }

    /**
     * （5.5）返回输出结果==buldGetOutPut
     * @return
     */
    public String buldGetOutPut(){ return null; }

    /**
     * (6)创建输入解析器
     * 此部分不用生成
     * @return
     */
    public AbstractModelGenerator buildMain(){  return this;  }

}
