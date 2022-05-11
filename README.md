# coursereq.io

## Running the API locally

``` 
cd api
python3 server.py
```
### Test the transcript parser 
On Postman, got to Body and select form-data radio button. Add a file key with the value being the path to your transcript. Then,
```
POST to http://localhost:5000/transcript
```
Should take about 15 seconds to process for a 4 page transcript. 

