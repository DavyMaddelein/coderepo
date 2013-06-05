package com.compomics.natter_remake.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Davy
 */
public class PeptideMatch {

    ArrayList<PeptidePartner> peptidePartners = new ArrayList<PeptidePartner>();
    private int matchId;
    private int chargeState;
    private String peptideSequence;
    private double intensity;
    private Ratio originalRatio;
    private Ratio hitRatio;
    private ChargeState chargeStateData;

    public void addPartner(PeptidePartner parsePeptidePartner) {
        peptidePartners.add(parsePeptidePartner);
    }

    public List<PeptidePartner> getMatchedPartners() {
        return Collections.unmodifiableList(peptidePartners);
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setChargeState(int chargeState) {
        this.chargeState = chargeState;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public void setPeptideSequence(String peptideSequence) {
        this.peptideSequence = peptideSequence;
    }

    public void setMods(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addOriginalRatio(Ratio originalRatio) {
        this.originalRatio = originalRatio;
    }

    public void addHitRatio(Ratio hitRatio) {
        this.hitRatio = hitRatio;
    }

    public void addChargeStateData(ChargeState chargeStateData) {
        this.chargeStateData = chargeStateData;
    }

    public List<PeptidePartner> getPeptidePartners() {
        return Collections.unmodifiableList(peptidePartners);
    }

    public int getMatchId() {
        return matchId;
    }

    public int getChargeState() {
        return chargeState;
    }

    public String getPeptideSequence() {
        return peptideSequence;
    }

    public double getIntensity() {
        return intensity;
    }

    public Ratio getOriginalRatio() {
        return originalRatio;
    }

    public Ratio getHitRatio() {
        return hitRatio;
    }

    public ChargeState getChargeStateData() {
        return chargeStateData;
    }
}
