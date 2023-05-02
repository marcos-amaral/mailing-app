/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp.enums;

/**
 *
 * @author mmouraam
 */
public enum DatabaseVendor {
    ORACLE("Oracle"),
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    SQLSERVER("SQL Server");
    
    private String label;

    private DatabaseVendor(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    
}
