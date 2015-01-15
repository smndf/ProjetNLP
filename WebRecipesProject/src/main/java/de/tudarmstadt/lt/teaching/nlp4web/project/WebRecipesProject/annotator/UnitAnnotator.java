package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.NP;
import de.tudarmstadt.ukp.teaching.general.type.UnitAnnotation;

/**
 * Examples :
 *  1 teaspoon
 *  1/2 onion
 *  1 cup
 * 
 * @author Solene, Killian
 *
 */
public class UnitAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		splitByToken(jcas);
		
		splitByFollowingNP(jcas);
		splitByCoveredNP(jcas);
		
	} // process
	
	private void splitByFollowingNP(JCas jcas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub

		// we are processing the Unit patterns sentence by sentence
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {

			// for all cardinal number in this sentence
			for (CARD number : JCasUtil.selectCovered(jcas, CARD.class,
					sentence)) {
				if (JCasUtil.selectCovered(CARD.class, number).size() > 0) {
					continue;
				}
				try {

					// check the next NC (noun chunk)
					NP unit_ingredient = JCasUtil.selectFollowing(jcas,
							NP.class, number, 1).get(0);
					System.out.println("unit_ingredient : "
							+ unit_ingredient.getCoveredText());
					// Idea? : check the (longest?) NP (noun chunk) that covers
					// the number

					{
						// get the list of tokens between the number and the
						// chunk
						List<Token> tokens = JCasUtil.selectCovered(jcas,
								Token.class, number.getEnd(),
								unit_ingredient.getBegin());

						setUnitAnnotation(jcas, number, unit_ingredient,
								"splitByFollowingNP");
					}
				} catch (IndexOutOfBoundsException e) {

					System.out.println("IndexOutOfBoundsException");
					// empty select() calls arrive here

				} // catch

			} // for all noun chunks in the sentence

		} // for all sentences

	}
	
	private void splitByCoveredNP(JCas jcas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub

		// we are processing the Unit patterns sentence by sentence
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {

			// for all cardinal number in this sentence
			for (CARD number : JCasUtil.selectCovered(jcas, CARD.class,
					sentence)) {
				if (JCasUtil.selectCovered(CARD.class, number).size() > 0) {
					continue;
				}
				try {

					// check the covered NP (noun chunk)
					NP unit_ingredient = JCasUtil.selectCovered(jcas,
							NP.class, number.getBegin(), jcas.getDocumentText().length()).get(0);
					System.out.println("unit_ingredient : "
							+ unit_ingredient.getCoveredText());
					// Idea? : check the (longest?) NP (noun chunk) that covers
					// the number

					{
						// get the list of tokens between the number and the
						// chunk
						List<Token> tokens = JCasUtil.selectCovered(jcas,
								Token.class, number.getEnd(),
								unit_ingredient.getBegin());

						setUnitAnnotation(jcas, number, unit_ingredient,
								"splitByCoveredNP");
					}
				} catch (IndexOutOfBoundsException e) {

					System.out.println("IndexOutOfBoundsException");
					// empty select() calls arrive here

				} // catch

			} // for all noun chunks in the sentence

		} // for all sentences

	}

	private void splitByToken(JCas jcas) throws AnalysisEngineProcessException {
		// we are processing the Unit patterns sentence by sentence
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			
			// for all cardinal number in this sentence
			for (CARD number : JCasUtil.selectCovered(jcas, CARD.class, sentence)) {
				if(JCasUtil.selectCovered(CARD.class, number).size()>0){
					continue;
				}
				try {

					// check the next NC (noun chunk)
					Token unit_ingredient = JCasUtil.selectFollowing(jcas, Token.class, number, 1).get(0);
					System.out.println("unit_ingredient : "+unit_ingredient.getCoveredText());
					// Idea? : check the (longest?) NP (noun chunk) that covers the number
					
					{
						// get the list of tokens between the number and the chunk
						List<Token> tokens = JCasUtil.selectCovered(jcas,
								Token.class, number.getEnd(), unit_ingredient.getBegin());

						setUnitAnnotation(jcas, number, unit_ingredient, "splitByToken");
											}
					} catch (IndexOutOfBoundsException e) {

						System.out.println("IndexOutOfBoundsException");
						// empty select() calls arrive here
						
					} // catch

				} // for all noun chunks in the sentence

			} // for all sentences
	}

	private void setUnitAnnotation(JCas jcas, CARD number, Token unit_ingredient,
			String type) {
		
		// get the lemmata
				Lemma lemma = JCasUtil.selectCovered(jcas, Lemma.class, unit_ingredient.getBegin(),
						unit_ingredient.getEnd()).get(0);
		/*
		 * create a new UnitAnnotation
		 */
		UnitAnnotation a = new UnitAnnotation(jcas);
		a.setBegin(number.getBegin());
		a.setEnd(unit_ingredient.getEnd());
		a.setTypeOf(type);
		a.setQuantity(number.getCoveredText());
		a.setUnit(unit_ingredient.getLemma().getValue());
		a.addToIndexes();

	} // setUnitAnnotation()

	public void setUnitAnnotation(JCas jcas, CARD number, NP unit_ingredient, String type) {

		// dive into the noun chunks and get the nouns
		N noun = JCasUtil.selectCovered(jcas, N.class, unit_ingredient).get(0);

		// get the lemmata
		Lemma lemma = JCasUtil.selectCovered(jcas, Lemma.class, noun.getBegin(),
				noun.getEnd()).get(0);

		/*
		 * create a new UnitAnnotation
		 */
		UnitAnnotation a = new UnitAnnotation(jcas);
		a.setBegin(number.getBegin());
		a.setEnd(unit_ingredient.getEnd());
		a.setTypeOf(type);
		a.setQuantity(number.getCoveredText());
		a.setUnit(lemma.getValue());
		a.addToIndexes();

	} // setUnitAnnotation()


}
