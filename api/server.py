from flask import Flask, request, jsonify
from transcript_parser.extract import transcript_to_json, fan_out
from course_recommender.parse_json_transcript import get_recommendations
app = Flask(__name__)

@app.route('/transcript', methods = ['POST'])
def transcript_parser():
    if 'file' not in request.files:
        print(request.headers)
        print("JSON")
        print("DATA")
        print(request.data)
        message = 'No file part in the request'
        print(message)
        res = jsonify({'message': message})
        res.status_code = 400
        print(res)
        return res
    file = request.files['file']
    if file.filename == '':
        message = 'No file selected for uploading'
        print(message)
        res = jsonify({'message': message})
        res.status_code = 400
        return res
    if file and file.filename.split(".")[-1].lower() == "pdf":
        print(request.headers)
        print("JSON")
        print("DATA")
        print(request.data)
        file_path = fan_out(file)
        result = transcript_to_json(file_path)
        recommendations = get_recommendations(result)
        response = {"courses": result, "recommendations": recommendations}
        resp = jsonify(response)
        print(resp)
        resp.status_code = 200
        return resp
    else:
        message = 'Allowed file types are pdf only'
        print(message)
        resp = jsonify({'message': message})
        resp.status_code = 400
        print(resp)
        return resp


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')