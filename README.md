# coursereq.io

## Running the API locally

``` 
cd api
./start-cluster.sh

***DOES NOT WORK ON M1 MACS***
```
### Test the transcript parser 
On Postman, got to Body and select form-data radio button. Add a file key with the value being the path to your transcript. Then,
```
POST to http://localhost:5000/transcript
```
Should take about 10-15 seconds to process a 4 page transcript and receive reccomendations.

### Running the webscrape tasks

```
cd api/course_recommender
python3 webscrape_courses.py
python3 get_course_graph.py
```
