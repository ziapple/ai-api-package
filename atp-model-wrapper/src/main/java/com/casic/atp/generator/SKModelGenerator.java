package com.casic.atp.generator;


/**
 * @author zouping
 * @date 2019-08-13
 * SKModel的模板文件生成器
 */
public class SKModelGenerator extends AbstractModelGenerator {
    @Override
    public String buildImportLine(){
        return "from sklearn.externals import joblib";
    }

    @Override
    public String buildLoadModel(){
        return "model = joblib.load('" +  model.getEnvironment().APPRoot + "/" + model.getModelFilePath() + "')";
    }

    @Override
    public java.lang.String buildPredictModel() {
        return "result = model.predict(x_test)";
    }
}
