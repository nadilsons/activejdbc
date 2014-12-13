/*
Copyright 2009-2014 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/

package org.javalite.db_migrator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.javalite.db_migrator.DbUtils.closeQuietly;

public abstract class AbstractIntegrationSpec {

    protected String execute(File dir, String... args) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(args, null, dir);
        p.waitFor();
        String out = read(p.getInputStream());
        String err = read(p.getErrorStream());
        String output = "TEST MAVEN EXECUTION START >>>>>>>>>>>>>>>>>>>>>>>>\nOut: \n" + out
                + "\nErr:" + err + "\nTEST MAVEN EXECUTION END <<<<<<<<<<<<<<<<<<<<<<";
        if (p.exitValue() != 0) {
            System.out.println(output);
        }
        return output;
    }

    public static String read(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("input stream cannot be null");
        }
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in, "UTF-8");
            char[] buffer = new char[1024];
            StringBuilder sb = new StringBuilder();
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } finally {
            closeQuietly(reader);
        }
    }

}
