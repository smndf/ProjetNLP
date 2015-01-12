package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.VP;

public class DirectivesAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
        int len = document.length();
        int begin = 0;
        int end = 0 + len;
        
        for (Annotation a : JCasUtil.select(jcas, Annotation.class)){
        	System.out.println("nouvelle annotation : " + a.getCoveredText());
            	for (POS posTag : JCasUtil.selectCovered(jcas, POS.class, a)){
            		
            		//System.out.println("pos : " + posTag.getCoveredText());
            		
                	if (posTag.getType().getShortName().equals("V")){	
                		System.out.println("instruction : " + posTag.getCoveredText());
                	}
                }    
//        	break;
        }
	}

}
