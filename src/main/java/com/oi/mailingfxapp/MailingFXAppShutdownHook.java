/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oi.mailingfxapp;

import com.oi.mailingfxapp.core.MailingFXApp;

/**
 *
 * @author mmouraam
 */
public class MailingFXAppShutdownHook implements Runnable {
    
    @Override
    public void run() {
        MailingFXApp.shutdown();
    }
}
