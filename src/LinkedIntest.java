


import Parser.LinkedinOntology;
import Parser.ProfileParser;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javaapplication6.LinkedIn;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;


public class LinkedIntest {

final static String serviceEndpoint = "http://sparql.org/sparql";
//final static String serviceEndpoint = "http://data.linkedmdb.org/sparql";

public static void main(String[] args) throws IOException, ParseException {
    
   String dataNamespace = "http://www.semanticweb.org/mirko/data/";
   String ontologyNamespace = "http://www.semanticweb.org/mirko/ontologies/2016/1/LinkedIn#";
   String owlNamespace = "http://www.w3.org/2006/time#";
Calendar cal = Calendar.getInstance();

cal.setTimeInMillis(0);
cal.set(2012, 9, 1);
DateFormat df = new SimpleDateFormat("MMMMMMMMMMMM yyyy");
Date date = df.parse("March 2018");
cal = Calendar.getInstance();

cal.setTime(date);
XSDDateTime time = new XSDDateTime(cal);
Calendar cal1 = Calendar.getInstance();

Date dat1 = new Date();
//
cal1.setTime(dat1);
XSDDateTime time1 = new XSDDateTime(cal1);
System.out.println(time.getMonths());
System.out.println(time);
    String queryString = " SELECT ?x ?z WHERE { ?x a ?z } ";
    // create an empty Model
Model model = ModelFactory.createDefaultModel();

//Owl Time ontology
Resource Interval = ResourceFactory.createResource(owlNamespace+"Interval");
Resource Instant = ResourceFactory.createResource(owlNamespace+"Instant");
Property hasBeginning = ResourceFactory.createProperty(owlNamespace+"hasBeginning");
Property hasEnd = ResourceFactory.createProperty(owlNamespace+"hasEnd");
Property inXSDDateTime = ResourceFactory.createProperty(owlNamespace+"inXSDDateTime");


//LinkedIn Ontology

Resource Member = ResourceFactory.createResource(ontologyNamespace+"Member");
Resource ExperienceItem = ResourceFactory.createResource(ontologyNamespace+"ExperienceItem");
Property Summary = ResourceFactory.createProperty(ontologyNamespace, "Summary");
Property hasExperience = ResourceFactory.createProperty(ontologyNamespace, "hasExperience");
Property atTime = ResourceFactory.createProperty(ontologyNamespace, "atTime");
Property atInstitution = ResourceFactory.createProperty(ontologyNamespace, "atInstitution");
Property withRole = ResourceFactory.createProperty(ontologyNamespace+"withRole");
        
//LinkedIn Resources

Resource University_of_Bedfordshire = model.createResource("http://dbpedia.org/resource/University_of_Bedfordshire");
Resource Lecturer_in_Computer_Science_and_Technology = ResourceFactory.createResource(dataNamespace+"Lecturer_in_Computer_Science_and_Technology");


// create the resource
//   and add the properties cascading style
Resource experience11 = model.createResource(); 
Resource member1
  = model.createResource(dataNamespace+"1")
        .addProperty(RDF.type, Member)
        .addProperty(Summary, "- Researcher and university lecturer in Cyber Security, Digital Forensics and Incident Response (DFIR). Experience extended to Project Management (PRINCE2), Information System Strategy and Risk Assessment.\n" +
"- Fellow of the Higher Education Academy (HEA), UK\n" +
"- Regular author in the Digital Forensics Magazine, UK since 2014.\n" +
"- Transnational education (TNE) Link-coodinator for the University of Bedfordshire in Oman.\n" +
"- In 2015, contributed three chapters on cyberstalking & online safety to a book published in collaboration with Bedfordshire Police.\n" +
"- First-class BSc (Hons) in Computer Science and PhD in Cyber Security from the University of Bedfordshire, UK.")
        
        .addProperty(hasExperience, experience11);
                                                          
                                                          experience11.addProperty(atTime, model.createResource()
                                                                                                    .addProperty(RDF.type, Interval )
                                                                                                    .addProperty(hasBeginning, model.createResource()
                                                                                                                                                    .addProperty(RDF.type, Instant)
                                                                                                                                                    .addProperty(inXSDDateTime, model.createTypedLiteral(time)
                                                                                                                                                    ))
                                                                                                    .addProperty(hasEnd, model.createResource()
                                                                                                                                              .addProperty(RDF.type, Instant)
                                                                                                                                              .addProperty(inXSDDateTime, model.createTypedLiteral(time1)
                                                            )));
//member1.addProperty(hasExperience, experience1);

experience11.addProperty(atInstitution,University_of_Bedfordshire );

    LinkedinOntology Lont = new LinkedinOntology();

experience11.addProperty(withRole, Lecturer_in_Computer_Science_and_Technology);

experience11.addProperty(RDFS.label, "• Teaching Cyber Security, Digital Forensics and Incident Response (DFIR)\n" +
"• Undertake research in accordance with the University’s research strategy\n" +
"• Participate in the academic management of the department.\n" +
"• Supervision of PG and UG research projects/dissertations and other duties as appropriate.\n" +
"\n" +
"• To support student learning at UG and PG level underpinned by knowledge and skills in the application of the latest information and communications technologies from industry experience.\n" +
"\n" +
"• Selected Units I deliver(ed): Advanced Security and Countermeasures, Incident Response, Forensic Data Analysis, Network Systems, Cryptography, Systems Architecture, Network Administration and Management\n" +
"\n" +
"• PhD and MSc by research Examiner, as of 2014, an approved PhD examiner by the Academic Board at the university.\n" +
"\n" +
"• Researcher in IRAC, NCCR and Cyber Security Research Group; click the hyperlink titled 'Organization' below for more details.\n" +
"\n" +
"• International Collaboration: I am a member of the Faculty International Group (FIG) as a Link Co-ordinator. Therefore, I support the collaboration with our international partners and co-ordinate the delivery of some of our MSc courses outside the UK.");


model.write(System.out , "TURTLE");

ProfileParser parser = new ProfileParser(new File("src/profile1.html"));
parser.getModel().write(System.out , "TURTLE");

final File folder = new File("C:\\Users\\mirko\\Pictures");
listFilesForFolder(folder);

/*
Model schema = FileManager.get().loadModel("LinkedinOntology.owl");
InfModel infmodel = ModelFactory.createRDFSModel(schema, model);

    Query query = QueryFactory.create(queryString); 
    System.out.println(query);
// exception happens here
    QueryExecution qe = QueryExecutionFactory.create(query, infmodel);

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

*/

}
public static void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
            listFilesForFolder(fileEntry);
        } else {
            System.out.println(fileEntry.getName());
        }
    }
}



}