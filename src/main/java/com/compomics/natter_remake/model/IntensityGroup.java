/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.natter_remake.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Davy
 */
public class IntensityGroup {
    
    private int value;
    private int retentiontime;
    private int scanid;
    private int peakStart;
    private int peakEnd;
    private int regionStart;
    private int regionEnd;
    private List<Intensity> groupedIntensities = new ArrayList<Intensity>();
    
    
    public IntensityGroup(int value,int retentionTime,int scanid,int peakStart, int peakEnd, int regionStart, int regionEnd){
    
        this.value = value;
        this.retentiontime = retentionTime;
        this.scanid = scanid;
        this.peakStart = peakStart;
        this.peakEnd = peakEnd;
        this.regionStart = regionStart;
        this.regionEnd = regionEnd;
    }

    public int getValue() {
        return value;
    }

    public int getRetentiontime() {
        return retentiontime;
    }

    public int getScanid() {
        return scanid;
    }

    public int getPeakStart() {
        return peakStart;
    }

    public int getPeakEnd() {
        return peakEnd;
    }

    public int getRegionStart() {
        return regionStart;
    }

    public int getRegionEnd() {
        return regionEnd;
    }

    public List<Intensity> getGroupedIntensities() {
        return groupedIntensities;
    }
}
