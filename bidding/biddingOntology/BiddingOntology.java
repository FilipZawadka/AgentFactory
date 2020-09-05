package biddingOntology;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.*;

import javax.swing.text.Position;

public class BiddingOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Bidding-ontology";

    public static final String GET_HELP = "GetHelp";

    public static final String PROPOSAL = "Proposal";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String NUMBER = "trNumber";

    public static final String GOM = "Gom";
    public static final String GOM_ID = "gomId";

    public static final String POSITION = "Position";
    public static final String X_POS = "x";
    public static final String Y_POS = "y";

    public static final String DESTINATION_GOM = "destGom";
    public static final String SOURCE_GOM = "srcGom";
    public static final String TOKENS = "tokens";


    public static final String SEND_RESULT = "SendResult";
    public static final String RESULT = "result";

    public static final String DESTINATION = "Destination";

    public static final String MESSAGE = "message";


    private static Ontology theInstance = new BiddingOntology();

    public static Ontology getInstance(){
        return theInstance;
    }

    private BiddingOntology(){
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try{
            add(new ConceptSchema(POSITION), PositionInfo.class);
            add(new ConceptSchema(PROPOSAL), Proposal.class);
            add(new ConceptSchema(GOM), GomInfo.class);
            add(new AgentActionSchema(GET_HELP), GetHelp.class);
            add(new AgentActionSchema(SEND_RESULT), SendResult.class);
            add(new PredicateSchema(DESTINATION),Destination.class);

            ConceptSchema cs = (ConceptSchema) getSchema(POSITION);
            cs.add(X_POS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(Y_POS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

            cs = (ConceptSchema) getSchema(GOM);
            cs.add(GOM_ID, (TermSchema)getSchema(BasicOntology.AID));
            cs.add(POSITION, (TermSchema) getSchema(POSITION));

            cs = (ConceptSchema) getSchema(PROPOSAL);
            cs.add(PROPOSAL_ID, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(NUMBER, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(DESTINATION_GOM, (TermSchema)getSchema(GOM));
            cs.add(SOURCE_GOM, (TermSchema)getSchema(GOM));
            cs.add(TOKENS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

            AgentActionSchema as = (AgentActionSchema) getSchema(GET_HELP);
            as.add(PROPOSAL, (ConceptSchema) getSchema(PROPOSAL));

            as = (AgentActionSchema) getSchema(SEND_RESULT);
            as.add(RESULT, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));

            PredicateSchema p = (PredicateSchema) getSchema(DESTINATION);
            p.add(POSITION, (TermSchema) getSchema(POSITION));
            p.add(MESSAGE,(TermSchema)getSchema(BasicOntology.ACLMSG), ObjectSchema.OPTIONAL);

        }
        catch(OntologyException oe){
            oe.printStackTrace();
        }
    }
}
