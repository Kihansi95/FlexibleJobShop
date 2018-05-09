package research;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import data.*;

public class InitialSolution {
	
	// input
	private FlexibleJobShop context; //TODO need it??
	private List<Machine> machines;
	private List<Operation> availableOperations;
	private AssignmentComparator comparator;
	
	// output
	private List<Label> assignments;
	
	//comparator that helps sort  
	private class AssignmentComparator implements Comparator<Label> {

		@Override
		public int compare(Label label1, Label label2) {
			return label1.getProcessingTime() - label2.getProcessingTime();
		}
	}
	
	public InitialSolution(FlexibleJobShop context) {
		
		this.machines = new ArrayList<Machine>();
		this.availableOperations = new LinkedList<Operation>();
		this.context = context; // TODO to copy context or to reference
		this.comparator = new AssignmentComparator();
		this.assignments = new LinkedList<Label>();
		
		// init machine from context
		int nbMachines = context.getNbMachine();		
		for(int i = 0; i < nbMachines; i++) {
			machines.add(new Machine(i + 1)); 
		}
		
		// init available activities
		for(Job job: context.getJobs()) {
			Operation waitingOperation = job.getOperation();
			availableOperations.add(waitingOperation);	// get the first activity from each job
		}
	}
	
	public void start() {
		//TODO using thread?
		
		int time = 0;
		List<Label> candidateList = new LinkedList<Label>();
		List<Label> chosenCandidate = new LinkedList<Label>();
		
		//for(int i = 0; i < 2; i++)	{
		while(!availableOperations.isEmpty()) {
			
			// make candidate list
			candidateList.clear();
			for(Operation operation: this.availableOperations) {
				Map<Integer, Integer> tuples = operation.getTuples();
				for(Integer idMachine: tuples.keySet()) {
					
					Machine machine = machines.get(idMachine - 1);
					if(machine.isReady()) {
						candidateList.add(new Label(operation, machine, null));
					}
					
				}
			}
			
			// choose the best candidate(s) but be careful with duplicata assignments
			Collections.sort(candidateList, comparator);
			while(!candidateList.isEmpty()) {
				
				// choose the best
				Label best = candidateList.remove(0);
				best.setMachineState(false);
				best.updateFinishTime(time);
				chosenCandidate.add(best);
				
				// remove duplicata machine || operation
				while(candidateList.remove(best));	//not the best but the corresponding
				
				// remove the corresponding from waiting available list
				Operation chosenOp = best.getOperation();
								
				availableOperations.remove(chosenOp);
				Operation next = chosenOp.getNext();
				if(next != null)
					availableOperations.add(next);
			}
			
			// update candidate's father
			for(Label candidate: chosenCandidate) {
				
				Label father = candidate.getFather();
				
				// if label doesn't have father yet, its the first time we process algo
				// so no need to update the father
				if(father != null) {
					
					if(father.getFinishTime() < time) {
						candidate.setFatherByMachine();
					}
				}
			}

			// advance time cost and update machine state (ready)
			int min_time = chosenCandidate.get(0).getProcessingTime();
			time += min_time;
			for(Label label : chosenCandidate)	{	// update machine state
				label.setMachineState(time <= label.getFinishTime());
			}
			
			// save chosen candidate into output solution 
			this.assignments.addAll(chosenCandidate);
			chosenCandidate.clear(); // TODO check if all assigned labels are null
			
			System.out.println("availableOperations = "+availableOperations);
		}
		
		
	}
	
	public List<Label> getSolution()	{
		return assignments;
	}
	
	public void visualizeSolution() {
		String TEMP_DIRECTORY = "tmp";
        String TEMP_TEX_FILE_NAME = "is"; // for is.tex
        
     // 1. Prepare the .tex file
        String newLineWithSeparation = System.getProperty("line.separator")+System.getProperty("line.separator");
        String math = "";
        math += "\\documentclass{article}" + newLineWithSeparation;
        math += "\\usepackage{amsfonts}" + newLineWithSeparation;
        math += "\\usepackage{amsmath}" + newLineWithSeparation;
        math += "\\begin{document}" + newLineWithSeparation;
        math += "$\\begin{array}{l}" + newLineWithSeparation;
        math += "\\forall\\varepsilon\\in\\mathbb{R}_+^*\\ \\exists\\eta>0\\ |x-x_0|\\leq\\eta\\Longrightarrow|f(x)-f(x_0)|\\leq\\varepsilon\\\\" + newLineWithSeparation;
        math += "\\det\\begin{bmatrix}a_{11}&a_{12}&\\cdots&a_{1n}\\\\a_{21}&\\ddots&&\\vdots\\\\\\vdots&&\\ddots&\\vdots\\\\a_{n1}&\\cdots&\\cdots&a_{nn}\\end{bmatrix}\\overset{\\mathrm{def}}{=}\\sum_{\\sigma\\in\\mathfrak{S}_n}\\varepsilon(\\sigma)\\prod_{k=1}^n a_{k\\sigma(k)}\\\\" + newLineWithSeparation;
        math += "{\\sideset{_\\alpha^\\beta}{_\\gamma^\\delta}{\\mathop{\\begin{pmatrix}a&b\\\\c&d\\end{pmatrix}}}}\\\\" + newLineWithSeparation;
        math += "\\int_0^\\infty{x^{2n} e^{-a x^2}\\,dx} = \\frac{2n-1}{2a} \\int_0^\\infty{x^{2(n-1)} e^{-a x^2}\\,dx} = \\frac{(2n-1)!!}{2^{n+1}} \\sqrt{\\frac{\\pi}{a^{2n+1}}}\\\\" + newLineWithSeparation;
        math += "\\int_a^b{f(x)\\,dx} = (b - a) \\sum\\limits_{n = 1}^\\infty  {\\sum\\limits_{m = 1}^{2^n  - 1} {\\left( { - 1} \\right)^{m + 1} } } 2^{ - n} f(a + m\\left( {b - a} \\right)2^{-n} )\\\\" + newLineWithSeparation;
        math += "\\int_{-\\pi}^{\\pi} \\sin(\\alpha x) \\sin^n(\\beta x) dx = \\textstyle{\\left \\{ \\begin{array}{cc} (-1)^{(n+1)/2} (-1)^m \\frac{2 \\pi}{2^n} \\binom{n}{m} & n \\mbox{ odd},\\ \\alpha = \\beta (2m-n) \\\\ 0 & \\mbox{otherwise} \\\\ \\end{array} \\right .}\\\\" + newLineWithSeparation;
        math += "L = \\int_a^b \\sqrt{ \\left|\\sum_{i,j=1}^ng_{ij}(\\gamma(t))\\left(\\frac{d}{dt}x^i\\circ\\gamma(t)\\right)\\left(\\frac{d}{dt}x^j\\circ\\gamma(t)\\right)\\right|}\\,dt\\\\" + newLineWithSeparation;
        math += "\\begin{array}{rl} s &= \\int_a^b\\left\\|\\frac{d}{dt}\\vec{r}\\,(u(t),v(t))\\right\\|\\,dt \\\\ &= \\int_a^b \\sqrt{u'(t)^2\\,\\vec{r}_u\\cdot\\vec{r}_u + 2u'(t)v'(t)\\, \\vec{r}_u\\cdot\\vec{r}_v+ v'(t)^2\\,\\vec{r}_v\\cdot\\vec{r}_v}\\,\\,\\, dt. \\end{array}\\\\" + newLineWithSeparation;
        math += "\\end{array}$" + newLineWithSeparation;
        math += "\\end{document}";
        
        // 2. Create the .tex file
        FileWriter writer = null;
        try {
            writer = new FileWriter(TEMP_DIRECTORY + "\\" + TEMP_TEX_FILE_NAME + ".tex", false);
            writer.write(math, 0, math.length());
            writer.close();
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // 3. Execute LaTeX from command line  to generate picture
        ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\x64\\pdflatex", "-shell-escape", TEMP_TEX_FILE_NAME + ".tex");
        pb.directory(new File(TEMP_DIRECTORY));
        try {
            Process p = pb.start();
            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), false);
            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();
            p.waitFor();
            
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
        /*
        pb.command("C:\\Program Files\\ImageMagick-7.0.7-Q16\\convert.exe", TEMP_DIRECTORY + "\\" + TEMP_TEX_FILE_NAME + ".pdf", TEMP_TEX_FILE_NAME + ".png");
        try {
            Process p = pb.start();
            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();
            p.waitFor();
            
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }*/
        
        // 4. Display picture
        JFrame maFrame = new JFrame();
        maFrame.setResizable(false);
        maFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maFrame.setSize(800, 800);
        maFrame.getContentPane().setLayout(new FlowLayout());
        maFrame.getContentPane().add(new JLabel(new ImageIcon(TEMP_DIRECTORY + "\\" + TEMP_TEX_FILE_NAME + ".png")));
        maFrame.pack();
        maFrame.setVisible(true);
        
	}
	
	private class StreamPrinter implements Runnable {

	    // Source: http://labs.excilys.com/2012/06/26/runtime-exec-pour-les-nuls-et-processbuilder/
	    private final InputStream inputStream;

	    private boolean print;

	    StreamPrinter(InputStream inputStream, boolean print) {
	        this.inputStream = inputStream;
	        this.print = print;
	    }

	    private BufferedReader getBufferedReader(InputStream is) {
	        return new BufferedReader(new InputStreamReader(is));
	    }

	    @Override
	    public void run() {
	        BufferedReader br = getBufferedReader(inputStream);
	        String ligne = "";
	        try {
	            while ((ligne = br.readLine()) != null) {
	                if (print) {
	                    System.out.println(ligne);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
}

