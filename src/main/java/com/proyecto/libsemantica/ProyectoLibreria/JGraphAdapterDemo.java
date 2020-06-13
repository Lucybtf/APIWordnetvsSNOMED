package com.proyecto.libsemantica.ProyectoLibreria;



import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.*;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.*;

import org.jgrapht.traverse.*;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.rmi.server.ExportException;
import java.util.*;

import javax.imageio.ImageIO;


public final class JGraphAdapterDemo
{

	public static void testUndirected() throws IOException
    {
		/*File imgFile = new File("./dist/graph.png");
	    imgFile.createNewFile();
	    if(imgFile.exists())
	    	System.out.print("EXISTE");
	    else System.out.print("NO EXISTE");*/
	    DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	 
	    String x1 = "a";
	    String x2 = "b";
	    String x3 = "c";
	 
	    g.addVertex(x1);
	    g.addVertex(x2);
	    g.addVertex(x3);
	 
	    g.addEdge(x1, x2);
	    g.addEdge(x2, x3);
	    g.addEdge(x3, x1);
		 
	    JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
	    mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
	    layout.execute(graphAdapter.getDefaultParent());
	     
	    BufferedImage image =  mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
	    File imgFile1 = new File("./rdf/imagen.png");
	    ImageIO.write(image, "PNG", imgFile1);
	 
	   // assertTrue(imgFile.exists());
	}
	
	public static void main(String args[]) throws IOException {
		testUndirected();
	  // givenAdaptedGraph_whenWriteBufferedImage_thenFileShouldExist();
	}
}   