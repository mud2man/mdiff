package net.jonbell.examples.bytecode.instrumenting;

import java.util.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class ClassDiff{
	LinkedList<InstructionSet> originalInstructionSets;
	LinkedList<InstructionSet> modifiedInstructionSets;
	File originalFile;
	File modifiedFile;
    File outputDir;

	
	private class InstructionSet{
		int lineNum;
		StringBuilder instructions;
		InstructionSet(int l, StringBuilder i){lineNum = l; instructions = i;};
	}
	
	public ClassDiff(File originalFile, File modifiedFile, File outputDir){
		System.out.println(originalFile.getPath());
		System.out.println(modifiedFile.getPath());
		System.out.println(outputDir.getPath());
		this.originalFile = originalFile;
		this.modifiedFile = modifiedFile;
		this.outputDir = outputDir;
		originalInstructionSets = parser(originalFile);
		modifiedInstructionSets = parser(modifiedFile);
	}
	
	public void dumpInstructionSets(LinkedList<InstructionSet> instructionSets){
		for(InstructionSet i: instructionSets){
			System.out.println(i.lineNum);
			System.out.println(i.instructions);
		}
	}
	
	public LinkedList<InstructionSet> parser(File file){
		LinkedList<InstructionSet> instructionSets;
		InstructionSet instSet;
		String numStr;
		
		instructionSets = new LinkedList<InstructionSet>();
		instSet = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	if(line.lastIndexOf("line") != -1){
		    		if(instSet != null){
		    			instructionSets.add(instSet);
		    		}
		    		instSet = new InstructionSet(0, new StringBuilder(""));
		    		instSet.lineNum = Integer.parseInt(line.substring(6));
		    	}
		    	else{
		    		instSet.instructions.append(line);
		    		instSet.instructions.append("\n");
		    	}
		        line = br.readLine();
		    }
		    
		    if(instSet != null){
		    	instructionSets.add(instSet);
		    }
		} 
		catch (IOException e) {
	        e.printStackTrace();
		} 	
		return instructionSets;
	}
	
	private enum Direction{
		FROMUPLEFT,
		FROMLEFT,
	    FROMUP;
	}
	
	private class Element{
		int direction;
		int length;
		Element(){direction = 0; length = 0;}
	}
	
	public void longestCommonSequenceDiff(){
		int rowNum;
		int colNum;
		Element[][] dp;
		int y, x;
		int i, j;
		LinkedList<InstructionSet> lcs;
		
		rowNum = originalInstructionSets.size();
		colNum = modifiedInstructionSets.size();
		dp = new Element[rowNum][colNum];
		lcs = new LinkedList<InstructionSet>();
		
		for(y = 0; y < rowNum; ++y){
			for(x = 0; x < colNum; ++x){
				dp[y][x] = new Element();
			}
		}
		
		/* Calculate the longest common sequence using Dynamic Programming*/
		for(y = 0; y < rowNum; ++y){
			for(x = 0; x < colNum; ++x){
				/* Origin */
				if((y == 0) && (x == 0)){
					if(originalInstructionSets.get(y).instructions.toString().equals(modifiedInstructionSets.get(x).instructions.toString())){
						dp[y][x].direction = 0;
						dp[y][x].length = 1;
					}
					else{
						dp[y][x].direction = 0;
						dp[y][x].length = 0;
					}
				}
				/* Left edge */
				else if(y == 0){
					if(originalInstructionSets.get(y).instructions.toString().equals(modifiedInstructionSets.get(x).instructions.toString())){
						dp[y][x].direction = 0;
						dp[y][x].length = 1;
					}
					else{
						dp[y][x].direction = 1;
						dp[y][x].length = dp[y][x - 1].length;
					}
				}
				/* Right edge */
				else if (x == 0){
					if(originalInstructionSets.get(y).instructions.toString().equals(modifiedInstructionSets.get(x).instructions.toString())){
						dp[y][x].direction = 0;
						dp[y][x].length = 1;
					}
					else{
						dp[y][x].direction = 2;
						dp[y][x].length = dp[y - 1][x].length;
					}
				}
				else{
					if(originalInstructionSets.get(y).instructions.toString().equals(modifiedInstructionSets.get(x).instructions.toString())){
						dp[y][x].direction = 0;
						dp[y][x].length = dp[y - 1][x - 1].length + 1;
					}
					else{
						if(dp[y][x - 1].length > dp[y - 1][x].length){
							dp[y][x].direction = 1;
							dp[y][x].length = dp[y][x - 1].length;
						}
						else{
							dp[y][x].direction = 2;
							dp[y][x].length = dp[y - 1][x].length;
						}
					}					
				}
			}
		}
		
		/* Reconstruct the longest common sequence */
		y = rowNum - 1;
		x = colNum - 1;
		while(y > 0 || x > 0){
			if(dp[y][x].direction == 0){
				lcs.addFirst(originalInstructionSets.get(y));
				x--;
				y--;
			}
			else if (dp[y][x].direction == 1){
				x--;
			}
			else{
				y--;
			}
		}
		if(dp[y][x].length == 1){
			lcs.addFirst(originalInstructionSets.get(0));
		}	

		BufferedWriter bw_old = null;
		BufferedWriter bw_new = null;

		FileWriter fw_old = null;
		FileWriter fw_new = null;
		
		String oldLineFileName;
		String newLineFileName;
		
		oldLineFileName = this.originalFile.getName().substring(0, this.originalFile.getName().length() - 4);
		newLineFileName = this.modifiedFile.getName().substring(0, this.modifiedFile.getName().length() - 4);
        
        /* With the longest commom sequence, we now can differentiate both old and new version */	
		try {
			fw_old = new FileWriter(outputDir.getPath() + "/" + oldLineFileName + "-OLD");
			bw_old = new BufferedWriter(fw_old);

			fw_new = new FileWriter(outputDir.getPath() + "/" + newLineFileName + "-NEW");
			bw_new = new BufferedWriter(fw_new);



			/* Print the deleted line from original instruction sets */
			System.out.println("####################");
			System.out.println("# Previous version #");
			System.out.println("####################");
			System.out.println("this.originalFile:" + this.originalFile);
			for(i = 0, j = 0; i < lcs.size(); ++i, ++j){
				while(!lcs.get(i).instructions.toString().equals(originalInstructionSets.get(j).instructions.toString())){
					System.out.println("- line: " + originalInstructionSets.get(j).lineNum);
					System.out.println(originalInstructionSets.get(j).instructions.toString());
					bw_old.write(originalInstructionSets.get(j).lineNum + "\n");
					++j;
				}
			}
			while(j < originalInstructionSets.size()){
				System.out.println("- line: " + originalInstructionSets.get(j).lineNum);
				System.out.println(originalInstructionSets.get(j).instructions.toString());
				bw_old.write(originalInstructionSets.get(j).lineNum + "\n");
				++j;
			}
			
			/* Print the added line from original instruction sets */
			System.out.println("###################");
			System.out.println("# Updated version #");
			System.out.println("###################");
			System.out.println("this.modifiedFile:" + this.modifiedFile);
			for(i = 0, j = 0; i < lcs.size(); ++i, ++j){
				while(!lcs.get(i).instructions.toString().equals(modifiedInstructionSets.get(j).instructions.toString())){
					System.out.println("+ line: " + modifiedInstructionSets.get(j).lineNum);
					System.out.println(modifiedInstructionSets.get(j).instructions.toString());
					bw_new.write(modifiedInstructionSets.get(j).lineNum + "\n");
					++j;
				}
			}
			while(j < modifiedInstructionSets.size()){
				System.out.println("+ line: " + modifiedInstructionSets.get(j).lineNum);
				System.out.println(modifiedInstructionSets.get(j).instructions.toString());
				bw_new.write(modifiedInstructionSets.get(j).lineNum + "\n");
				++j;
			}


		
			bw_new.close();
			bw_old.close();

			fw_new.close();
			fw_old.close();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
