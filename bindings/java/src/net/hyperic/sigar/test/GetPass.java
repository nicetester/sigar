package net.hyperic.sigar.test;

import java.io.IOException;

import net.hyperic.sigar.Sigar;

public class GetPass {
    public static void main(String[] args) throws Exception {

        try {
            String password = Sigar.getPassword("Enter password: ");
            System.out.println("You entered: ->" + password + "<-");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
