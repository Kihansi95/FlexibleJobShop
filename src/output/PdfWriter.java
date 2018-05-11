package output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jobshopflexible.Configuration;

public class PdfWriter {
	
	// config const
	private static final String[] 
			DIRECTORY = {"output.directory", "tmp"},
			FILENAME = {"output.filename", "is"};
	private static final String VERBOSE = "output.verbose";
	
	// config affectation
	private String directory;
	private String filename;
	private boolean verbose;
	
	private StringBuilder template;
	
	private static final String endL = System.getProperty("line.separator"); // == \n
	private static final String nodeToken = "@nodes@";
	private static final String pathToken = "@paths@";
	
	public PdfWriter(Configuration conf) {
		this.directory = conf.getParam(DIRECTORY[0], DIRECTORY[1]);
		this.filename = conf.getParam(FILENAME[0], FILENAME[1]);
		String v =  conf.getParam(VERBOSE);
		verbose = v != null && !v.toUpperCase().equalsIgnoreCase("FALSE");
		
		template = new StringBuilder();
		template
			//.append("\\documentclass[border=0.50001bp,convert={convertexe={imgconvert}, size=800x800,outext=.jpg}]{standalone}").append(endL)
			.append("\\documentclass{article}").append(endL)
			.append("\\usepackage{pgf}").append(endL)
			.append("\\usepackage{tikz}").append(endL)
			.append("\\usetikzlibrary{arrows,automata,positioning}").append(endL).append(endL)
			.append("\\begin{document}").append(endL)
			.append("\\begin{center}").append(endL)
			.append("\\begin{tikzpicture}[shorten >=1pt,node distance=2cm,on grid,auto] ").append(endL).append(endL)

			/*
			.append("\\node[state,initial] (q_0)   {$q_0$};").append(endL)
			.append("\\node[state] (q_1) [above right=of q_0] {$q_1$};").append(endL)
			.append("\\node[state] (q_2) [below right=of q_0] {$q_2$};").append(endL)
			.append("\\node[state,accepting](q_3) [below right=of q_1] {$q_3$};").append(endL)
			*/
			.append(nodeToken).append(endL).append(endL)
			
			/*
			
			.append("(q_0) edge  node {0} (q_1)").append(endL)
			.append("\t edge  node [swap] {1} (q_2)").append(endL)
			.append("(q_1) edge  node  {1} (q_3)").append(endL)
			.append("\t edge [loop above] node {0} ()").append(endL)
			.append("(q_2) edge  node [swap] {0} (q_3) ").append(endL)
			.append("\t edge [loop below] node {1} ();").append(endL)
			 */
			.append("\t\\path[->]").append(endL)
			.append(pathToken).append(endL).append(endL)
			
			.append("\\end{tikzpicture}").append(endL)
			.append("\\end{center}").append(endL)
			.append("\\end{document}").append(endL);
	}
	
	public void addNode(String node) {
		this.addNode(node, false, false);
	}
	
	public void addNode(String node, boolean start, boolean end) {
		
		// translate into latex's node
		StringBuilder nodeString = new StringBuilder("\t\\node[state");
		if(start) nodeString.append(",initial");
		if(end) nodeString.append(",accepting");
		nodeString.append("] (" + node + ") {$" + node + "$};").append(endL);
		
		// fill into the template
		int place = template.indexOf(nodeToken);
		template.insert(place, nodeString.toString());
	}
	
	public void addPath(String from, String to, int weight) {
		
		/*
		
		.append("(q_0) edge  node {0} (q_1)").append(endL)
		.append("\t edge  node [swap] {1} (q_2)").append(endL)
		.append("(q_1) edge  node  {1} (q_3)").append(endL)
		.append("\t edge [loop above] node {0} ()").append(endL)
		.append("(q_2) edge  node [swap] {0} (q_3) ").append(endL)
		.append("\t edge [loop below] node {1} ();").append(endL)
		 */
		String pathString = "\t(" + from + ") edge node {" + weight + "} ("+to + ")" + endL;
		
		int place = template.indexOf(pathToken);
		template.insert(place, pathString);
	}
	
	public void write() {
		
		// clear paramter in template
		String content = template.toString()
				.replaceFirst(nodeToken, "")
				.replaceFirst(pathToken, ";"); // can use replace all but use this for performance
		
        try {
        	
        	// Create the .tex file
        	verbose("Create the " + filename + ".tex");
        	FileWriter writer = new FileWriter(this.directory + "\\" + this.filename + ".tex", false);
            writer.write(content, 0, content.length());
            writer.close();
            
            // Clear the last result if exist
            File lastPdf = new File(directory+"\\"+ filename +".pdf");
            if(!lastPdf.delete()) 
            	System.err.println("Unable to delete "+lastPdf.getPath());
            
            
            // Execute LaTeX from command line  to generate picture
            verbose("Generate the " + filename + ".pdf");
            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\x64\\pdflatex", "-shell-escape", this.filename + ".tex");
            pb.directory(new File(this.directory));
            
            Process p = pb.start();
            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), verbose);
            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), verbose);
            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();
            p.waitFor();
            
            // Display picture
            /*
            JFrame maFrame = new JFrame();
            maFrame.setResizable(false);
            maFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            maFrame.setSize(800, 800);
            maFrame.getContentPane().setLayout(new FlowLayout());
            maFrame.getContentPane().add(new JLabel(new ImageIcon(TEMP_DIRECTORY + "\\" + TEMP_TEX_FILE_NAME + ".png")));
            maFrame.pack();
            maFrame.setVisible(true);
            */
            
            // Delete files
            /*
            for (File file : (new File(TEMP_DIRECTORY).listFiles())) {
                if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
                    file.delete();
                }
            }
            */
            
            File log = new File(directory+"\\"+ filename +".log");
            if(!log.delete()) 
            	System.err.println("Unable to delete "+log.getPath());
            
            /*
            if(!(new File(directory+"\\"+ filename +".log")).delete()) 
            	System.err.println("Unable to delete "+filename+".log");*/
            
            if(!(new File(directory+"\\"+ filename +".aux")).delete()) 
            	System.err.println("Unable to delete "+filename+".aux");
                   
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void verbose(String msg) {
		if(verbose) System.out.println(msg);
	}
	
}
