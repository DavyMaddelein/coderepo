/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.autoupdater;

import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Davy
 */
public class CompareVersionNumbers implements Comparator<String> {

    @Override
    public int compare(String oldVersionNumber, String newVersionNumber) {
        int compareInt = -1;
        Scanner a = (new Scanner(oldVersionNumber)).useDelimiter("\\.");
        Scanner b = (new Scanner(newVersionNumber)).useDelimiter("\\.");
        int i = 0, j = 0;
        if (!newVersionNumber.contains("b") || !newVersionNumber.contains("beta")) {
            while (a.hasNext() && b.hasNext()) {
                i = Integer.parseInt(a.next());
                j = Integer.parseInt(b.next());
                if (j > i) {
                    compareInt = 1;
                }
            }
            if (b.hasNext() && !a.hasNext()) {
                compareInt = 1;
            } else if (!b.hasNext() && !a.hasNext() && i == j) {
                compareInt = 0;
            }
        }
        return compareInt;
    }
}
