from flask import Flask, jsonify

app = Flask(__name__)

led_flag = False

@app.route('/led/on', methods=['GET'])
def led_on():
    global led_flag
    led_flag = True
    # check here if the le is actualy on
    print('led on')
    return {'led status': 1}

@app.route('/led/off', methods=['GET'])
def led_off():
    global led_flag
    led_flag = False
    #check here if the le is actualy off
    print('led off')
    return {'led status': 0}

@app.route('/led/checkState', methods=['GET'])
def led_checkState():
    return str(int(led_flag))

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
