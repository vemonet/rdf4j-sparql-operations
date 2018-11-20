package nl.unimaas.ids.operations;

import java.io.IOException;

import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.slf4j.LoggerFactory;

/**
 * A class to upload to GraphDB SPARQL endpoint
 */
public class SparqlUpdate extends AbstractSparqlOperation {

	public SparqlUpdate(String endpoint, String username, String password) {
		super(endpoint, username, password);
		logger = LoggerFactory.getLogger(SparqlConstruct.class.getName());
	}

	public void executeQuery(RepositoryConnection conn, String queryString, String filepath) throws RepositoryException, MalformedQueryException, IOException {
		logger.info("Updating: " + filepath);
		
		// Query the SPARQL endpoint
		Update update = conn.prepareUpdate(QueryLanguage.SPARQL, queryString);
		update.execute();
	}
}