package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;

public class DirectivesAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
        int len = document.length();
        int begin = 0;
        int end = 0 + len;
        
        System.out.println("debut");
        for (Annotation a : JCasUtil.select(jcas, Annotation.class)){
            System.out.println("annotation : " + a.getCoveredText());
        	for (POS posTag : JCasUtil.selectCovered(jcas, POS.class, a.getBegin(), a.getBegin() + 20)){
        		System.out.println("pos");
            	if (posTag.getType().getShortName().equals("V")){
            		System.out.println("coucou : " + posTag.getCoveredText());
            	}
            }        	
        }
	}

}
