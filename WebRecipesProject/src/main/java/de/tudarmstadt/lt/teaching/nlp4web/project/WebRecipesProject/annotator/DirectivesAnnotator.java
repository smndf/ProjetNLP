package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.VP;
import de.tudarmstadt.ukp.teaching.general.type.DirectivesAnnotation;
import de.tudarmstadt.ukp.teaching.general.type.IngredientAnnotation;


public class DirectivesAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String doc = jcas.getDocumentText();
		String[] documents = doc.split("\n");
		int len = documents[1].length();
		int begin = documents[0].length();
		int end = begin + len;

		try {
			JWNL.initialize(new FileInputStream("src/main/resources/properties.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dictionary dictionary = Dictionary.getInstance();
		HashSet<String> hSet = new HashSet<String>();
		IndexWord indexWord = null;
		try {
			indexWord = dictionary.lookupIndexWord(POS.NOUN, "beef");
			System.out.println("indexword : " + indexWord);
			if (indexWord != null) {
				Synset[] set = indexWord.getSenses();
				if (set != null) {
					for (Synset s:set) {
						Pointer[] pointerArr = s.getPointers(PointerType.HYPERNYM);
						if (pointerArr != null)
							for (Pointer x : pointerArr) {
								for (Word w:x.getTargetSynset().getWords()){
									hSet.add(w.getLemma());
								}
							}
					}
				}
			}
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("hypernym de beef : " + hSet);



		for (Annotation a : JCasUtil.selectCovered(jcas, Annotation.class, begin, end)){

			if (a.getType().getShortName().equals("VP")){

				DirectivesAnnotation d = new DirectivesAnnotation(jcas);
				d.setBegin(a.getBegin());
				d.setEnd(a.getEnd());
				//List<String> ingredients = new ArrayList<String>();
				StringList ingredients = new StringList(jcas);
				for (Annotation b : JCasUtil.selectCovered(jcas, Annotation.class, a.getBegin(), a.getEnd())){
					if (b.getType().getShortName().equals("V")){
						d.setInstruction(b.getCoveredText());
					}
					if (b.getType().getShortName().equals("NN")){
						d.setIngredient(ingredients.);
					}

				}
				d.addToIndexes();
			}  

			if (a.getType().getShortName().equals("")){
				for (String s : hset){

					DirectivesAnnotation d = new DirectivesAnnotation(jcas);
					d.setBegin(a.getBegin());
					d.setEnd(a.getEnd());
					d.setInstruction(a.getCoveredText());
					d.addToIndexes();
				}
			}
		}
	}    
}
