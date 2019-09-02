package com.casic.atp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 算法模型，用来保存模型的名称，路径，输入输出参数，解析函数
 *  * @author zouping
 *  * @date 2019-08-13
 *  * 数据生成器，按以下顺序生成模型
 *  * seq    name           method           variable               eg.
 *  * (1)  引入包文件     buildImport        $def_import          import *
 *  * (2)  定义app工程    buildFlaskApp      $def_app            app = Flask()
 *  * (3)  定义输入函数   buildInputParser   $def_get_input      def get_input()
 *  * (4)  定义输出结果   buildOutputParser  $def_get_output     def get_output(data)
 *  * (5)  定义api函数    buildModelAPI     $def_model_api       def model_api()
 *  * (5.1)定义模型       buildGetModel     $load_model          model = joblib.loads
 *  * (5.2)获取输入数据   buildGetInput     $get_input           get_input()
 *  * (5.3)加载模型      buildLoadModel    $load_input          get_input(model)
 *  * (5.4)预测模型      buildPredictModel  $predict_model      predict_model(data)
 *  * (5.5)返回输出结果  buldGetOutPut     $return_output       return get_output()
 *  * (6) 主执行文件     buildMain         $def_main            if __main__ == "main"
 */
public class ATPModel {
    public static String MODEL_JOBLIB = "joblib";
    public static String MODEL_KERAS = "keras";

    //模型名称, eg. iris
    public String  name;
    //app工程在环境中的路径
    public String appFilePath;
    //模型本身在环境中的路径
    public String modelFilePath;
    //keras或者joblib
    public String type;
    //自定义输入参数
    public List<ATPParams> inParams = new ArrayList<ATPParams>();
    //自定义输出参数
    public List<ATPParams> outParams = new ArrayList<ATPParams>();
    //定义输入解析函数
    public String defGetInput;
    //定义输出解析函数
    public String defGetOutput;
    //模型所在的容器环境对象
    public ATPEnvironment environment = new ATPEnvironment();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAppFilePath() {
        return appFilePath;
    }

    public void setAppFilePath(String appFilePath) {
        this.appFilePath = appFilePath;
    }

    /**
     * 运行环境会将app放置在model下面，通过python model/${filePath}来启动app工程
     * 所以在获取模型文件路径的时候，默认要加上model前缀
     * TODO 共享文件存储，要获得模型文件的绝对路径
     * @return
     */
    public String getModelFilePath() {
        return "model/" + this.modelFilePath;
    }

    public void setModelFilePath(String modelFilePath) {
        this.modelFilePath = modelFilePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefGetInput() {
        return defGetInput;
    }

    public void setDefGetInput(String defGetInput) {
        this.defGetInput = defGetInput;
    }

    public String getDefGetOutput() {
        return defGetOutput;
    }

    public void setDefGetOutput(String defGetOutput) {
        this.defGetOutput = defGetOutput;
    }

    public List<ATPParams> getInParams() {
        return inParams;
    }

    public void setInParams(List<ATPParams> inParams) {
        this.inParams = inParams;
    }

    public List<ATPParams> getOutParams() {
        return outParams;
    }

    public void setOutParams(List<ATPParams> outParams) {
        this.outParams = outParams;
    }

    public ATPEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(ATPEnvironment environment) {
        this.environment = environment;
    }

    public Map toMap(){
        Map map = new HashMap();
        if(this.getDefGetInput()!=null){
            map.put("def_get_input", this.getDefGetInput());
        }
        if(this.getDefGetOutput()!=null){
            map.put("def_get_output", this.getDefGetInput());
        }
        return map;
    }

    /**
     * 获得模型本地服务工程所在的路径
     * @return
     */
    public String getTmpAPPFilePath(){
        return ATPEnvironment.tmpDir + "/" + getTmpAppFileName();
    }

    /**
     * 获取工程文件名称
     * @return
     */
    public String getTmpAppFileName(){
        return  this.getName() + "_server.py";
    }
}
