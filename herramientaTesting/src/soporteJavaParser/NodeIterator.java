
package soporteJavaParser;

import com.github.javaparser.ast.Node;

public class NodeIterator {
	public interface NodeHandler {
        boolean handle(Node node);
    }
 
    private NodeHandler nodeHandler;
 
    public NodeIterator(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }
 
    /**
     * Explora recursivamente todo el arbol AST.
     * No distingue entre sentencias.
     * @param node
    */
    
    public void explore(Node node) {
        if (nodeHandler.handle(node)) {
            for (Node child : node.getChildNodes()) {
                explore(child);
            }
        }
    }
}
