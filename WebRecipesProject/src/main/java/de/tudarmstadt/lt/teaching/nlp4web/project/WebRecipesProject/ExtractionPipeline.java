package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.DirectivesAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.annotator.IngredientsAnnotator;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.reader.WebPageReader;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.IngredientWriter;
import de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.writer.WebPageConsumer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class ExtractionPipeline {

	   public static void main(String[] args)
		    	throws UIMAException, IOException
		    {
		    			
		        CollectionReader reader = createReader(
		                WebPageReader.class,  WebPageReader.PARAM_URL, "http://allrecipes.com/recipe/alisons-slow-cooker-vegetable-beef-soup/"
		        );
		        
		        AnalysisEngine ingredientAnnotator = createEngine(
		        		//BreakIteratorSegmenter.class
	        		IngredientsAnnotator.class
		        );
		        
		        AnalysisEngine directivesAnnotator = createEngine(
		        		//BreakIteratorSegmenter.class
	        		DirectivesAnnotator.class
		        );
		        
		        AnalysisEngine seg = createEngine(
		        		//BreakIteratorSegmenter.class
		        		StanfordSegmenter.class
		        );

		/*        AnalysisEngine jazzy =
		        		createEngine
		        		(SpellChecker.class,
		        		SpellChecker.PARAM_MODEL_LOCATION,
		        		"/Users/Fanny/Documents/2014_2015_Darmstadt/NLP_and_the_Web/dict/words");*/
		        	
		        AnalysisEngine ingredientWriter = createEngine(
		                IngredientWriter.class
		        );
		   
		        
		        AnalysisEngine writer = createEngine(
		                WebPageConsumer.class
		        );

		        SimplePipeline.runPipeline(reader, /*ingredientAnnotator,*/ directivesAnnotator, /*seg, jazzy,*/ ingredientWriter);
		    }

}
