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

public class WebPageReader extends JCasCollectionReader_ImplBase {
	

	public static final String URL = "url";
	@ConfigurationParameter(
	name = URL,
	description = "The URL to read the webpages from",
	mandatory = true, defaultValue = "http://allrecipes.com/recipe/alisons-slow-cooker-vegetable-beef-soup/")
	private String url;
	
	int i = 0;
	int size = 0;
	Document doc;

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		size = 1;
		try {
			doc = Jsoup.connect(url).get();
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
		jcas.setDocumentLanguage("en");
		String text = doc.select("div[id=msgDirections]").text();
		jcas.setDocumentText(text);
		i++;
	}

}
