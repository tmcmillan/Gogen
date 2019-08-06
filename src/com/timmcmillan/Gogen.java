package com.timmcmillan;

import java.lang.reflect.Array;
import java.util.*;

public class Gogen {

    Scanner scanner = new Scanner(System.in);

    public void newGogen() {

        String[] inputLetters = inputLetters();
        ArrayList<String> inputWords = inputWords();
        int iterations = 0;
        boolean solved=false;

//        String[] inputLetters = {"X", "C", "F", "G", "H", "W", "Q", "D", "Y"};
//        ArrayList<String> inputWords = new ArrayList<>();
//        inputWords.add("BAWLING");
//        inputWords.add("CRAVE");
//        inputWords.add("FAMILY");
//        inputWords.add("HIM");
//        inputWords.add("JUNKET");
//        inputWords.add("MOP");
//        inputWords.add("NEXT");
//        inputWords.add("SIP");
//        inputWords.add("SQUID");

        CellLetter[][] letterPositions = setInitialLetterPositions(inputLetters);


        drawBoard(letterPositions);
        printWords(inputWords);

        if (checkWordInputIsValid(inputWords) == false) {
            System.out.println("\nThe input contains some characters that are not letters");
            solved = true;
        }

        ArrayList<String> knownLetters;


        while(!solved) {

            letterPositions = updateCellPossibilities(letterPositions, inputWords);
            knownLetters = knownLetters(letterPositions);
            iterations++;

            if(knownLetters.size()==25){
                solved=true;
                System.out.println("Solved after " + iterations + " iterations");

                drawBoard(letterPositions);
            }

            if(iterations>200){
                System.out.println("\n\nHmmmm, I'm stumped with this one! Are you sure the input is right?");
                break;
            }

        }

    }



    public String[] inputLetters() {


        String[] inputLetters = new String[9];
        ArrayList<String> alphabet = new ArrayList<>();

        for (char x = 'A'; x <= 'Y'; x++) {
            alphabet.add(Character.toString(x));
        }

        System.out.println("Welcome to the game!\n");
        System.out.println("Please enter the 9 letters to be given to the player. \n" +
                "Starting with the top left value, working across the columns, and then down the rows\n");

        int i= 1;

        while(i<10) {
            System.out.println("Enter letter " + i);
            inputLetters[i - 1] = scanner.nextLine().toUpperCase();
            if(inputLetters[i-1].length()==1 && alphabet.contains(inputLetters[i-1])) {
                i++;
            } else {
                System.out.println("Invalid input. Try again");
            }
        }
        return inputLetters;
    }

    public ArrayList<String> inputWords() {


        ArrayList<String> inputWords = new ArrayList();

        boolean stop = false;

        System.out.println("Please enter the words to be given to the player. Enter -1 when all words have been specified\n");

        while (!stop) {
            System.out.println("Enter word: ");

            if (scanner.hasNextInt() && scanner.nextInt() == -1) {
                stop = true;
            } else {
                inputWords.add(scanner.nextLine().toUpperCase());
            }
        }
        return inputWords;

    }


    public CellLetter[][] setInitialLetterPositions(String[] inputLetters) {

        CellLetter[][] letterPositions = new CellLetter[5][5];

        int k = 0;

        for (int i = 0; i < 5; i += 2) {
            for (int j = 0; j < 5; j += 2) {
                letterPositions[i][j] = new CellLetter(inputLetters[k]);
                k++;
            }
        }

        for (int i = 0; i < 5; i += 2) {
            for (int j = 1; j < 5; j += 2) {
                letterPositions[i][j] = new CellLetter(availableLetters(letterPositions));
            }
        }

        for (int i = 1; i < 5; i += 2) {
            for (int j = 0; j < 5; j++) {
                letterPositions[i][j] = new CellLetter(availableLetters(letterPositions));
            }
        }

        return letterPositions;
    }

    public String letter(CellLetter[][] letterPositions, int i, int j) {

        String letter = "";

        if (letterPositions[i][j] == null) {
            letter = " ";
        } else {
            if (letterPositions[i][j].getLetterOptions().size() == 1) {
                letter = letterPositions[i][j].getLetterOptions().get(0);
            } else {
                letter = " ";
            }
        }
        return letter;
    }


    public void drawBoard(CellLetter[][] letterPositions) {


        String firstLineOfBoxes = "\n         1            2            3            4            5    \n" +
                "      _______      _______      _______      _______      _______ \n" +
                "     |       |    |       |    |       |    |       |    |       |\n" +
                "  A  |   " + letter(letterPositions, 0, 0) + "   |----|   " + letter(letterPositions, 0, 1) + "   |----|   " + letter(letterPositions, 0, 2) + "   |----|   " + letter(letterPositions, 0, 3) + "   |----|   " + letter(letterPositions, 0, 4) + "   |\n" +
                "     |_______|    |_______|    |_______|    |_______|    |_______|";

        System.out.println(firstLineOfBoxes);

        String subsequantLineOfBoxes = "";

        for (int i = 1; i < 5; i++) {
            char j = (char) ('A' + i + 1);
            subsequantLineOfBoxes = "         |   \\   /    |   \\   /    |   \\   /    |   \\   /    |\n" +
                    "         |    \\_/     |    \\_/     |    \\_/     |    \\_/     |\n" +
                    "         |    / \\     |    / \\     |    / \\     |    / \\     |\n" +
                    "      ___|___/   \\ ___|___/   \\ ___|___/   \\ ___|___/   \\ ___|___\n" +
                    "     |       |    |       |    |       |    |       |    |       |\n" +
                    "  " + (char) (j - 1) + "  |   " + letter(letterPositions, i, 0) + "   |----|   " + letter(letterPositions, i, 1) + "   |----|   " + letter(letterPositions, i, 2) + "   |----|   " + letter(letterPositions, i, 3) + "   |----|   " + letter(letterPositions, i, 4) + "   |\n" +
                    "     |_______|    |_______|    |_______|    |_______|    |_______|";

            System.out.println(subsequantLineOfBoxes);
        }
    }

    public void printWords(ArrayList<String> inputWords) {

        System.out.println("Words: " + inputWords);

    }

    public ArrayList<String> convertWordToLetters(String inputWord) {

        ArrayList<String> convertedWord = new ArrayList();

        for (int i = 0; i < inputWord.length(); i++) {
            convertedWord.add(Character.toString(inputWord.charAt(i)));
        }
        return convertedWord;
    }

    public ArrayList<ArrayList<String>> listWordsAsLetters(List<String> inputWords) {

        ArrayList<ArrayList<String>> wordsAsLetters = new ArrayList();

        for (int i = 0; i < inputWords.size(); i++) {
            wordsAsLetters.add(convertWordToLetters(inputWords.get(i)));
        }
        return wordsAsLetters;
    }

    public boolean checkWordInputIsValid(ArrayList<String> inputWords) {


        boolean validInput = true;
        ArrayList<ArrayList<String>> wordsAsLetters = listWordsAsLetters(inputWords);
        ArrayList<String> alphabet = new ArrayList<>();

        for (char x = 'A'; x <= 'Y'; x++) {
            alphabet.add(Character.toString(x));
        }


        for (int i=0; i<inputWords.size(); i++){
            for(int j=0; j<wordsAsLetters.get(i).size(); j++) {
                if (alphabet.contains(wordsAsLetters.get(i).get(j))){
            } else {
                validInput = false;
            }

        }
    }
        return validInput;
    }


    public ArrayList<String> availableLetters(CellLetter[][] letterPositions) {

        ArrayList<String> availableLetters = new ArrayList<>();


        for (char x = 'A'; x <= 'Y'; x++) {
            availableLetters.add(Character.toString(x));
        }


        for (int i = 0; i < 5; i += 2) {
            for (int j = 0; j < 5; j += 2) {

                if (letterPositions[i][j] == null) {
                } else {
                    if (letterPositions[i][j].getLetterOptions().size() == 1) {
                        availableLetters.remove(letterPositions[i][j].getLetterOptions().get(0));
                    }
                }
            }
        }

        return availableLetters;
    }

    public ArrayList<String> knownLetters(CellLetter[][] letterPositions) {

        ArrayList<String> knownLetters = new ArrayList<String>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (letterPositions[i][j].getLetterOptions().size() == 1) {
                    knownLetters.add(letterPositions[i][j].getLetterOptions().get(0));
                }
            }
        }
        return knownLetters;
    }


    public CellLetter[][] updateCellPossibilities(CellLetter[][] letterPositions, ArrayList<String> inputWords) {

        ArrayList<ArrayList<String>> listWordsAsLetters = listWordsAsLetters(inputWords);
        String letterOfInterest;
        String neighbouringLetter;
        ArrayList<String> knownLetters;

        for (int i = 0; i < inputWords.size(); i++) {
            for (int j = 0; j < listWordsAsLetters.get(i).size() - 1; j++) {
                letterOfInterest = listWordsAsLetters.get(i).get(j);
                neighbouringLetter = listWordsAsLetters.get(i).get(j + 1);


                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {

                        if (letterPositions[k][l].getLetterOptions().size() == 1 || !(letterPositions[k][l].getLetterOptions().contains(letterOfInterest))) {
                        } else {
                            if (cellPosition(k, l).equals("A2") || cellPosition(k, l).equals("A4")) {
                                if (letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("B1") || cellPosition(k, l).equals("C1") || cellPosition(k, l).equals("D1")) {
                                if (letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("B2") || cellPosition(k, l).equals("B3") || cellPosition(k, l).equals("B4") ||
                                    cellPosition(k, l).equals("C2") || cellPosition(k, l).equals("C3") || cellPosition(k, l).equals("C4") ||
                                    cellPosition(k, l).equals("D2") || cellPosition(k, l).equals("D3") || cellPosition(k, l).equals("D4")) {
                                if (letterPositions[k - 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("B5") || cellPosition(k, l).equals("C5") || cellPosition(k, l).equals("D5")) {
                                if (letterPositions[k - 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("E2") || cellPosition(k, l).equals("E3") || cellPosition(k, l).equals("E4")) {
                                if (letterPositions[k - 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            }
                        }
                    }
                }
                knownLetters = knownLetters(letterPositions);
                for (int z = 0; z < knownLetters.size(); z++) {
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            if (letterPositions[x][y].getLetterOptions().size() > 1 && letterPositions[x][y].getLetterOptions().contains(knownLetters.get(z))) {
                                letterPositions[x][y].getLetterOptions().remove(knownLetters.get(z));
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < inputWords.size(); i++) {
            for (int j = 1; j < listWordsAsLetters.get(i).size(); j++) {
                letterOfInterest = listWordsAsLetters.get(i).get(j);
                neighbouringLetter = listWordsAsLetters.get(i).get(j - 1);


                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 5; l++) {

                        if (letterPositions[k][l].getLetterOptions().size() == 1 || !(letterPositions[k][l].getLetterOptions().contains(letterOfInterest))) {
                        } else {
                            if (cellPosition(k, l).equals("A2") || cellPosition(k, l).equals("A4")) {
                                if (letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("B1") || cellPosition(k, l).equals("C1") || cellPosition(k, l).equals("D1")) {
                                if (letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("B2") || cellPosition(k, l).equals("B3") || cellPosition(k, l).equals("B4") ||
                                    cellPosition(k, l).equals("C2") || cellPosition(k, l).equals("C3") || cellPosition(k, l).equals("C4") ||
                                    cellPosition(k, l).equals("D2") || cellPosition(k, l).equals("D3") || cellPosition(k, l).equals("D4")) {
                                if (letterPositions[k - 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("B5") || cellPosition(k, l).equals("C5") || cellPosition(k, l).equals("D5")) {
                                if (letterPositions[k - 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k + 1][l].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            } else if (cellPosition(k, l).equals("E2") || cellPosition(k, l).equals("E3") || cellPosition(k, l).equals("E4")) {
                                if (letterPositions[k - 1][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k - 1][l + 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l - 1].getLetterOptions().contains(neighbouringLetter) ||
                                        letterPositions[k][l + 1].getLetterOptions().contains(neighbouringLetter)) {
                                } else {
                                    letterPositions[k][l].getLetterOptions().remove(letterOfInterest);
                                }
                            }
                        }
                    }
                }

                knownLetters = knownLetters(letterPositions);
                for (int z = 0; z < knownLetters.size(); z++) {
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            if (letterPositions[x][y].getLetterOptions().size() > 1 && letterPositions[x][y].getLetterOptions().contains(knownLetters.get(z))) {
                                letterPositions[x][y].getLetterOptions().remove(knownLetters.get(z));
                            }
                        }
                    }
                }
            }
        }



        return letterPositions;
}



    public String cellPosition(int i, int j) {

        String cellPosition;
        char rowPosition;


        rowPosition = (char) (65 + i);

        cellPosition = Character.toString(rowPosition)+Integer.toString(j+1);

        return cellPosition;
    }

}
