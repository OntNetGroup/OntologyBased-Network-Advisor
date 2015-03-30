package br.com.padtec.advisor.core.application;

import br.com.padtec.okco.core.application.OKCoComponents;

public class AdvisorComponents {
	
	public static AdvisorService service = new AdvisorService(OKCoComponents.repository);
	public static Visualizator visualizator = new Visualizator(OKCoComponents.repository,AdvisorComponents.service);
	public static SubRelationEnforcer enforcer = new SubRelationEnforcer(OKCoComponents.repository);
	
	public static GeneralBinds binds = new GeneralBinds(OKCoComponents.repository);
	public static BindsVisualizator bindsVisualizator = new BindsVisualizator(OKCoComponents.repository,binds);
	
	public static GeneralConnects connects = new GeneralConnects(OKCoComponents.repository);
	public static ConnectsVisualizator connectsVisualizator = new ConnectsVisualizator(OKCoComponents.repository,connects);
		
	public static SindelUploader sindelUploader = new SindelUploader(OKCoComponents.repository, OKCoComponents.reasoner);
}
