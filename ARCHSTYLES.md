# Architectural Styles

## 1. Pipe and Filter
### Definition
Applications that require a defined series of computations on ordered data, with input and output being streams of data.
### Parts
1. Components - "filters" that apply transformations on the input streams. Usually incremental (i.e. output begins before all input is consumed)
2. Connectors - "pipes" serve as channels between streams, taking outputs of one filter into inputs of another
### Examples
1. Unix shell scripts (i.e. cat file | grep Eroll | wc - l)
2. Traditional compilers, where compilation phases are pipelined and not already incremental (i.e. lexical analysis, parsing, semantic analysis, code generation)

| Pros | Cons |
| ---- | ---- |
|- easy to understand<br>- reusable<br>- easily maintained and enhanced<br>- specialized analysis<br>- support concurrent execution|- Not good for handling interactive systems<br>- Loss of performance<br>- Increased complexity|

### Analysis
<em>NO. Probably not for our use case as our app involves lots of user interaction with their course uploading and sequencing. Parsing/unparsing in excess not optimal as courses already taken do not need to be reprocessed.</em>

## 2. Data-Centered/Repository style
### Definition
Applications that require establishing, augmenting and maintaining a complex central body of information. Long-term persistence for information that is manipulated in many ways.
### Parts
1. Components - central data structure representing current state of system and a collection of independent components that operate on the central data structure
2. Connectors - direct memory access
### Examples
1. Central code repository systems
2. Programming environments
3. Graphical editors
4. Database management systems
5. Games (i.e. open world/city is central data share and each gamer is component; example of blackboard style)

| Pros | Cons |
| ---- | ---- |
|- Efficient way to store large amounts of data<br>- Sharing model is available for all components (know how to access data)<br>- Centralized|- Need agreement on data model used<br>- Difficult to distribute data<br>|- Data evolution is expensive<br>|-Single point of security failure|

### Analysis
<em>POTENTIAL. Can have the course list as a sharing model available for all components</em>

## 3. Implicit Invocation Style
### Definition
Applications where creators of data do not need to know who or how many will use the data. A component broadcasts output that are consumed by other components.
### Parts
1. Components - data generators and consumers
2. Connectors - procedure calls and event bus (some sort of centralized software communication hub)
### Examples
1. Publish-subscribe (Twitter, Environment Canada)
2. Event-based (debugged stops at breakpoint and makes announcement)

| Pros | Cons |
| ---- | ---- |
|- Provides strong support for reuse<br>- Eases system evolution<br>- Efficient dissemination of one-way information (especially for publish-subscribe)|- Need special protocol for when number of subs are high<br>- When component announces event, no idea what other components will respond to it, the order which they were invoked, or when the responses are finished|

### Analysis
<em>NO. Not many large sets of data communicating with others, data exclusive to user</em>

## 4. Layered Style
### Definition
Application that is seperated into ordered layers, which can interact with layers one above or below it.
### Parts
1. Components - "layers" which do local calculations and communicates with adjacent layers
2. Connectors - "pre-determined" protocols for communication between layers
### Examples
1. Layered communication protocols (TCP, UDP, IP protocol)
2. Operating systems

| Pros | Cons |
| ---- | ---- |
|- Design based on abstraction<br>- Enhancement as changes to one layer only affects two<br>- Reusable since different implementations of layers can be used|- Not all systems are easily structured in a layered fashion<br>- Performance requirements|

### Analysis
<em>NO. MVVM (later discussed below) or MVC can be used as layered communication.</em>

## 5. Client-Server Style
### Definition
Applications that involve distributed data and processing across a range of components.
### Parts
1. Components - servers (stand-alone components that provide specific services) and clients (components that call on the services provided by servers)
2. Connectors - the network
### Examples
1. File servers (i.e. dropbox)
2. Database servers (client passes SQL requests to the DB server and are returnded over the network to the client)

| Pros | Cons |
| ---- | ---- |
|- Data distribution straightforward<br>- Transparency of location<br>- Mix and match heterogenous platforms (clients and servers are running on different platforms)<br>- Easy to add new servers or upgrade existing|- No central register of names and services (hard to find which services are available)|

### Analysis
<em>POTENTIAL. Clients contact the server to retreive data about their courses or get alerts about their courses</em>

## 6. Process-control Style
### Definition
Applications whose purpose is to maintain properties of outputs of the process at different reference points.
### Parts
1. Components - process definition (mechanisms for manipulating process variables) and control algorithm (deciding how to manipulate process variables)
2. Connectors<br>
a) controlled variables (system controlled), input variables (input to the process), and manipulated variables (can be changed by controller)<br>
b) Set point, the desired value for a controlled variable<br>
c) Sensors, which are used to obtain values of process variables
### Examples
1. Automobile Anti-Lock Brakes
2. Nuclear Power Plants
3. Automobile Cruise-Control

| Pros | Cons |
| ---- | ---- |
|N/A|N/A|

### Analysis
<em>NO. Our application does not need to maintain locks or excessively checking in at different reference points</em>

## 7. Serverless
### Definition
Applications where users are using and paying for functionality on as as-needed-basis, usually when system load is inconsistent.
### Parts
1. Components - Function (Container)
2. Connectors - Event<br>
a) Synchronous (HTTP Request)<br>
b) Asynchronous (Message queue that does not expect and immediate response)
### Examples
1. Website that offeres image conversion service (will submit a request to convert which will start the conversion. Then when user clicks on download, will request a download image function. Since these requests are sporadic, running a server all day is not viable)

| Pros | Cons |
| ---- | ---- |
|- Pay per use<br>- Less maintenance (auto-scaling)<br>- Fast deployment<br>-Easy to debug as functions are specialized|- Third party dependencies<br>- Initial latency (cold start)<br>- Stateless nature, functions will not remember the previous runs|

### Analysis
<em>POTENTIAL. Since our application involves many user requests, serverless could be an option</em>

## 8. Microservices
### Definition
Applications with single-function modules with well-defined interfaces and operations. For complex applications that are highly scalable and need short release cycles.
### Parts
1. Components - Self-contained services
2. Connectors<br>
a) Synchronous (HTTP Request)<br>
b) Asynchronous (Advanced Message Queuing Protocol)
### Examples
1. E-commerce system

| Pros | Cons |
| ---- | ---- |
|- Independently deployavle microservices<br>- Easier CI/CD integrations<br>- Reduce downtime due to fault isolation<br>-Team autonomy|- Costs may be higher<br>- Additional complexity of a distributed system|

### Analysis
<em>YES. Could decouple features into microservices.</em>

## 9. MVVM
### Definition
Applications that have seperated frontend UI and backend logic
### Parts
1. Components<br>
a) View layer used to display data that is part of the app to the user and also get input from the user<br>
b) ViewModel is the component that binds to the view layer. Represents the state that will be used by the view component.<br>
c) Model is component that holds data through objects, abstraction of the data in the database or datastore<br>
2. Connectors - Events and Method Calls
### Examples
1. Reader app
2. Any app with UI and some data in the back to be manipulated

| Pros | Cons |
| ---- | ---- |
|- Testing<br>- Different teams can independently work|- Simpler applications MVVM may be overkill<br>- Harder to debug as databinding is declarative|

### Analysis
<em>YES. Similar to the layered communication. Able to abstract the repository and model view layers.</em>
