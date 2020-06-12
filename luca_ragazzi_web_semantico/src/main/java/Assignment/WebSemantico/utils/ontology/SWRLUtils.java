package Assignment.WebSemantico.utils.ontology;

import java.util.Collection;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;

import Assignment.WebSemantico.utils.ontology.exception.LoadedOntoNotSelected;

public class SWRLUtils extends LoadMainOntology {
	
	//##Those methods can be used only after have called the selectOntology.
	public static SWRLVariable createSwrlVariable(IRI ruleIRI) throws LoadedOntoNotSelected {
		return createSwrlVariable(getLoadedOnto(), ruleIRI);
	}
	
	public static SWRLClassAtom createSwrlClassAtom(OWLClassExpression predicate, SWRLIArgument arg) throws LoadedOntoNotSelected {
		return createSwrlClassAtom(getLoadedOnto(), predicate, arg);
	}
	
	public static SWRLRule createAnonymousSwrlRule(Collection<? extends SWRLAtom> body, Collection<? extends SWRLAtom> head) throws LoadedOntoNotSelected {
		return createAnonymousSwrlRule(getLoadedOnto(), body, head);
	}
	
	public static SWRLObjectPropertyAtom createSWRLObjectPropertyAtom(OWLObjectPropertyExpression property, SWRLIArgument arg0, SWRLIArgument arg1) throws LoadedOntoNotSelected {
		return createSWRLObjectPropertyAtom(getLoadedOnto(), property, arg0, arg1);
	}
	//###############################################################################
	
	public static SWRLVariable createSwrlVariable(OWLOntologyWithTools o, IRI ruleIRI) {
		return o.getDataFactory().getSWRLVariable(ruleIRI);
	}
	
	public static SWRLClassAtom createSwrlClassAtom(OWLOntologyWithTools o, OWLClassExpression predicate, SWRLIArgument arg) {
		return o.getDataFactory().getSWRLClassAtom(predicate, arg);
	}
	
	public static SWRLRule createAnonymousSwrlRule(OWLOntologyWithTools o, Collection<? extends SWRLAtom> body, Collection<? extends SWRLAtom> head) {
		return o.getDataFactory().getSWRLRule(body, head);
	}
	
	public static SWRLObjectPropertyAtom createSWRLObjectPropertyAtom(OWLOntologyWithTools o, OWLObjectPropertyExpression property, SWRLIArgument arg0, SWRLIArgument arg1) {
		return o.getDataFactory().getSWRLObjectPropertyAtom(property, arg0, arg1);
	}
}
