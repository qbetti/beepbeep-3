package ca.uqac.lif.cep.io;


import ca.uqac.lif.cep.tmf.Source;

public abstract class SourceInfoProvider extends Source {

    public SourceInfoProvider(int out_arity) {
        super(out_arity);
    }

    public abstract String getSourceName();


}
