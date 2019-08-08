package com.timmcmillan;

import java.util.ArrayList;

public class CellLetter {

    private ArrayList<String> letterOptionsList;

    public CellLetter(ArrayList<String> letterOptions) {
        this.letterOptionsList = letterOptions;
    }

    public ArrayList<String> getLetterOptions() {
        return letterOptionsList;
    }

    public CellLetter(String cellLetter) {

        ArrayList<String> letterOptions = new ArrayList<String>();
        letterOptions.add(cellLetter);
        this.letterOptionsList = letterOptions;
    }

    public void setLetterOptions(ArrayList<String> letterOptions) {
        this.letterOptionsList = letterOptions;
    }

}
