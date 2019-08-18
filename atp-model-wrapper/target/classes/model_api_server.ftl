from json import JSONDecodeError
from flask import Flask, jsonify, request
import numpy as np
import json
${def_import}

def ok(obj):
    """
    返回客户端API调用成功
    :param obj: 结果
    :return: json格式
    """
    return jsonify(code=200, result=obj)


def error(msg):
    """
    返回客户端API调用失败
    :param msg:
    :return:
    """
    return jsonify(code=500, msg=str(msg))


app = Flask(__name__)


<#if def_get_input??>
${def_get_input}
<#else>
def get_input(data):
    """ 自定义输入解析函数
    （一）ATP的API请求需满足以下要求
    1. 必须是POST请求
    2. 提交的content-type只支持application/json格式，如果是图像，需转换成base64编码后才能提交（自定义解析函数除外）
    3. 提交数据支持string，int，base64三种格式
    （二）ATP支持的模型
        ATP只支持joblib，keras两种模型格式，joblib通过joblib.load()加载模型
    （三）ATP支持的输入参数
    3.1 JobLib模型
        由于Joblib的预测函数只支持floating-point matrix浮点矩阵，所以默认解析函数只支持一个参数，不支持多个数据项参数，传入参数类似这种[[6.5, 3.0, 5.8, 2.2]]
    3.2 Keras模型
        同JobLib
    （四）输入数据解析要求
    4.1 JobLib模型
    1. 默认解析函数
        默认解析函数只支持一个参数传输，直接将原始数据作为模型预测的传入参数,系统会自动转化为np.array类型
    2. 自定义解析函数
        传入key-value类型的data参数，必须返回一个np.array类型的对象
    4.2 Keras模型
        同JobLib
    :param data: json类型，通过data.get(name)或者data[name]获取请求值
    :return: 返回np.array类型
    """
    predict_data = list(data.values())[0]  # 默认获取第一项数据
    return np.array(predict_data)
</#if>


<#if def_get_output??>
${def_get_output}
<#else>
def get_output(result):
    """
    将结果转化成json格式
    :param result:np.array类型
    :return:json格式
    """
    return ok(result.tolist())
<#/if>


@app.route('/api', methods=['POST'])
def model_api():
    """
    模型处理过程：
    1. 判断请求参数是否为空，为空直接返回，如果不为空，跳转到第2步
    2. 将请求参数转化为utf8编码，并转化为json格式
    3. 加载模型文件
    4. 处理请求数据
    5. 预测模型
    6. 处理返回结果
    :return: json格式
    ok:{code:200, result:[]}
    error:{code:500,msg:*}
    """
    # 1. 判断请求参数是否为空，为空直接返回，如果不为空，跳转到第2步
    data = request.get_data()
    if not data:
        return error("请输入模型参数")
    try:
        # 2. 将请求参数转化为utf8编码，并转化为json格式
        json_data = json.loads(data.decode('utf-8'))
        # 3. 加载模型文件
        ${load_model}
        # 4. 处理请求数据
        x_test = get_input(json_data)
        # 5. 预测模型
        result = model.predict(x_test)
        # 6. 处理返回结果
        return get_output(result)
    except JSONDecodeError:
        return error("json格式转化错误%s" % data)
    except Exception as ex:
        if hasattr(ex, 'message'):
            return error(ex.message)
        else:
            return error(ex)
    except KeyError:
        return error("处理失败")


if __name__ == '__main__':
    app.run()
