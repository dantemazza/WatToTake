# Architectural Styles

## 1. Pipe and Filter
### Definition
Applications that require a defined series of computations on ordered data, with input and output being streams of data.

### Parts
1. Components - "filters" that apply transformations on the input streams. Usually incremental (i.e. output begins before all input is consumed)
2. Connectors - "pipes" serve as channels between streams, taking outputs of one filter into inputs of another

### Variants
1. Pipelines - Filters are applied in linear sequences
2. Batch Sequential - A degenerate case; each filter processes all of the input data before producing any output

### Examples
1. Unix shell scripts (i.e. cat file | grep Eroll | wc - l)
2. Traditional compilers, where compilation phases are pipelined and not already incremental (i.e. lexical analysis, parsing, semantic analysis, code generation)

| Pros | Cons |
| ---- | ---- |
|- easy to understand<br>- reusable<br>- easily maintained and enhanced<br>- specialized analysis<br>- support concurrent execution|- Not good for handling interactive systems<br>- Loss of performance<br>|- Increased complexity

## Analysis
Probably not for our use case as our app involves lots of user interaction with their course uploading and sequencing. Parsing/unparsing in excess not optimal as courses already taken do not need to be reprocessed.
