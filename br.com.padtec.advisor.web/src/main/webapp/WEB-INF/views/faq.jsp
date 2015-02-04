<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@include file="../templates/header.jsp"%>

<style>

	.list-questions li
	{
		margin-top:10px;
	}
	.answer
	{
	margin-top:5px;
	}

</style>

<script type="text/javascript">

	//Variables to control specialization properties
	
	var ablePrev = false; //begnning
	var ableNext = true;  //begnning

	$(document).ready(function() {

		$(".answer").hide();

		//Previous bottom click
		$('.question').live('click', function() {		

			//$(this).parent().children(".answer").slideDown();
			if ( $(this).parent().children(".answer").is( ":hidden" ) ) {
				$(this).parent().children(".answer").slideDown();
			  } else {
				  $(this).parent().children(".answer").hide();
			  }
			
			
		}); // End - btn-prev
	});


</script>

	<h1>FAQ</h1>

	<div class="row">
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h2>
						<i class="icon-edit"></i>Questions
					</h2>
					<div class="box-icon">
						 <a	href="#" class="btn-minimize"><i class="icon-chevron-up"></i></a>
					</div>
				</div>
				<div class="box-content">
					
					<ol class="list-questions">
						  <li><a class="question" href="#">Why the Advisor is built over OWL?</a> <div class="answer">OWL is the most expressive Open World Assumption language with associated reasoning capabilities. It is highly used and it is the W3Cs current standard for knowledge representation at the semantic web.</div> </li>
						  <li><a class="question" href="#">How the Knowledge Completion Functionality works?</a> <div class="answer">The Knowledge Completion Functionality (called TN-OKCo) searches for instances that do not satisfy their belonging classes' assertions. It uses class axioms to verify incomplete knowledge over asserted individuals. OKCo does not intent to modify in any way class descriptions. To verify for unsatisfied instances, it performs eleven different queries, implemented on SPARQL, over an OWL input file</div> </li>
						  <li><a class="question" href="#">Why two different reasoners are available?</a> <div class="answer">The knowledge-completion (TN-OKCo) uses the OWL reasoners capabilities of classification, consistency checking and inference to result a better knowledge completion to the user. Two different reasoner options are provided to users: Pellet (version 2.3.1) and HermiT (version 1.3.8). The existence of two different options is justified by the different characteristics of these reasoners. Pellet implements a tableau-based algorithm, while HermiT implements a faster algorithm, the hypertableau calculus. However, Pellet has an incremental reasoning, i.e., it supports incremental classification and incremental consistency check for additions and removals.</div> </li>
						  <li><a class="question" href="#">Which are the Knowledge Completion functionalities?</a> <div class="answer">TN-OKCo performs eleven different knowledge completion checks over an OWL ontology. These are: (i) object and (ii) data properties minimum cardinality restriction (minCardinality), (iii) object and (iv) data properties maximum cardinality restriction (maxCardinality), (v) object and (vi) data properties exactly cardinality restriction (cardinality), (vii) object and (viii) data properties some cardinality restriction (someValuesFrom), (ix) class partitions and (x) object and (xi) data properties specializations.</div> </li>
						  <li><a class="question" href="#">Are we talking about which kind of completion?</a> <div class="answer">It is important to make clear that completion, in TN-OKCo's context actually does not mean really to complete all known domain knowledge: it means to, with the asserted individuals, to satisfy all ontology axioms. As an example, let us consider an OWL ontology where the class Mother is defined as equivalent of a Person that have at least one children, i.e., in order to be a Mother, a Person needs to have a minimum of one children (but no upper limit is provided to the number of sons a mother can have). Let us also suppose that, in a given domain, a Person called Mary have two distinct children, named Peter and Paul. Considering a scenario where it is stated the existence of Mary (asserted as a Mother) in an OWL ontology, but there is no information about its children, TN-OKCo will indicate this concept as not complete, as every Mother need to have at least one children. However, TN-OKCo will consider the ontology complete if only the children called Paul is declared, despite the real existence of Peter. This happens because the known asserted individuals at the ontology satisfy the axiom that every mother needs at least a child.</div> </li>
						  <li><a class="question" href="#">What are Ontologies?</a> <div class="answer">The Network Advisor uses an OWL ontology as its knowledge base. The term Ontology came up in Philosophy meaning a systematic explanation of the being. In computing, while used as 'an explicit specification of a conceptualization' <a target="_blank" href="http://www.dbis.informatik.hu-berlin.de/dbisold/lehre/WS0203/SemWeb/lit/KSL-92-17.pdf">(GRUBER, 1993)</a>, ontologies are used to provide a large number of resources for intelligent systems as well as for knowledge representation and reasoning in general <a target="_blank" href="http://books.google.com.br/books/about/Model_Driven_Engineering_and_Ontology_De.html?id=s-9yu7ubSykC&redir_esc=y">(GASEVIC; DJURIC; DEVEDZIC, 2009)</a>.</div> </li>
						  <li><a class="question" href="#">Which are the concepts in the Advisor's Knowledge Base?</a> <div class="answer">The Ontology-based Network Advisor knowledge base is an OWL ontology. A three-layered ontology is used to provide different levels of abstraction. <br><br> The lower level of this knowledge base is the Recommendation ITU-T G.800 Ontology. This level contains the most specific elements that composes a transport network. The second level of abstraction is the Simple Equipment Ontology, which presents network elements with a higher level of abstraction: equipment and interfaces. The third level of abstraction is the Simple Site Ontology, used for grouping equipment over sites, for more abstract network visualization.</div> </li>
						  <li><a class="question" href="#">Why use the Recommendation ITU-T G.800?</a> <div class="answer">The <a target="_blank" href="http://www.itu.int/ITU-T/recommendations/rec.aspx?rec=11489">Recommendation ITU-T G.800</a> provides a set of constructs (definitions and diagrammatic symbols) and the semantics that can be used to describe the functional architecture of transport networks in a technology-independent way. <br><br> The generic functional architecture provides the basis for a harmonized set of functional architecture Recommendations for specific layer network technologies, including those that use connection-oriented circuit switching or connection-oriented packet switching, and connectionless packet switching (e.g. ITU-T G.803, ITU-T G.872, ITU-T I.326, ITU-T G.8010/Y.1306), and a corresponding set of Recommendations for management, performance analysis, and equipment specifications  <a target="_blank" href="http://www.itu.int/ITU-T/recommendations/rec.aspx?rec=11489">(ITU-T, 2012a)</a>. In practice, it unifies concepts from the ITU-T Recommendations G.805, for connection oriented networks, and from the G.809, for connectionless networks.</div> </li>
						  <li><a class="question" href="#">How can I declare network information to the Advisor?</a> <div class="answer">The Ontology-based Transport Network Advisor processes OWL instances, according a class structuring (the knowledge base). When using the Advisor, the user must provide the individual's information. This information consists of a network declaration, which can be formatted in three different ways: (i) in an OWL file with instances, or by using one of the network description Domain Specific Languages (DSL) created specifically for the Advisor, that are (ii) the Simple Network Description Language (Sindel) or (iii) the Complete Network Description Language (Condel).</div> </li>
						  <li><a class="question" href="#">Why an OWL option is available for the Advisor's instantiation?</a> <div class="answer">The use of an OWL file with instances as input have a direct advantage over the use of one of the DSLs created specifically for the Network Advisor: it has no loss of information and can capture all necessary information to be processed by the Advisor. This happens because no language transformation should be done. However, it has a significant disadvantage over the DSLs: its usability by the network operator, which, besides the domain knowledge, must also have knowledge about the OWL structure and how to use it.</div> </li>
						  <li><a class="question" href="#">What is Sindel?</a> <div class="answer">The Simple Network Description Language, or Sindel, for short, is a network declaration Domain Specific Language built over the ontology used as basis for the Advisor. The Sindel language is intended to be an easy and quick way to instantiate the ontology with a network description. <br><br> With relation to the domain elements (mainly the Recommendation ITU-T G.800), Sindel do not have support of representation to all architectural elements and its relations, it can only represent the most important classes, relations and attributes. In addition, the transformation between Sindel and the OWL instances can result in loss of information, as some Sindel constructs are much more abstract. Nevertheless, a valid Sindel specification must obey a more precise metamodel than a Condel specification, what makes it less error-prone in domain representations (although Sindel is certainly not error free with relation to domain representations).</div> </li>
						  <li><a class="question" href="#">How to write a Sindel specification?</a> <div class="answer">Sindel's abstract syntax (i.e., its metamodel) is defined in Unified Modeling Language's (UML) class diagrams. Its concrete syntax (i.e., its grammars) is defined with Extended Backus-Naur Form (EBNF) notation. Click on links to visualize them.</div> </li>
						  <li><a class="question" href="#">What is Condel?</a> <div class="answer">The Complete Network Description Language, or Condel, for short, can be used as an input, providing network declarations, or as an output for the Network Advisor. It was designed in order to reduce information loss between the network declaration and the OWL ontology used by the Advisor. <br><br> A Sindel declaration has a significant difference in expressivity (the capacity of representing information) in relation to the network OWL file used by the Advisor. This fact can result in information loss, hence, the usage of Sindel as an output language can severely compromises the information processed at the Advisor. Differently, Condel should be used at saving an ontology processed by the Advisor, what implies in irrelevant loss of information. Condel has as objective to capture almost all OWL information necessary for a future information reprocessing.</div> </li>
						  <li><a class="question" href="#">How to write a Condel specification?</a> <div class="answer">Condel's abstract syntax (i.e., its metamodel) is defined in Unified Modeling Language's (UML) class diagrams. Its concrete syntax (i.e., its grammars) is defined with Extended Backus-Naur Form (EBNF) notation. Click on links to visualize them.</div> </li>
						  <li><a class="question" href="#">Why I cannot save/export a Sindel declaration?</a> <div class="answer">The Advisor provides only two ways to save the information processed by it. I.e., differently from the input, where three possible languages are available, the Advisor has only two output languages available: (i) an OWL file or (ii) a Condel specification. The absence of an option for saving a Sindel specification is explained because of the loss of information that would certainly happen - Condel is a more expressive language, making it a more suitable output option.</div> </li>
						  <li><a class="question" href="#">What is the Equipment Studio functionality?</a> <div class="answer">The equipment studio functionality aims to provide to the equipment manufacturer an equipment design tool. With this tool, a user can create equipment that is in accordance with the Sindel's metamodel. <br><br> It is important to note that this functionality creates types of equipment, not instances, i.e., with its aid, a user can create, for example, a transponder (an Optical Transport Network equipment), but it cannot create the transponder with the id X present in the hack Y. The equipment created with the aid of this tool can be later used to create specific equipment instances. For example, once created the generic concept transponder, the user can create many individuals transponder1, transponder2, etc., of transponder type. <br><br> This functionality deals with a different abstraction from that one presented in the Advisor's knowledge base. Instances created here are not mixed with those present in the knowledge base, once this one's corresponds to types, not instances, in comparison with the later.</div> </li>
						  <li><a class="question" href="#">What is the Visualization functionality?</a> <div class="answer">The network visualization has as objective the better network comprehension by the network operator. He or she can use it for visualize the network elements and its known relationships. The visualization displays a graph visualization for the user, where the vertices are the network elements and the edges are their relationship.</div> </li>
						  <li><a class="question" href="#">What is the Provisioning functionality?</a> <div class="answer">The equipment provisioning functionality aims at help the network operator to correctly assert the relations between network equipment. Once the equipment compositions are known (for example, by using the Equipment Studio functionality), this information can be used to verify which are its possible relations with other elements. This functionality has two main parts: equipment binding (physical relation) and equipment connection (logical relation).</div> </li>
					</ol>
				</div>
			</div>
		</div>
		<!--/col-->

	</div>
<<<<<<< .mine
=======
	<!--/row-->
>>>>>>> .r139


<%@include file="../templates/footer.jsp"%>