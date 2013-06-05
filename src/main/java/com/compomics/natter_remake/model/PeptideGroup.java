package com.compomics.natter_remake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Davy
 */
public class PeptideGroup {

    private List<Peptide> peptidesInGroup = new ArrayList<Peptide>();
    private int groupNumber;

    public List<Peptide> getPeptidesInGroup() {
        return Collections.unmodifiableList(peptidesInGroup);
    }

    public void addPeptide(Peptide peptide) {
        this.peptidesInGroup.add(peptide);
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public void addPeptides(List<Peptide> parsePeptideGroupPeptides) {
        peptidesInGroup.addAll(parsePeptideGroupPeptides);
    }
}
