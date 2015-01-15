package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.VP;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class DirectivesAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
        int len = document.length();
        int begin = 0;
        int end = 0 + len;
                
        for (Annotation a : JCasUtil.select(jcas, Annotation.class)){
        	
        	if (a.getType().getShortName().equals("V")){
        		System.out.println("nouvelle annotation : " + a.getCoveredText());
            	System.out.println("type : " + a.getType().getShortName());
            	//for (POS posTag : JCasUtil.selectCovered(jcas, POS.class, a)){
            		System.out.println(" create annotation instruction : " + a.getCoveredText());
            		DirectivesAnnotation d = new DirectivesAnnotation(jcas);
        			d.setBegin(a.getBegin());
        			d.setEnd(a.getEnd());
        			d.setInstruction(a.getCoveredText());
        			d.addToIndexes();
            	//}
        	}            		
//        	break;
        }
	}

}
