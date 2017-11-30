package ca.uqac.lif.cep.tmf;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;

public abstract class Branch extends GroupProcessor {


    public Branch(int in_arity, int out_arity) throws Connector.ConnectorException {
        super(in_arity, out_arity);
    }

    public abstract void build() throws Connector.ConnectorException;

}
