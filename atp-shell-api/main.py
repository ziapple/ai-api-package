from flask import Flask
from flask import request
import os


app = Flask(__name__)


# 执行成功
def ok():
    return '{"code": "ok"}'


# 执行失败
def fail(msg):
    return '{"code": "500:, "msg": msg}'
    

# 检查端口是否存在，直接kill掉
def check_server():
    os.system("netstat -nltp|grep 5001|awk '{print $7}'|awk -F/ '{print $1}'|xargs kill -9")
    
    
# 执行shell命令
@app.route('/shell/<cmd>', methods=['GET','POST'])
def execute_shell(cmd):
    try:
        result = os.system(cmd)
        return ok()
    except:
        return fail('执行失败')
    return ok()


# 执行某个python文件
@app.route('/python/<file_path>', methods=['GET','POST'])
def execute_python(file_path):
    try:
        file_path = file_path.replace('!','/') # 用!替换/文件路径
        dir = os.path.abspath(os.path.dirname(__file__)) #当前路径
        print(os.path.join(dir, file_path))
        check_server()
        result = os.system('python ' + os.path.join(dir, file_path))
    except:
        return fail('执行失败')
    return ok()


# 执行某个python文件
@app.route('/python/nohup/<file_path>', methods=['GET','POST'])
def execute_python_nohup(file_path):
    try:
        dir = os.path.abspath(os.path.dirname(__file__)) #当前路径
        file_path = file_path.replace('!','/') # 用!替换/文件路径
        print(os.path.join(dir, file_path))
        check_server()
        result = os.system('python ' + os.path.join(dir, file_path) + ' &')
        return ok()
    except:
        return fail('执行失败')
    return ok()
    

# 上传模型文件
# keras,joblib,tensorflow三种模型文件类型
# 文件名跟上传文件名保持一致
@app.route('/upload_model', methods=['POST'])
def upload_model():
    fname = request.files.get('file')  #获取上传的文件
    if fname:
        new_fname = 'model/' + fname.filename
        fname.save(new_fname)  #保存文件到指定路径
        return ok()
    else:
        return fail('请上传文件')


# 上传模型API工程
# 该工程实际上也是一个flask工程，就一个执行文件
# 通过该文件启动web算法API服务，接收外部API访问
# 文件名保存为model_api.py
@app.route('/upload_app', methods=['POST'])
def upload_file():
    fname = request.files.get('file')
    if fname:
        new_fname = os.path.abspath(os.path.dirname(__file__) + '/model/' + fname.filename)
        fname.save(new_fname)
        return ok()
    else:
        return fail('请上传文件')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)