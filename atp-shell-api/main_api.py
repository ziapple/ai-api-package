from flask import Flask
from flask import request
import os


app = Flask(__name__)


# 执行命令
@app.route('/shell/<cmd>', methods=['GET','POST'])
def execute_shell(cmd):
    try:
        result = os.system(cmd)
        return '200'
    except:
        return '500'
    return '200'


# 执行某个python文件
@app.route('/python/<file_path>', methods=['GET','POST'])
def execute_python(file_path):
    try:
        result = os.system('python ' + file_path)
        return '200'
    except:
        return '500'
    return '200'
    
   
@app.route('/upload', methods=['POST'])
def upload_file():
    f = request.files['the_file']
    f.save('./'+f.filename)


if __name__ == '__main__':
    app.run()