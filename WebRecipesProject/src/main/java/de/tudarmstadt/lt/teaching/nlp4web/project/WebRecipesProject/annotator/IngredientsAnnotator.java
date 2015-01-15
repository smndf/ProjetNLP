package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.NP;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.VP;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;

public class IngredientsAnnotator extends JCasAnnotator_ImplBase{
 
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String document = jcas.getDocumentText();
        int len = document.length();
        int begin = 0;
        int end = 0;
        
        String NOMBRE = "[[1-9][0-9]*]"; //| [0-9].[0-9]* | y \in Numbers_in_letters
        String FRACTION = "[[1-9]/[1-9]]";// | y \in Fraction_in_letters
        //String UNIT = y \in Measure_unit | y \in Measure_tools
        String QUANTITY = FRACTION+"|"+NOMBRE+"|["+NOMBRE+" "+FRACTION+"]";// | y \in Other_quantities
        
//        String REGEX1 = QUANTITY+" "+UNIT+" "+QUALIFIERS+" "+INGREDIENT;
//        String REGEX2 = QUANTITY+" "+QUALIFIERS+" "+INGREDIENT; 
//        String REGEX3 = UNIT+" "+QUALIFIERS+" "+INGREDIENT;
//        String REGEX4 = QUALIFIERS+" "+INGREDIENT;
//        String REGEX5 = INGREDIENT+" "+QUALIFIERS;
        /*
        boolean finished = false;
        
        String remainingText = document;
        String quantity = null;
        Matcher mQuantity = Pattern.compile(QUANTITY).matcher(remainingText);
        if (mQuantity.find()) {
        	// QUANTITY
        	quantity = mQuantity.group();
        	System.out.println("quantity : "+quantity);
        	remainingText 
        	Matcher mQuantity = Pattern.compile(QUANTITY).matcher(remainingText);
        }*/
        
        
        String amount = "[1-9][0-9]*";//"|[[1-9][0-9]]*[\\s?][1-9]/[1-9])";
        String amountUnit = "pounds?|cups?|teaspoons?|tablespoons?";
        String ingredient = "\\w*"; // supposed actually to be the next chunk
        String regex = "(("+amount+")( ("+amountUnit+"))?) ("+ingredient+")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(document);
        
		while (m.find()) {
			IngredientAnnotation a = new IngredientAnnotation(jcas);
			a.setBegin(m.start());
			a.setEnd(m.end());
			a.setAmount(m.group(1));
			a.addToIndexes();
			/*System.out.println("0 :"+m.group(0)+"\n"
					+"1 :"+m.group(1)+"\n"
					+"2 :"+m.group(2)+"\n"
					+"3 :"+m.group(3)+"\n"
					+"4 :"+m.group(4)+"\n"
					);
					*/
					
		}
		/*
		for (Annotation a : JCasUtil.select(jcas, Annotation.class)) {
			begin = a.getBegin();
			end = a.getEnd();
			if (a instanceof NP) {
					Pattern p = Pattern.compile("(.*)(such as)(.*)");
					Matcher m = p.matcher(a.getCoveredText());
					if (m.find()) {
						for (POS a1 : JCasUtil.selectCovered(
								jcas, POS.class,
								begin + m.start(3) , begin
										+ m.end(3))) {
							if (a1.getType().getShortName().equals("NN")) {
								HearstAnnotation hearstAnnotation = new HearstAnnotation(
										jcas, begin, end);
								List<Lemma> lemmas = JCasUtil.selectCovered(jcas,
										Lemma.class, begin + m.start(1),
										begin + m.end(1));
								hearstAnnotation.setHyperonym(lemmas.get(0).getValue());
								List<Lemma> lemmas1 = JCasUtil
										.selectCovered(
												jcas,
												Lemma.class,
												a1);
								if (lemmas1.size() == 1){
								hearstAnnotation.setHyponym(lemmas1
										.get(0).getValue());
								} else {
									hearstAnnotation.setHyponym(a1.getCoveredText());
								}
								hearstAnnotation.setTypeOf("NP0 such as NP");
								hearstAnnotation.addToIndexes();
							}
						}
					}
					Pattern p1 = Pattern.compile("(.*)(and other|or other)(.*)");
					Matcher m1 = p1.matcher(a.getCoveredText());
					if (m1.find()) {
						for (POS a1 : JCasUtil.selectCovered(
								jcas, POS.class,
								begin + m1.start(1) , begin
										+ m1.end(1))) {
							if (a1.getType().getShortName().equals("NN")) {
								HearstAnnotation hearstAnnotation = new HearstAnnotation(
										jcas, begin, end);
								List<Lemma> lemmas = JCasUtil.selectCovered(jcas,
										Lemma.class, begin + m1.start(3),
										begin + m1.end(3));
								hearstAnnotation.setHyperonym(lemmas.get(0).getValue());
								List<Lemma> lemmas1 = JCasUtil
										.selectCovered(
												jcas,
												Lemma.class,
												a1);
								if (lemmas1.size() == 1){
								hearstAnnotation.setHyponym(lemmas1
										.get(0).getValue());
								} else {
									hearstAnnotation.setHyponym(a1.getCoveredText());
								}
								hearstAnnotation.setTypeOf("NP and/or other NP0");
								hearstAnnotation.addToIndexes();
							}
						}
					}
			}
		}*/
	}
	
}
