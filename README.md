# About
A project to execute SPARQL queries from string, URL or multiple files using `rdf4j`.

* The user can execute SPARQL queries by
  * Passing a SPARQL query string in `-sp` param 
  * Providing a URL in `-rq` param
  * Providing the path to a directory where the queries are stored in `.rq` text files and executed in the alphabetical order of their filename. 
  * A YAML file with multiple queries. See the [example in resources](https://github.com/vemonet/rdf4j-sparql-operations/blob/master/src/main/resources/describe_statistics-drugbank.yaml)
* *Update*, *construct* and *select* operations supported.
* It is possible to optionally define username and password for the SPARQL endpoint.
* Examples queries: https://github.com/MaastrichtU-IDS/data2services-insert



# Docker
## Build
```shell
docker build -t rdf4j-sparql-operations .
```
## Usage
```shell
docker run -it --rm rdf4j-sparql-operations -?
```
## Run
```shell
# SELECT on dbpedia
docker run -it --rm rdf4j-sparql-operations -op select \
	-sp "select distinct ?Concept where {[] a ?Concept} LIMIT 10" \
	-ep "http://dbpedia.org/sparql"

# CONSTRUCT on GraphDB using GitHub URL
docker run -it --rm rdf4j-sparql-operations -op construct \
	-ep "http://graphdb.dumontierlab.com/repositories/ncats-red-kg" \
	-rq "https://raw.githubusercontent.com/MaastrichtU-IDS/data2services-insert/master/resources/construct-test.rq" 

# Multiple updates (INSERT) on graphdb.dumontierlab.com 
# Note: GraphDB requires to add /statements at the end of the endpoint URL for INSERT
docker run -it --rm -v "/data/data2services-insert/insert-biolink/drugbank":/data \
	rdf4j-sparql-operations -f "/data" -op update \
	-ep "http://graphdb.dumontierlab.com/repositories/test/statements" \
	-un USERNAME -pw PASSWORD

# Run on a YAML with construct
docker run -it --rm -v "/path/to/rdf4j-sparql-operations/src/main/resources/describe_statistics-drugbank.yaml":/data/describe_statistics-drugbank.yaml \
	sparql-rdf4j-operations -f "/data/describe_statistics-drugbank.yaml" -op construct \
	-ep "http://graphdb.dumontierlab.com/repositories/ncats-red-kg" \
	-un username -pw password
```



## Variables

Variables can be set in the SPARQL queries. For example:

construct.rq in /data/operations

```sql
PREFIX owl: <http://www.w3.org/2002/07/owl#>
CONSTRUCT 
{ 
    ?class a <?_classType> .
}
WHERE {
    GRAPH <?_graphUri> {
        [] a ?class .
    }
}
```

Execute:

```shell
docker run -it --rm -v /data/operations:/data rdf4j-sparql-operations \
	-f "/data/operations/construct.rq" -op construct \
	-ep "http://localhost:7200/repositories/test" \
    -var serviceUrl:http://localhost:7200/repositories/test graphUri:http://graph classType:http://test/class
```

 ## Execute on specific datasets

From https://github.com/MaastrichtU-IDS/data2services-insert 

```shell
# DrugBank
docker run -it --rm -v "$PWD/insert-biolink/drugbank":/data rdf4j-sparql-operations \
	-f "/data" -un USERNAME -pw PASSWORD \
	-ep "http://graphdb.dumontierlab.com/repositories/ncats-red-kg/statements" \
	-var serviceUrl:http://localhost:7200/repositories/test inputGraph:http://data2services/graph/xml2rdf outputGraph:http://data2services/biolink/drugbank

# HGNC
docker run -it --rm -v "$PWD/insert-biolink/hgnc":/data rdf4j-sparql-operations \
	-f "/data" -un LOGIN -pw PASSWORD \
	-ep "http://graphdb.dumontierlab.com/repositories/ncats-red-kg/statements" \
    -var serviceUrl:http://localhost:7200/repositories/test inputGraph:http://data2services/graph/autor2rml outputGraph:http://data2services/biolink/hgnc
```

