package com.compomics.natter_remake.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davy
 */
public class Protein {

    private String accession;
    private int score;
    private double mass;
    private Ratio ratio;
    private List<PeptideMatch> peptideMatches = new ArrayList<PeptideMatch>();

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setRatio(Ratio ratio) {
        this.ratio = ratio;
    }

    public void addLinkToPeptideMatch(PeptideMatch peptideMatch) {
        peptideMatches.add(peptideMatch);
    }
}
