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
|- easy to understand<br>- reusable<br>- easily maintained and enhanced<br>- specialized analysis<br>- support concurrent execution|- Not good for handling interactive systems<br>- Loss of performance<br>|- Increased complexity

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
<em>POTENTIAL.</em>

## 3. Implicit Invocation Style
### Definition
Applications where creators of data do not need to know who or how many will use the data. A component broadcasts output that are consumed by other components.

### Parts
1. Components - data generators and consumers
2. Connectors - procedure calls and event bus (some sort of centralized software communication hub)

### Examples
1. Publish-subscribe (Twitter, Environment Canada)
2. Event-based (debugged stops at bearkpoint and makes announcement)

| Pros | Cons |
| ---- | ---- |
|- Provides strong support for reuse<br>- Eases system evolution<br>- Efficient dissemination of one-way information (especially for publish-subscribe)|- Need special protocol for when number of subs are high<br>- When component announces event, no idea what other components will respond to it, the order which they were invoked, or when the responses are finished|

### Analysis
<em>POTENTIAL. For alerts when certain classes are available, users may choose to subscribe for a notification.</em>
