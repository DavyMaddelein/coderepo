package com.compomics.natter_remake.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davy
 */
public class Peptide {
    
    private String sequence;
    private boolean valid = false;
    private String composition;
    private int peptideMatchId;
    private List<PeptidePartner> peptidesInMatch = new ArrayList<PeptidePartner>();
    private int peptideGroupHitNumber;
    private PeptideGroup peptideGroup;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public int getPeptideMatchId() {
        return peptideMatchId;
    }

    public void setPeptideMatchId(int peptideMatchId) {
        this.peptideMatchId = peptideMatchId;
    }

    public List<PeptidePartner> getPeptidesInMatch() {
        return peptidesInMatch;
    }

    public void setPeptidesInMatch(List<PeptidePartner> peptidesInMatch) {
        this.peptidesInMatch = peptidesInMatch;
    }

    public void setPeptideNumber(int peptideNumber) {
        this.peptideGroupHitNumber = peptideNumber;
    }
    public int getPeptideNumber(){
        return this.peptideGroupHitNumber;
    }

    public void setPeptideGroup(PeptideGroup peptideGroup) {
    this.peptideGroup = peptideGroup;
            }
    public PeptideGroup getPeptideGroup(){
        return peptideGroup;
    }
    
}
