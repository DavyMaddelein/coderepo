package com.compomics.natter_remake.model;

import java.util.ArrayList;

/**
 *
 * @author Davy
 */
public class IntensityList extends ArrayList<Intensity>{
    private boolean valid;
    private double peakStart;
    private double peakEnd;
    private double peakRegionEnd;
    private double peakRegionStart;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setPeakStart(double peakStart) {
        this.peakStart = peakStart;
    }

    public void setPeakEnd(double peakEnd) {
        this.peakEnd = peakEnd;
    }

    public void setPeakRegionEnd(double peakRegionEnd) {
        this.peakRegionEnd = peakRegionEnd;
    }

    public void setPeakRegionStart(double peakRegionStart) {
        this.peakRegionStart = peakRegionStart;
    }

    public boolean isValid() {
        return valid;
    }

    public double getPeakStart() {
        return peakStart;
    }

    public double getPeakEnd() {
        return peakEnd;
    }

    public double getPeakRegionEnd() {
        return peakRegionEnd;
    }

    public double getPeakRegionStart() {
        return peakRegionStart;
    }
    
}
