package com.pw.biddingOntology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.*;

public class BiddingOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Bidding-ontology";

    public static final String GET_HELP = "GetHelp";

    public static final String CALL_FOR_PROPOSAL = "callForProposal";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String NUMBER = "trNumber";

    public static final String GOM = "Gom";
    public static final String GOM_ID = "gomId";

    public static final String POSITION = "Position";
    public static final String X_POS = "x";
    public static final String Y_POS = "y";

    public static final String MATERIAL = "Material";
    public static final String MATERIAL_NAME = "name";
//    public static final String MATERIAL_AMOUNT = "amount";
    public static final String MATERIAL_WEIGHT = "weight";

    public static final String DESTINATION = "destGom";
    public static final String SOURCE = "srcGom";
    public static final String TOKENS = "tokens";

    public static final String SEND_RESULT = "SendResult";
    public static final String RESULT = "result";

    public static final String GOM_JOB_REQUEST = "gomJobRequest";
    public static final String GOM_JOB_REQUEST_FROM = "from";
    public static final String GOM_JOB_REQUEST_TO = "to";
    public static final String GOM_JOB_REQUEST_MATERIAL = "materialInfo";

    public static final String JOB_INITIAL_POSITION = "jobInitialPosition";
    public static final String JOB_SENDER = "sender";
    public static final String JOB_CONVERSATION = "conversation";

    private static Ontology theInstance = new BiddingOntology();

    public static Ontology getInstance() {
        return theInstance;
    }

    private BiddingOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {
            add(new ConceptSchema(POSITION), PositionInfo.class);
            add(new ConceptSchema(CALL_FOR_PROPOSAL), CallForProposal.class);
            add(new ConceptSchema(GOM), GomInfo.class);
            add(new ConceptSchema(MATERIAL), MaterialInfo.class);
            add(new AgentActionSchema(GET_HELP), GetHelp.class);
            add(new AgentActionSchema(SEND_RESULT), SendResult.class);
            add(new AgentActionSchema(GOM_JOB_REQUEST), GomJobRequest.class);
            add(new PredicateSchema(JOB_INITIAL_POSITION), JobInitialPosition.class);

            ConceptSchema cs = (ConceptSchema) getSchema(POSITION);
            cs.add(X_POS, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            cs.add(Y_POS, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            cs = (ConceptSchema) getSchema(GOM);
            cs.add(GOM_ID, (TermSchema) getSchema(BasicOntology.AID));
            cs.add(POSITION, (TermSchema) getSchema(POSITION));

            cs = (ConceptSchema) getSchema(CALL_FOR_PROPOSAL);
            cs.add(PROPOSAL_ID, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            cs.add(NUMBER, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            cs.add(DESTINATION, (TermSchema) getSchema(GOM));
            cs.add(SOURCE, (TermSchema) getSchema(GOM));
            cs.add(TOKENS, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            cs = (ConceptSchema) getSchema(MATERIAL);
            cs.add(MATERIAL_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
//            cs.add(MATERIAL_AMOUNT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            cs.add(MATERIAL_WEIGHT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

            AgentActionSchema as = (AgentActionSchema) getSchema(GET_HELP);
            as.add(CALL_FOR_PROPOSAL, (ConceptSchema) getSchema(CALL_FOR_PROPOSAL));

            as = (AgentActionSchema) getSchema(SEND_RESULT);
            as.add(RESULT, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));

            as = (AgentActionSchema) getSchema(GOM_JOB_REQUEST);
            as.add(GOM_JOB_REQUEST_FROM, (TermSchema) getSchema(GOM));
            as.add(GOM_JOB_REQUEST_TO, (TermSchema) getSchema(GOM));
            as.add(GOM_JOB_REQUEST_MATERIAL, (TermSchema) getSchema(MATERIAL));

            PredicateSchema p = (PredicateSchema) getSchema(JOB_INITIAL_POSITION);
            p.add(POSITION, getSchema(POSITION));
            p.add(JOB_SENDER, getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
            p.add(JOB_CONVERSATION, getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);


        } catch (OntologyException oe) {
            oe.printStackTrace();
        }
    }
}
