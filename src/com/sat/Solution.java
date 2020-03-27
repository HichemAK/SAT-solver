package com.sat;

import java.util.HashMap;

public class Solution extends HashMap<Variable, Boolean> {

    public Solution(Solution solution) {
        super(solution);
    }
    public Solution(){
        super();
    }
}
