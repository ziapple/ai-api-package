from json import JSONDecodeError
from flask import Flask, jsonify, request
import numpy as np
import json


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


def get_input(data):
    predict_data = list(data.values())[0]  # 默认获取第一项数据
    return np.array(predict_data)


def get_output(result):
    """
    将结果转化成json格式
    :param result:np.array类型
    :return:json格式
    """
    return ok(result.tolist())


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
