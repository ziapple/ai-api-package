package com.casic.atp.generator;

import com.casic.atp.model.ATPModel;
import freemarker.template.TemplateException;

import java.io.IOException;


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
public interface ModelGenerator {
    //生成模型
    void generate(ATPModel model) throws IOException, TemplateException;
}
