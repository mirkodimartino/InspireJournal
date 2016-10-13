/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chase;


import Parser.L4AllOntology;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import static org.apache.jena.rdf.model.ModelFactory.createRDFSModel;

import org.apache.jena.util.FileManager;

/**
 *
 * @author mirko
 */
public class Test {
    
    public static void main(String[] args) {
        
    Model model = FileManager.get().loadModel("LinkedinData.RDF_XML");
    Model schema = FileManager.get().loadModel("LinkedInOntology.owl");
    Model unionLinkedin = ModelFactory.createUnion(model,schema);
   
    Model dataL4all = FileManager.get().loadModel("seed.nt");
    Model schemaL4all = FileManager.get().loadModel("ontology.nt");
    Model unionL4All= ModelFactory.createUnion(dataL4all,schemaL4all);
    Model union = ModelFactory.createUnion(unionL4All, unionLinkedin);
    InfModel infmodel = createRDFSModel(union);
    L4AllOntology l4all = new L4AllOntology();
        System.out.println(l4all.epEnd);
    
    final String query1="SELECT ?x ?y ?z WHERE { ?x ?y ?z } LIMIT 100";
    
    Query query = QueryFactory.create(query1);
    
    QueryExecution qe = QueryExecutionFactory.create(query,union);
    
    try {
        ResultSet rs = qe.execSelect();
        if ( rs.hasNext() ) {
            // show the result, more can be done here
            System.out.println(ResultSetFormatter.asText(rs));
        }
    } 
    catch(Exception e) { 
        System.out.println(e.getMessage());
    }
    finally {
        qe.close();
    }
    
    System.out.println("\nall done.");
    boolean add = true;
    while (add==true){
        add = false;
        String query1s = "SELECT ?x ?y WHERE {?x <"+ l4all.epEnd+"> ?y}";
                Query queryEpend = QueryFactory.create(query1s);
                
        
    }
}
    
    
    }
    

