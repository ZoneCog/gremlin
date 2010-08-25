package com.tinkerpop.gremlin.compiler.functions.sail;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.sail.SailGraph;
import com.tinkerpop.gremlin.BaseTest;
import com.tinkerpop.gremlin.compiler.Tokens;
import com.tinkerpop.gremlin.compiler.context.GremlinScriptContext;
import com.tinkerpop.gremlin.compiler.functions.Function;
import com.tinkerpop.gremlin.compiler.types.Atom;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RemoveNamespaceFunctionTest extends BaseTest {

    public void testRemoveNamespace() {
        SailGraph graph = new SailGraph(new MemoryStore());
        GremlinScriptContext context = new GremlinScriptContext();
        context.getVariableLibrary().putAtom(Tokens.GRAPH_VARIABLE, new Atom<Graph>(graph));
        
        Function<Boolean> function = new RemoveNamespaceFunction();
        assertNotNull(graph.getNamespaces().get("rdf"));
        this.stopWatch();
        Atom<Boolean> atom = function.compute(createUnaryArgs(graph, "rdf"), context);
        printPerformance(function.getFunctionName() + " function", 1, "namespace removed", this.stopWatch());
        assertTrue(atom.getValue());
        assertNull(graph.getNamespaces().get("rdf"));

        assertNotNull(graph.getNamespaces().get("rdfs"));
        this.stopWatch();
        atom = function.compute(createUnaryArgs("rdfs"), context);
        printPerformance(function.getFunctionName() + " function", 1, "namespace removed", this.stopWatch());
        assertTrue(atom.getValue());
        assertNull(graph.getNamespaces().get("rdfs"));

        graph.shutdown();
    }
}
