/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.natter_remake.model;

/**
 *
 * @author Davy
 */
    public class Project {

        private int projectId;
        private String projectName;

       public Project(int projectId, String projectName) {
            this.projectId = projectId;
            this.projectName = projectName;
        }

        public int getProjectId() {
            return projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        @Override
        public String toString() {
            return projectId + " - " + projectName;
        }
    }
