package SwingUserInterface;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
import Business.Search;
import Domain.DtoInstanceRelation;

import com.hp.hpl.jena.ontology.OntModel;

public class GraphIndividual extends Thread implements ViewerListener {
	
	/*
	 * Class for construct the individual graph.
	 * Her we construct the graph and calls the IndividualFrame to show the graph
	 * */
	
    protected boolean loop = true;
    
    private Graph graph;
    private Search search;
    private OntModel model;
    private String individualURI;
    private String individualClass;
    private IndividualFrameGraph frame;
    private Viewer viewer;
    private View view;
    protected Semaphore semaphore;
 
    public GraphIndividual(OntModel model, Search search, String individualURI, String individualClass) {
        
    	this.search = search;
    	this.model = model;
    	this.individualURI = individualURI;
    	this.individualClass = individualClass;
    	this.start();    	
	}
    
    public void run(){ 
    	
    	// We do as usual to display a graph. This
        // connect the graph outputs to the viewer.
        // The viewer is a sink of the graph.
    	
        graph = new SingleGraph("GraphIndividual");
        
        Node node = graph.getNode(individualURI);
        
        if (node == null)
    	{
    		graph.addNode(individualURI);
    		node = graph.getNode(individualURI);
    	}
        
        node.addAttribute("ui.label", individualURI.split("#")[1] + " (" + individualClass.split("#")[1] + ")");
        
        ArrayList<DtoInstanceRelation> list = search.GetInstanceRelations(model, individualURI);
        
        for (DtoInstanceRelation dto : list) {
        	
        	node = graph.getNode(dto.Target);
        	if (node == null)
        	{
        		graph.addNode(dto.Target);
        		node = graph.getNode(dto.Target);
        	}
        	        	
        	String edgeName = individualURI + "-" + dto.Target;
        	graph.addEdge(edgeName, individualURI, dto.Target);
        	
        	Edge edge = graph.getEdge(edgeName);
        	edge.addAttribute("ui.label",dto.Property.split("#")[1]);
        	
        	node.addAttribute("ui.label", node.getId().split("#")[1] + "(" + dto.TargetClass.split("#")[1] + ")");
		}       
		
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view = viewer.addDefaultView(false);   // false indicates "no JFrame".
        frame = new IndividualFrameGraph();
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        
		    	viewClosed();
		    }
		});
        frame.add(view);        
		frame.setVisible(true);
		
        
        //Viewer viewer = graph.display();
        //System.out.println("DEU ONDA");
 
        // The default action when closing the view is to quit
        // the program.
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
        viewer.enableAutoLayout();
 
        // We connect back the viewer to the graph,
        // the graph becomes a sink for the viewer.
        // We also install us as a viewer listener to
        // intercept the graphic events.
        ViewerPipe fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener((ViewerListener) this);
        fromViewer.addSink(graph);
 
        // Then we need a loop to do our work and to wait for events.
        // In this loop we will need to call the
        // pump() method before each use of the graph to copy back events
        // that have already occurred in the viewer thread inside
        // our thread.
 
        while(loop) {
            fromViewer.pump(); // or fromViewer.blockingPump();
            // System.out.println("Uhu");
            
            // here your simulation code.
 
            // You do not necessarily need to use a loop, this is only an example.
            // as long as you call pump() before using the graph. pump() is non
            // blocking.  If you only use the loop to look at event, use blockingPump()
            // to avoid 100% CPU usage.
        }
 
    	//The release happen on the frame button click	//semaphore.release();
    }
 
    public void viewClosed() {
    	
        loop = false;
        System.out.println("FECHO FRAME DO GRAFICO");
        frame.dispose();
        
        viewer.close();
        
                
        try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    public void buttonPushed(String id) {

    	Node nodeClicked = graph.getNode(id);
    	
    	System.out.println("Clicou em " + nodeClicked.getId());

        ArrayList<DtoInstanceRelation> list = search.GetInstanceRelations(model, nodeClicked.getId());
        
        try {
        	
        	if (list != null)
            {
    	        for (DtoInstanceRelation dto : list) {
    	        	
    	        	Node node = graph.getNode(dto.Target);
    	        	if (node == null)
    	        	{
    	        		graph.addNode(dto.Target);
    	        		node = graph.getNode(dto.Target);
    	        	}
    	        	
    	        	String edgeName = nodeClicked.getId() + "-" + dto.Target;
    	        	graph.addEdge(edgeName,nodeClicked.getId(), dto.Target);
    	        	
    	        	Edge edge = graph.getEdge(edgeName);
    	        	edge.addAttribute("ui.label",dto.Property.split("#")[1]);
    	        	
    	        	node.addAttribute("ui.label", node.getId().split("#")[1] + "(" + dto.TargetClass.split("#")[1] + ")");
    			}
            }
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
 
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }

	public void finalizeFrame() {
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void viewClosed(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("FECHO com OVERRIDE");
		
	}
}