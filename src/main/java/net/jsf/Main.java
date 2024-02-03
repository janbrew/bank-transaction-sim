package net.jsf;

import net.jsf.brain.Bank;
import net.jsf.face.Visual;

public class Main {
    public static void main(String[] args) {
        Bank.generateAccount();
        new Visual();
    }
}