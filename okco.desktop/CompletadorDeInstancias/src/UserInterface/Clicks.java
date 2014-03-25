package UserInterface;
import java.awt.Frame;
import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import com.hp.hpl.jena.ontology.OntModel;

import Business.FactoryModel;
import Business.Search;
import Domain.DtoInstanceRelation;
import Domain.IFactory;
import Domain.IRepository;

public class Clicks implements ViewerListener {
    protected boolean loop = true;
    
    Graph graph;
    public int numNodes;
    
    static IFactory Factory;
	static IRepository Repository;
	
	public Search search;
	public String NS;
	public OntModel model;
	
    public static void main(String args[]) {
        new Clicks();
    }
    public Clicks() {
        // We do as usual to display a graph. This
        // connect the graph outputs to the viewer.
        // The viewer is a sink of the graph.
        graph = new SingleGraph("Clicks");
        
		Factory = new FactoryModel();
		Repository = Factory.GetRepository();
		NS = "http://www.semanticweb.org/parentesco-simples.owl#";	
		String inputFileName = "Input/bateria1.owl";
		
		model = Repository.Open(inputFileName);

		search = new Search(NS);		
		
		String ind = NS + "f2";
		
        Node node = graph.getNode(ind);        
        if (node == null)
    	{
    		graph.addNode(ind);
    		node = graph.getNode(ind);
    	}
        
        node.addAttribute("ui.label", node.getId().split("#")[1] + "(Father)");
        
        /*ArrayList<DtoIndividualRelation> list = search.GetIndividualRelations(model, ind);
        
        for (DtoIndividualRelation dto : list) {
        	
        	Node nodeTarget = graph.getNode(dto.Target);
            if (nodeTarget == null)
        	{
        		graph.addNode(ind);
        		nodeTarget = graph.getNode(ind);
        	}
        	
        	String edgeName = ind + "-" + dto.Target;
        	graph.addEdge(edgeName, ind, dto.Target);
        	
        	Edge edge = graph.getEdge(edgeName);
        	edge.addAttribute("ui.label",dto.Property.split("#")[1]);
        	
        	node.addAttribute("ui.label", nodeTarget.getId().split("#")[1] + "(" + dto.TargetClass.split("#")[1] + ")");
		}*/
        
        //graph.addNode("B" );
        //graph.addNode("C" );
        //graph.addEdge("AB", "A", "B");
        //graph.addEdge("BC", "B", "C");
        //graph.addEdge("CA", "C", "A");
        
        numNodes = 3;
        
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        
        
        //Old
        //Viewer viewer = graph.display();

        FrameTeste frame = new FrameTeste();
        View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
        frame.add(view);        
		frame.setVisible(true);
		
		frame.remove(view);
		viewer.close();
		
        // The default action when closing the view is to quit
        // the program.
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
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
            System.out.println('O');
            // here your simulation code.
 
            // You do not necessarily need to use a loop, this is only an example.
            // as long as you call pump() before using the graph. pump() is non
            // blocking.  If you only use the loop to look at event, use blockingPump()
            // to avoid 100% CPU usage.
        }
    }
 
	public void viewClosed(String id) {
        loop = false;
    }
 
    public void buttonPushed(String id) {
        
        Node node = graph.getNode(id);

        ArrayList<DtoInstanceRelation> list = search.GetInstanceRelations(model, node.getId());
        
        try {
        	
        	if (list != null)
            {
    	        for (DtoInstanceRelation dto : list) {
    	        	graph.addNode(dto.Target);
    	        	
    	        	String edgeName = node.getId() + "-" + dto.Target;
    	        	graph.addEdge(edgeName,node.getId(), dto.Target);
    	        	
    	        	Edge edge = graph.getEdge(edgeName);
    	        	edge.addAttribute("ui.label",dto.Property.split("#")[1]);
    	        	Node nodeTarget = graph.getNode(dto.Target);
    	        	nodeTarget.addAttribute("ui.label", nodeTarget.getId().split("#")[1] + "(" + dto.TargetClass.split("#")[1] + ")");
    			}
            }
			
		} catch (Exception e) {
			// TODO: handle exception
		}
        
    }
 
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }
}