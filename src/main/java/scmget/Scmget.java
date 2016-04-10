/*
 */
package scmget;

import com.beust.jcommander.JCommander;
import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
        if (targetDirectory.exists()) {
            throw new RuntimeException(String.format("CVS checkout - target directory already exists: '%s'", targetDirectory.getAbsolutePath()));
        }

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
                boolean result = client.executeCommand(cmd, globalOptions);
                if (result) {
                    File checkedOutModuleDir = new File(targetDirectoryParent, FilenameUtils.getName(FilenameUtils.getFullPathNoEndSeparator(FilenameUtils.concat(cvsRoot, cvsModule))));
                    if (!checkedOutModuleDir.equals(targetDirectory)) {
                        try {
                            FileUtils.moveDirectory(checkedOutModuleDir, targetDirectory);
                            result = true;
                        } catch (IOException ioe) {
                            result = false;
                            throw new RuntimeException(String.format("CVS checkout - failed to move the checked out directory from : %s to %s", checkedOutModuleDir.getAbsolutePath(), targetDirectory.getAbsolutePath()));
                        }
                    }
                }
                return result;
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
                            System.err.println(gitCmd.getErrorMessage());
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
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}
