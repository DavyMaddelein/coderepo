/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.natter_remake.model;

/**
 *
 * @author Davy
 */
public class Intensity {

    private double value;
    private double retentionTime;
    private int scanid;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRetentionTime() {
        return retentionTime;
    }

    public void setRetentionTime(double retentionTime) {
        this.retentionTime = retentionTime;
    }

    public int getScanid() {
        return scanid;
    }

    public void setScanid(int scanid) {
        this.scanid = scanid;
    }
}
