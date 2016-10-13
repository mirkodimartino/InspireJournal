/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author mirko
 */
public class ProfileParser {
    
    Model model; 
    Document document; 
    String dataNamespace = "http://www.semanticweb.org/mirko/data/";
    Resource member ; 
    LinkedinOntology Lont = new LinkedinOntology();
    
    //html key string 
    String stringSummary = "div#summary-item";

    
    

    public ProfileParser(String profiletext) throws IOException {
        String uniqueID = UUID.randomUUID().toString();
        this.model = ModelFactory.createDefaultModel();
        this.document = Jsoup.parse(profiletext);
        this.member = model.createResource(dataNamespace+uniqueID);
        parse();
    }
    
    public ProfileParser(File file) throws IOException {
        String uniqueID = UUID.randomUUID().toString();
        this.model = ModelFactory.createDefaultModel();
        this.document = Jsoup.parse(file, "UTF-8", "http://example.com/");
        this.member = model.createResource(dataNamespace+uniqueID);
        parse();
    }

    public Model getModel() {
        
        return model;
    }
    
    public static Model parseFilesFromFolder(final File folder) throws IOException {
       
        Model model  = ModelFactory.createDefaultModel(); 
        
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
            Model temp1, temp2 = parseFilesFromFolder(fileEntry);
            temp1 = temp2.union(model);
            
            model = temp1;
            
        } else {
            ProfileParser parser = new ProfileParser(new File(fileEntry.getAbsolutePath()));
            //parser.getModel().write(System.out , "RDF/XML-ABBREV");
            Model temp1, temp2 = parser.getModel();
            temp1 = temp2.union(model);
            //temp1.write(System.out, "TURTLE");
            model = temp1;
        }
    }
    
    return model;
}


    

  
    
    
    
    public static void main(String[] args) throws IOException{
        File input = new File("src/profile.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Element link = doc.select("div#summary-item").first();
        System.out.println(link.text());
        Elements links = doc.select("div[id~=experience-[0-9][0-9]*$]");
        //String text = doc.body().text(); // "An example link"
        //String linkHref = link.attr("href"); // "http://example.com/"
        System.out.println(links.size());
        for (Element element : links){
            
        
        System.out.println(element.select("a[title]").text());// "example""
        }
        //String linkOuterH = link.outerHtml();
        // "<a href="http://example.com"><b>example</b></a>"
        //String linkInnerH = link.html(); // "<b>example</b>"
        
    }
    
    public void parse() throws IOException{
        
        
        addSummary(document.select(stringSummary).first());
        addExperienceItems(document.select("div[id~=experience-[0-9][0-9]*$]"));
        addSubtitle(document.select("p.title"));
        addEducation(document.select("div[id~=education-[0-9][0-9]*$]"));
        addSkills(document.select("li[data-endorsed-item-name]"));
        //addPublications(document.select("div[id~=publication-[0-9][0-9]*$]"));
        
        
       
    }

    

    private void addSummary(Element first) {
        if (first!=null)
        member.addProperty(new LinkedinOntology().Summary, first.text());
    }

    private void addExperienceItems(Elements experienceItems) {
        for (Element element : experienceItems)
        addExperience(element);
    }

    private void addExperience(Element element) {
        if (element!=null){
        Resource experience = model.createResource();
        member.addProperty(Lont.hasExperience, experience);
        experience.addProperty(Lont.withRole, model.createResource(element.select("h4 a").attr("href")).addProperty(RDFS.label, element.select("h4 a").text()));
        experience.addProperty(Lont.atInstitution, model.createResource("https://www.linkedin.com"+element.select("h5 a").attr("href")).addProperty(RDFS.label,element.select("h5 a").text()));
        Elements timeExpElements = element.select("span.experience-date-locale");
        addIntervalTime(experience, timeExpElements);
        experience.addProperty(RDFS.label, element.text());
        }
    }

    private void addSubtitle(Elements select) {
       member.addProperty(Lont.subtitle, select.first().text());
    }

    private void addIntervalTime(Resource resource, Elements timeExpElements) {
        //System.out.println(timeExpElements.text());
        
        try {
        TimeOntology timeOnt = new TimeOntology();
        LinkedinOntology linkOnt = new LinkedinOntology();
        Elements timeList = timeExpElements.select("time");
        
        XSDDateTime timeB, timeE;
        
        if (timeExpElements!=null&&timeExpElements.text().matches(".*\\w.*")){
        if (timeExpElements.text().contains("Present")){
            /* Date date = new Date();
            Calendar calE = Calendar.getInstance();
            calE.setTime(date);
            timeE = new XSDDateTime(calE);*/
            timeE = null;
            String timeString = timeList.first().text();
            
            timeB = parseToXSDDateTime(timeString);
        }
        else {
            Element timeElement ;
            timeElement = timeList.first();
            String timeString = timeElement.text();
            
            timeB = parseToXSDDateTime(timeString);
            
            timeString = timeElement.nextElementSibling().text();
            parseToXSDDateTime(timeString);
            timeE = parseToXSDDateTime(timeString);
            
            
            
        }
        Resource interval = model.createResource();
        resource.addProperty(linkOnt.atTime, interval);
        
        if (timeB!=null){
        interval.addProperty(RDF.type, timeOnt.Interval )
                .addProperty(timeOnt.hasBeginning, model.createResource()
                        .addProperty(RDF.type, timeOnt.Instant)
                        .addProperty(timeOnt.inXSDDateTime, model.createTypedLiteral(timeB)
                        ));
        }
        if (timeE!=null){
        interval.addProperty(timeOnt.hasEnd, model.createResource()
                        .addProperty(RDF.type, timeOnt.Instant)
                        .addProperty(timeOnt.inXSDDateTime, model.createTypedLiteral(timeE)
                        ));}
        
        //System.out.println(timeB);
        //System.out.println(timeE);
        }
        }
        catch (Exception e ) { 
            Logger.getLogger(ProfileParser.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(timeExpElements.text());
            
        }
        
    }

    private XSDDateTime parseToXSDDateTime(String timeString){
        Date date = null;
        timeString.replaceAll("\\s+","");
        List<String> stringList = new ArrayList<String>();
        stringList.add("MMMMMMMMMMMM dd, yyyy");
        stringList.add("MMMMMMMMMMMM yyyy");
        stringList.add("yyyy");
        stringList.add("yyyy      ");
        stringList.add("– yyyy");
        
        Calendar cal = Calendar.getInstance();
        XSDDateTime time=null;
        DateFormat df = null;
        
        for (String key: stringList){
            try {
               
                df =  new SimpleDateFormat(key);
                date = df.parse(timeString);
                
                if (date!=null) break;
            } catch (ParseException ex) {
                //Logger.getLogger(ProfileParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if (date==null) return null;
        
        cal.setTime(date);
        return new XSDDateTime(cal);
    }
    
    /*
    private XSDDateTime parseToXSDDateTime(String timeString) {
        Date date = null;
        DateFormat df = new SimpleDateFormat("MMMMMMMMMMMM dd, yyyy");
        
        Calendar cal = Calendar.getInstance();
        XSDDateTime time=null;
        try {
            date = df.parse(timeString);
        } catch (ParseException ex) {
            
            df = new SimpleDateFormat("MMMMMMMMMMMM yyyy");
            try {
                date = df.parse(timeString);
            } catch (ParseException ex1) {
                
            
            //Logger.getLogger(ProfileParser.class.getName()).log(Level.SEVERE, null, ex);
            df= new SimpleDateFormat("yyyy");
            try {
                date = df.parse(timeString);
            } catch (ParseException ex2) {
               return null; //Logger.getLogger(ProfileParser.class.getName()).log(Level.SEVERE, null, ex1);
            }
            }
            
        }
        cal.setTime(date);
        return new XSDDateTime(cal);
    }
    */

    private void addEducation(Elements select) {
        if (select!=null)
        for (Element element: select)
        addEducationElement(element);
    }

    private void addEducationElement(Element element) {
        String strInstitution, strDegree, strFOS, strGrade;
        strInstitution = "h4 a";
        strDegree = "h5 span.degree";
        strGrade = "h5 span.grade";
        strFOS = "h5 span.major a";
        Resource education = model.createResource();
        education.addProperty(RDFS.label, element.text());
        member.addProperty(Lont.hasEducation, education);
        Elements timeElements = element.select("span.education-date");
        addIntervalTime(education, timeElements);
        education.addProperty(Lont.fromShool, model.createResource("https://www.linkedin.com"+element.select(strInstitution).attr("href")).addProperty(RDFS.label, element.select(strInstitution).text()));
        education.addProperty(Lont.degreeTitle , substractLastChar(element.select(strDegree).text()));
        education.addProperty(Lont.degreeGrade , substractFirst2Char(element.select(strGrade).text()));
        Resource fieldOfstudy = model.createResource("https://www.linkedin.com"+element.select(strFOS).attr("href"));
        education.addProperty(Lont.ofField, fieldOfstudy);
        fieldOfstudy.addProperty(RDFS.label, element.select(strFOS).text());
    }
    
    private String substractLastChar(String str) {
    if (str != null && str.length() > 0) {
      str = str.substring(0, str.length()-1);
    }
    return str;
    }
    
    private String substractFirst2Char(String str) {
    if (str != null && str.length() > 0) {
      str = str.substring(2, str.length());
    }
    return str;
    }

    private void addSkills(Elements select) {
        for (Element element : select) addSkill(element);
    }

    private void addSkill(Element element) {
      Resource skill = model.createResource(element.select("a.endorse-item-name-text").attr("href"));
      member.addProperty(Lont.hasSkill, skill);
      skill.addProperty(RDFS.label, element.select("a.endorse-item-name-text").text());
    }

    private void addPublications(Elements select) {
        
        
        
    }

}
