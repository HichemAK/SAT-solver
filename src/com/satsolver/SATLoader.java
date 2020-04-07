package com.satsolver;

import com.sat.Clause;
import com.sat.SAT;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SATLoader {
    public static SAT[] loads(String directorypath){
        SAT[] sats;
        File folder = new File(directorypath);
        File[] fileList = folder.listFiles();
        sats = new SAT[fileList.length];
        for (int i=0;i<fileList.length;i++) {
            sats[i] = load(fileList[i].getAbsolutePath());
        }
        return sats;
    }

    public static SAT load(String filepath){
        SAT sat = null;
        int numVar = 0;
        ArrayList<Clause> clauses = new ArrayList<>();
        HashSet<Character> correctClauseChars = new HashSet<>(Arrays.asList('-','1','2','3','4','5','6','7','8','9','0'));
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim().replaceAll(" +", " ");
                if(line.length() != 0 && line.charAt(0) == 'p'){
                    String[] comp = line.split(" ");
                    numVar = Integer.parseInt(comp[2]);
                }
                else if(line.length() != 0 && correctClauseChars.contains(line.charAt(0))){
                    String[] comp = line.split(" ");
                    if(comp.length <= 1){
                        line = reader.readLine();
                        continue;
                    }
                    boolean[] notOp = new boolean[comp.length-1];
                    int[] variables = new int[comp.length-1];
                    for(int i=0;i<comp.length-1;i++){
                        notOp[i] = true;
                        if(comp[i].charAt(0) == '-'){
                            notOp[i] = false;
                            comp[i] = comp[i].substring(1);
                        }
                        variables[i] = Integer.parseInt(comp[i])-1;
                    }
                    clauses.add(new Clause(variables, notOp));
                }
                line = reader.readLine();
            }
            reader.close();

            sat = new SAT(clauses.toArray(new Clause[clauses.size()]), numVar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sat;
    }
}
