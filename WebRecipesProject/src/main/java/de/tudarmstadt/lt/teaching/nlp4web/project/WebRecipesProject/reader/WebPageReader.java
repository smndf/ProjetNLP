package de.tudarmstadt.lt.teaching.nlp4web.project.WebRecipesProject.reader;

import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPageReader extends JCasCollectionReader_ImplBase {
	

	/*public static final String PARAM_SELECTOR = "SELECTOR";
	@ConfigurationParameter(name = PARAM_SELECTOR,
			description = "css element to analyse",
			mandatory = false, defaultValue = "body")
	private String cssSelector;
	*/
	
	public static final String PARAM_LANGUAGE = "LANGUAGE";
	@ConfigurationParameter(name = PARAM_LANGUAGE,
			description = "default language for the text",
			mandatory = false, defaultValue = "en")
	private String language;
	
	public static final String PARAM_URL = "url";
	@ConfigurationParameter(
	name = PARAM_URL,
	description = "The URL to read the webpages from",
	mandatory = true)
	private String url;
	
	int i = 0;
	int size = 0;
	Document docRecipe;
	Document docIngredients;
	Document docTitle;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		size = 1;
		try {
			docRecipe = Jsoup.connect(url).get();
			docIngredients = Jsoup.connect(url).get();
			docTitle = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(i, size, Progress.ENTITIES) };
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return i < size;
	}

	@Override
	public void getNext(JCas jcas) throws IOException, CollectionException {
		jcas.setDocumentLanguage(language);
		
		String titleRecipe = docTitle.select("h1").text(); // proposition : select("title").text()
		String textRecipe = docRecipe.select("span.plaincharacterwrap").text();
		
		// old version : String textIngredients = docIngredients.select("ul.ingredient-wrap").text();
		// Fetch the ingredients one by one
		String textIngredients = "";
		Elements elts = docIngredients.select("p.fl-ing");
		for (Element e : elts) {
			textIngredients += e.text()+".\n";
		}
		
		//jcas.setDocumentText(textIngredients);//recipe);
		String docText = titleRecipe + "\n" + textRecipe + "\n" + textIngredients ;
		jcas.setDocumentText(docText);
		i++;
	}

}
