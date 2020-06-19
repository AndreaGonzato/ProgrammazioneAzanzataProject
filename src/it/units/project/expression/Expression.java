package it.units.project.expression;

import java.net.ProtocolException;
import java.util.List;
import java.util.Set;

public class Expression {
  public final String definition;
  public Set<Variable> variables;
  public Set<List<Double>> tuples;

  public Node root;

  public Expression(String definition, Set<Variable> variables, Set<List<Double>> tuples) throws ProtocolException {
    this.definition = definition;
    this.variables = variables;
    this.tuples = tuples;
    Parser parser = new Parser(definition);

    try {
      root = parser.parse();
    }catch (IllegalArgumentException e){
      throw new ProtocolException("This expression: '"+definition+"' does not respect the protocol");
    }


  }
}
