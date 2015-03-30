package br.com.padtec.okco.core.application;

public class OKCoComponents {

	public static OKCoUploader repository = new OKCoUploader();
	public static OKCoSelector selector = new OKCoSelector(repository);
	public static OKCoVisualizer visualizer = new OKCoVisualizer(repository);
	public static OKCoReasoner reasoner = new OKCoReasoner(repository, selector);
	public static OKCoClassifier classifier = new OKCoClassifier(repository, selector);
	public static OKCoCommiter commiter = new OKCoCommiter(repository, selector, reasoner);
}
