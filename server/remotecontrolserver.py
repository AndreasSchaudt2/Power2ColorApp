from flask import Flask, request, jsonify
from ruamel.yaml import YAML
import os

app = Flask(__name__)

CONFIG_FILE_PATH = '/home/andi/Power2Color/Power2Color/config.yaml'
yaml = YAML()

@app.route('/config', methods=['POST'])
def config():
    try:
        command = request.json.get('command')
        if command == 'read':
            with open(CONFIG_FILE_PATH, 'r') as file:
                config_data = yaml.load(file)
            return jsonify({'status': 'success', 'config': config_data})
        elif command == 'write':
            new_config = request.json.get('config')
            if not new_config:
                return jsonify({'status': 'error', 'message': 'No config data provided'}), 400
            with open(CONFIG_FILE_PATH, 'w') as file:
                yaml.dump(new_config, file)
            return jsonify({'status': 'success'})
        else:
            return jsonify({'status': 'error', 'message': 'Invalid command'}), 400
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500

@app.route('/restart', methods=['POST'])
def restart():
    try:
        os.system('sudo reboot')
        return jsonify({'status': 'restarting'})
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500

@app.errorhandler(404)
def not_found(error):
    return jsonify({'status': 'error', 'message': 'Not found'}), 404

@app.errorhandler(405)
def method_not_allowed(error):
    return jsonify({'status': 'error', 'message': 'Method not allowed'}), 405

@app.errorhandler(500)
def internal_server_error(error):
    return jsonify({'status': 'error', 'message': 'Internal server error'}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)