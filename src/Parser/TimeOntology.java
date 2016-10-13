/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;


/**
 *
 * @author mirko
 */
public class TimeOntology {
    
 private final String owlNamespace = "http://www.w3.org/2006/time#";   
public final Resource Interval;
public final Resource Instant;
public final Property hasBeginning;
public final Property hasEnd;
public final Property inXSDDateTime;

    public TimeOntology() {
        this.inXSDDateTime = ResourceFactory.createProperty(owlNamespace+"inXSDDateTime");
        this.hasEnd = ResourceFactory.createProperty(owlNamespace+"hasEnd");
        this.hasBeginning = ResourceFactory.createProperty(owlNamespace+"hasBeginning");
        this.Instant = ResourceFactory.createResource(owlNamespace+"Instant");
        this.Interval = ResourceFactory.createResource(owlNamespace+"Interval");
    }
    
}
