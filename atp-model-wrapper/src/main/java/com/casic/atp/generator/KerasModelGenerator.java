package com.casic.atp.generator;


/**
 * @author zouping
 * @date 2019-08-13
 * Keras数据模板生成器
 */
public class KerasModelGenerator extends AbstractModelGenerator {
    @Override
    public String buildImportLine(){
        return "from keras.models import load_model";
    }

    @Override
    public String buildLoadModel(){
        return "model = load_model('" +  model.getEnvironment().APPRoot + "/" + model.getModelFilePath() + "')";
    }

    @Override
    public java.lang.String buildPredictModel() {
        return "result = model.predict_classes(x_test)";
    }
}
