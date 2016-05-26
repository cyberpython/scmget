/*
 * Copyright 2016 Georgios Migdos
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scmget;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.checkout.CheckoutCommand;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.netbeans.lib.cvsclient.connection.StandardScrambler;

import com.beust.jcommander.JCommander;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Scmget {

    public static boolean checkoutCVSTag(CvsCommand cvsCmd) {
        return checkoutCVSTag(cvsCmd.getHost(), cvsCmd.getCvsroot(), cvsCmd.getModule(), cvsCmd.getTag(), cvsCmd.getUsername(), cvsCmd.getPassword(), cvsCmd.getTargetDir());
    }

    public static boolean cloneAndCheckoutGitTag(GitCommand gitCmd) {
        return cloneAndCheckoutGitTag(gitCmd.getUrl(), gitCmd.getTag(), gitCmd.getUsername(), gitCmd.getPassword(), gitCmd.getTargetDir());
    }

    public static XmlConfig loadXmlConfig(File file) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(XmlConfig.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        XmlConfig result = (XmlConfig) jaxbUnmarshaller.unmarshal(file);
        return result;

    }

    public static boolean checkoutCVSTag(String cvsHost, String cvsRoot,
            String cvsModule, String tagName, String username, String password,
            String targetDir) {

        File targetDirectory = new File(targetDir);

        File targetDirectoryParent = targetDirectory.getParentFile();
        String targetDirParent = targetDirectoryParent.getAbsolutePath();

        if (!cvsModule.endsWith("/")) {
            cvsModule = cvsModule + "/";
        }

        PServerConnection c = new PServerConnection();
        c.setUserName(username);
        c.setEncodedPassword(StandardScrambler.getInstance().scramble(password));
        c.setHostName(cvsHost);
        c.setRepository(cvsRoot);

        GlobalOptions globalOptions = new GlobalOptions();
        globalOptions.setCVSRoot(cvsRoot);

        try {

            Client client = new Client(c, new StandardAdminHandler());
            client.setLocalPath(targetDirParent);
            client.getConnection().open();
            try {
                CheckoutCommand cmd = new CheckoutCommand(true, cvsModule);
                cmd.setCheckoutByRevision(tagName);
                cmd.setCheckoutDirectory(targetDir);
                cmd.setPruneDirectories(true);
                return client.executeCommand(cmd, globalOptions);
            } catch (CommandException ce) {
                ce.printStackTrace();
            }

            try {
                client.getConnection().close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        } catch (AuthenticationException ae) {
            ae.printStackTrace();
        } catch (CommandAbortedException cae) {
            cae.printStackTrace();
        }
        return false;
    }

    public static boolean cloneAndCheckoutGitTag(String gitRepoURL, String tagName, String username, String password, String targetDir) {
        File targetDirectory = new File(targetDir);
        if (targetDirectory.exists()) {
            throw new RuntimeException(String.format("Git clone - target directory already exists: '%s'", targetDirectory.getAbsolutePath()));
        }
        CloneCommand cmd = new CloneCommand();
        cmd.setBare(false);
        cmd.setNoCheckout(false);
        cmd.setBranch(tagName);
        cmd.setDirectory(targetDirectory);
        cmd.setURI(gitRepoURL);
        cmd.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
        try {
            Git git = cmd.call();
            git.close();
            return true;
        } catch (GitAPIException gae) {
            gae.printStackTrace();
        }
        return false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        try{
            JCommander jc = new JCommander();
            jc.setProgramName("scmget");

            XmlCommand xmlCmd = new XmlCommand();
            jc.addCommand("xml", xmlCmd);
            GitCommand gitCmd = new GitCommand();
            jc.addCommand("git", gitCmd);
            CvsCommand cvsCmd = new CvsCommand();
            jc.addCommand("cvs", cvsCmd);

            try {
                jc.parse(args);
            } catch (Exception e) {
                jc.usage();
                System.exit(1);
            }

            boolean success = false;
            if (jc.getParsedCommand() == null) {
                jc.usage();
            } else if (jc.getParsedCommand().equals("xml")) {
                try{
                    XmlConfig xmlConfig = loadXmlConfig(new File(xmlCmd.getFilePath()));
                    success = true;
                    for(ScmCommand scmCmd : xmlConfig.getCommands()){
                        success = scmCmd.apply();
                        if(!success){
                            System.err.println(scmCmd.getErrorMessage());
                            break;
                        }
                    }
                }catch(JAXBException je){
                    System.err.println(String.format("Could not load configuration file: %s", xmlCmd.getFilePath()));
                    je.printStackTrace();
                    success = false;
                }

            } else if (jc.getParsedCommand().equals("git")) {
                success = gitCmd.apply();
                if (!success) {
                    System.err.println(gitCmd.getErrorMessage());
                }
            } else {
                success = cvsCmd.apply();
                if (!success) {
                    System.err.println(cvsCmd.getErrorMessage());
                }
            }

            System.exit(success ? 0 : 1);
        }catch(Exception e){
        	e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}
