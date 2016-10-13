package Parser;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;



/**
 *
 * @author mirko
 */
public class L4AllOntology {
    


private final String ontologyNamespace = "http://www.L4All.com/";

public final Property epEnd ; 

    public L4AllOntology() {
        this.epEnd = ResourceFactory.createProperty(ontologyNamespace+"epEnd");
  
    }
    
    
    
}
