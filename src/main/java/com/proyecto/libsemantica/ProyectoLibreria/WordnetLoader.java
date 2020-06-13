package com.proyecto.libsemantica.ProyectoLibreria;

/*import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.oupls.sail.GraphSail;


import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.SailException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


public class WordnetLoader {
    private static final String SOURCE = "./rdf";
    private static final String DEST = "/Users/josh/data/shortterm/wordnet-loading/neo4j";
    private static final long BUFFER_SIZE = 1000;

    public static void main(final String[] args) {
        try {
            new WordnetLoader().load();
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private void load() throws Exception {
        Neo4jGraph g = new Neo4jGraph("./dict");

        Sail sail = new GraphSail(g);
        sail.initialize();
        try {
            SailConnection c = sail.getConnection();
            try {
                long startTime = System.currentTimeMillis();

                File dir = new File(SOURCE);
                for (File f : dir.listFiles()) {
                    long before = System.currentTimeMillis();
                    System.out.println("loading file: " + f);
                    String n = f.getName();
                    InputStream is;
                    if (n.endsWith(".ttl.gz")) {
                        is = new GZIPInputStream(new FileInputStream(f));
                    } else if (n.endsWith(".ttl")) {
                        is = new FileInputStream(f);
                    } else {
                        continue;
                    }

                    RDFParser p = Rio.createParser(RDFFormat.TURTLE);
                    p.setStopAtFirstError(false);
                    p.setRDFHandler(new SailConnectionAdder(c));

                    try {
                        p.parse(is, "http://example.org/wordnet/");
                    } catch (Throwable t) {
                        // Attempt to recover.
                        t.printStackTrace(System.err);
                    } finally {
                        is.close();
                    }

                    long after = System.currentTimeMillis();
                    System.out.println("\tfinished in " + (after - before) + "ms");
                }

                long endTime = System.currentTimeMillis();
                System.out.println("resulting triple store has " + c.size() + " statements");
                System.out.println("total load time: " + (endTime - startTime) + "ms");
            } finally {
                c.close();
            }
        } finally {
            sail.shutDown();
        }
    }

    private class SailConnectionAdder implements RDFHandler {
        private final SailConnection c;
        private long count = 0;

        private SailConnectionAdder(SailConnection c) {
            this.c = c;
        }

        public void startRDF() throws RDFHandlerException {
        }

        public void endRDF() throws RDFHandlerException {
            try {
                c.commit();
            } catch (SailException e) {
                throw new RDFHandlerException(e);
            }
        }

        public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
            try {
                c.setNamespace(prefix, uri);
            } catch (SailException e) {
                throw new RDFHandlerException(e);
            }

            incrementCount();
        }

        public void handleStatement(Statement statement) throws RDFHandlerException {
            try {
                c.addStatement(statement.getSubject(), statement.getPredicate(), statement.getObject(), statement.getContext());
            } catch (SailException e) {
                throw new RDFHandlerException(e);
            }

            incrementCount();
        }

        public void handleComment(String s) throws RDFHandlerException {
        }

        private void incrementCount() throws RDFHandlerException {
            count++;
            if (0 == count % BUFFER_SIZE) {
                try {
                    c.commit();
                } catch (SailException e) {
                    throw new RDFHandlerException(e);
                }
            }
        }
    }
}*/
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.graphdb.GraphDatabaseService;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;

import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;

import static org.neo4j.driver.Values.parameters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.neo4j.dbms.api.DatabaseManagementService;

import org.graphstream.graph.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class WordnetLoader {
	
	public static int num_nodos=20;
	public static DefaultDirectedGraph<String, DefaultEdge> printHijos(DefaultDirectedGraph<String, DefaultEdge> g, String node, WordnetLibrary wl, ArrayList<String> hijos){
	   
	   g.addVertex(node);
	  // System.out.print(" PINTANDO NODO:"+node+"->"+ hijos+"\n\n");
	   for(int i=0;i<hijos.size();i++){
	    	 g.addVertex(hijos.get(i));
	    	 g.addEdge(node, hijos.get(i));
	    }
		return g;
	}
	
	public static void BuiltGraph(DefaultDirectedGraph<String, DefaultEdge> g, int contador, ArrayList<String> nodos, WordnetLibrary wl){
		
		ArrayList<String> hijosnivel=new ArrayList<String> ();
		ArrayList<String> hijosnivelaux=new ArrayList<String> ();
		System.out.print("\n\n------------ENTRA DE NUEVO FUNCIONE CONTADOR->"+contador+"LISTA NIVEL"+nodos+"\n");
	//	ArrayList<String> hijos= printHijos(g, node, wl);
		//BuiltGraph(g, 0, nodes,wl);
		if(contador<1){
		
		ArrayList<String> nodoshijos = wl.getHyponyms(POS.NOUN,nodos.get(contador));
		//System.out.print(nodoshijos);
		hijosnivel = RecorrerNodos(0,nodoshijos,wl,g, hijosnivel);
		
		System.out.print("\n\n-------HIJOS NIVEL"+hijosnivel);
		
		//String nodehijo =hijosnivel.get(pos);
		//System.out.print("RECORRIDO"+hijosnivel);
	//	hijosnivelaux.remove(hijosnivel.get(contador));
		//contador++;
		
		BuiltGraph(g, contador+1, hijosnivel, wl);
		
		
		}
	}
	
	public static ArrayList<String> RecorrerNodos(int pos, ArrayList<String> hijos, WordnetLibrary wl,DefaultDirectedGraph<String, DefaultEdge> g, ArrayList<String> hijosnodos){
		ArrayList<String> hijosnivelaux=new ArrayList<String> ();
		
		if (pos < hijos.size()-1) { 
			 g =printHijos(g, hijos.get(pos),wl, hijos);
           // System.out.println("\n\nElemento en posición "+pos + " es "+ hijos.get(pos));
            System.out.print("\n\nElemento en posición ----"+pos+"---NODO--"+hijos.get(pos)+"HIJOS"+wl.getHyponyms(POS.NOUN, hijos.get(pos)));
            ArrayList<String> nhijos=wl.getHyponyms(POS.NOUN, hijos.get(pos));
           
            hijosnodos.addAll(nhijos);
            RecorrerNodos(pos+1,hijos, wl,g, hijosnodos);
        }
		
        return hijosnodos;
    }
	
	
	public static void testUndirected() throws IOException
    {
	    DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	    WordnetLibrary wl = new WordnetLibrary();
	    
	    //1º NODO
	    Synset s = wl.getSynset("entity", POS.NOUN);
	    
	    String node =s.getWord(0).getLemma();
	 //   System.out.print(node); 
	  //  g.addVertex(node);
	    ArrayList<String> nodes = new ArrayList<String> ();
	    nodes.add("entity");
	    System.out.print(nodes);
	    BuiltGraph(g, 0, nodes,wl);
	  
	    JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
	
	    graphAdapter.setAutoSizeCells(true);
	    graphAdapter.setCellsResizable(true);
	    graphAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
	    graphAdapter.setCellsEditable(false);
	    graphAdapter.setCellsMovable(false);
	    graphAdapter.setEdgeLabelsMovable(false);
	    graphAdapter.setCellsDeletable(false);
	    graphAdapter.setCellsDisconnectable(false);
	    graphAdapter.setCellsResizable(false);
	    graphAdapter.setCellsBendable(false);

	    mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
	   // layout.moveCell(arg0, arg1, arg2);
	    layout.execute(graphAdapter.getDefaultParent());
	    
	    JFrame frame = new JFrame("NewGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.add(new mxGraphComponent(graphAdapter));

		frame.setPreferredSize(new Dimension(900, 600));
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	   /* BufferedImage image =  mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
	    File imgFile1 = new File("./rdf/imagen.png");
	    ImageIO.write(image, "PNG", imgFile1);*/
	 
	}
	
	public static void main(String args[]) throws IOException {
		testUndirected();
	 
	}
}
