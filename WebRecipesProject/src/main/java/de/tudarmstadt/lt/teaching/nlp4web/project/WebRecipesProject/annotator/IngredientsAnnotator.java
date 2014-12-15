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

public class IngredientsAnnotator extends JCasAnnotator_ImplBase{

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		int begin = 0;
		int end = 0;
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
					Pattern p2 = Pattern.compile("(such\\s)(.+)(as)(.*)");
					Matcher m2 = p2.matcher(a.getCoveredText());
					if (m2.find()) {
						for (POS a1 : JCasUtil.selectCovered(
								jcas, POS.class,
								begin + m2.start(3) , begin
										+ m2.end(3))) {
							if (a1.getType().getShortName().equals("NN")) {
								HearstAnnotation hearstAnnotation = new HearstAnnotation(
										jcas, begin, end);
								List<Lemma> lemmas = JCasUtil.selectCovered(jcas,
										Lemma.class, begin + m2.start(1),
										begin + m2.end(1));
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
								hearstAnnotation.setTypeOf("such NP0 as NP");
								hearstAnnotation.addToIndexes();
							}
						}
					}
			}
			if (a instanceof VP) {
				Pattern p = Pattern.compile("(\\w*)( including| especially)(.*)");
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
							hearstAnnotation.setTypeOf("NP0 including/ especially NP");
							hearstAnnotation.addToIndexes();
						}
					}
				}
			}
		}
	}
	
}
