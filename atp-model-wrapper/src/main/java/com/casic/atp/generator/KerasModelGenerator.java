package com.casic.atp.generator;


/**
 * @author zouping
 * @date 2019-08-13
 * Keras数据模板生成器
 */
public class KerasModelGenerator extends AbstractModelGenerator {
    public String buildImportLine(){
        return "from sklearn.externals import joblib";
    }

    public String buildLoadModel(){
        return "model = joblib.load('" +  model.getEnvironment().APPRoot + "/" + model.getModelFilePath() + "')";
    }
}
