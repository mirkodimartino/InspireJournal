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
public class LinkedinOntology {
    


private final String ontologyNamespace = "http://www.semanticweb.org/mirko/ontologies/2016/1/LinkedIn#";

public final Resource Member;
public final Resource ExperienceItem;
public final Property Summary;
public final Property hasExperience;
public final Property atTime;
public final Property atInstitution;
public final Property withRole;
public final Property subtitle;
public final Property hasEducation;
public final Property fromShool;
public final Property degreeTitle;
public final Property degreeGrade;
public final Property ofField;
public final Property hasSkill;

    public LinkedinOntology() {
        this.withRole = ResourceFactory.createProperty(ontologyNamespace+"withRole");
        this.atInstitution = ResourceFactory.createProperty(ontologyNamespace, "atInstitution");
        this.atTime = ResourceFactory.createProperty(ontologyNamespace, "atTime");
        this.hasExperience = ResourceFactory.createProperty(ontologyNamespace, "hasExperience");
        this.Summary = ResourceFactory.createProperty(ontologyNamespace, "Summary");
        this.ExperienceItem = ResourceFactory.createResource(ontologyNamespace+"ExperienceItem");
        this.Member = ResourceFactory.createResource(ontologyNamespace+"Member");
        this.subtitle = ResourceFactory.createProperty(ontologyNamespace+"memberSubtitle");
        this.hasEducation = ResourceFactory.createProperty(ontologyNamespace+"hasEducation");
        this.fromShool = ResourceFactory.createProperty(ontologyNamespace+"fromSchool");
        this.degreeTitle = ResourceFactory.createProperty(ontologyNamespace+"degreeTitle");
        this.degreeGrade = ResourceFactory.createProperty(ontologyNamespace+"degreeGrade");
        this.ofField = ResourceFactory.createProperty(ontologyNamespace+"ofField");
        this.hasSkill = ResourceFactory.createProperty(ontologyNamespace+"hasSkill");
        
    }
    
    
    
}
