
import Parser.ProfileParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.jena.query.*;
import org.apache.jena.query.QueryFactory;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mirko
 */
public class LinkedInTest1 {
    
    public static void main(String[] args) throws IOException{
        //PrintWriter writer = new PrintWriter("D:\\LinkedinData.RDF", "UTF-8");
        //Model model;
        //model = ProfileParser.parseFilesFromFolder(new File("D:\\LinkedInProfiles"));
        //model.write(writer , "RDF/XML");
        
        Path input = Paths.get("D:\\LinkedinData.RDF");

        Model model_data = ModelFactory.createDefaultModel() ;
        System.out.println(input.toUri().toString());
        model_data.read(input.toUri().toString(), "RDF/XML") ;
        model_data.write(System.out , "RDF/XML");
        
        
        String queryString1 = 
        "PREFIX imdb: <http://data.linkedmdb.org/resource/movie/> " + 
        "PREFIX dbpedia: <http://dbpedia.org/ontology/> " +
        "PREFIX dcterms: <http://purl.org/dc/terms/> " + 
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
        "SELECT ?brand ?title ?count\n" +
"WHERE {?brand ?title ?count\n }" +
"    LIMIT 100";
    String queryString = " SELECT ?oil ?oiq  WHERE { SERVICE <http://foaf/> {<foaf:Toby_Maguire> <foaf:age> <foaf:Toby_Maguire> }  . SERVICE <http://DB1/> {<DB1:Toby_Maguire> <DB1:starring> <DB1:Spiderman> }  BIND ( <DB1:Toby_Maguire> as ?oil )  BIND ( <DB1:Toby_Maguire> as ?oiq ) }";
    

    Query query = QueryFactory.create(queryString1); 
    System.out.println(query);
// exception happens here
    QueryExecution qe = QueryExecutionFactory.create(query,model_data);

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
                


        
    }
    
}
