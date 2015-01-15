package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
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

		// we are processing the Unit patterns sentence by sentence
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			
			// for all cardinal number in this sentence
			for (CARD number : JCasUtil.selectCovered(jcas, CARD.class, sentence)) {
				if(JCasUtil.selectCovered(CARD.class, number).size()>0){
					continue;
				}
				try {

					// check the next NC (noun chunk)
					NP unit_ingredient = JCasUtil.selectFollowing(jcas, NP.class, number, 1)
							.get(0);
					{
						// get the list of tokens between the number and the chunk
						List<Token> tokens = JCasUtil.selectCovered(jcas,
								Token.class, number.getEnd(), unit_ingredient.getBegin());

						setUnitAnnotation(jcas, number, unit_ingredient, "");
						/*
						 * if one of the following conditions apply, we have a
						 * Unit pattern
						 */
						/*if (tokens != null && tokens.size() == 2) {

							if (tokens.get(0).getCoveredText().equals("such")&& tokens.get(1).getCoveredText().equals("as")) {

								setUnitAnnotation(jcas, number, unit_ingredient, "NP0 such as Np");

							} // if: "such as"
							else if (tokens.get(0).getCoveredText().equals(",")	&& tokens.get(1).getCoveredText().equals("especially")) {

								setUnitAnnotation(jcas, number, unit_ingredient, "NP0, especially NP");

							} // if: ", especially"
							else if (tokens.get(0).getCoveredText().equals(",")	&& tokens.get(1).getCoveredText().equals("including")) {

								setUnitAnnotation(jcas, number, unit_ingredient, "NP0, including NP");

							} // if: ", including"
												
							
						} // if: there are two tokens between two noun chunks
						else if (tokens != null && tokens.size() == 1) {

							// the "other " is encoded in the NP/NC
							if ((tokens.get(0).getCoveredText().equals("and")
									|| tokens.get(0).getCoveredText().equals("or")) 
									&& unit_ingredient.getCoveredText().startsWith("other ")) {

								setUnitAnnotation(jcas, unit_ingredient, number, "NP and|or other NP0");

							} // if: "and/or other"
							// the "such" is encoded in the NP/NC
							else if (tokens.get(0).getCoveredText().equals("as")
									 && number.getCoveredText().startsWith("such")) {

								setUnitAnnotation(jcas, number, unit_ingredient, "such NP0 as NP");

							} // such NP0 as NP
							
						} // one token
					*/	
					}
					} catch (IndexOutOfBoundsException e) {

						// empty select() calls arrive here
						
					} // catch

				} // for all noun chunks in the sentence

			} // for all sentences

		} // process
	
	public void setUnitAnnotation(JCas jcas, CARD number, NP unit_ingredient, String type) {

		// dive into the noun chunks and get the nouns
		N noun = JCasUtil.selectCovered(jcas, N.class, unit_ingredient).get(0);

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
		a.setUnit(lemma.getValue());
		a.addToIndexes();

	} // setUnitAnnotation()


}
