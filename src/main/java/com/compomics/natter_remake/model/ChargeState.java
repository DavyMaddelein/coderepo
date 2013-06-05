package com.compomics.natter_remake.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davy
 */
public class ChargeState {

    private double correlation;
    private double bucketWitdh;
    private double totalIntensity;
    private List<Scan> scansForCharge = new ArrayList<Scan>(20);

    public void setBucketWidth(double bucketWidth) {
        this.bucketWitdh = bucketWidth;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }

    public void setTotalIntensity(double totalIntensity) {
        this.totalIntensity = totalIntensity;
    }

    public void addScan(Scan scan) {
        scansForCharge.add(scan);
    }
}
