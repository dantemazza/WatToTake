from flask import Flask, request, jsonify
from transcript_parser.extract import transcript_to_json, fan_out
from course_recommender.parse_json_transcript import get_recommendations
app = Flask(__name__)

@app.route('/transcript', methods = ['POST'])
def transcript_parser():
    if 'file' not in request.files:
        res = jsonify({'message': 'No file part in the request'})
        res.status_code = 400
        return res
    file = request.files['file']
    if file.filename == '':
        res = jsonify({'message': 'No file selected for uploading'})
        res.status_code = 400
        return res
    if file and file.filename.split(".")[-1].lower() == "pdf":
        file_path = fan_out(file)
        result = transcript_to_json(file_path)
        recommendations = get_recommendations(result)
        response = {"courses": result, "recommendations": recommendations}
        resp = jsonify(response)
        resp.status_code = 200
        return resp
    else:
        resp = jsonify({'message': 'Allowed file types are pdf only'})
        resp.status_code = 400
        return resp


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')