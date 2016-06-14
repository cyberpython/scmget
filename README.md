# scmget

## Description
`scmget` is a simple Java application and library that clones / checks out 
tagged code from SCM systems. Currently only Git and CVS are supported.
CVS checkouts are performed in a temporary directory and then this directory is
merged with the target directory (if it exists) in order to facilitate the 
checkout of multiple checkouts of differently tagged subtrees of a module.

The user has to provide information about the server, the repository and/or 
module, the tag and the credentials to be used.
For multiple projects an XML file can be used.

The credentials are provided in the `scmget_credentials.xml` file located
in the user's home directory.

### Sample scmget_credentials.xml file

     <credentials>
        <git host="gitserver" username="testuser" password="mypassword"/>
        <cvs host="mycvsserver" username="testuser" password="mypassword"/>
        <cvs host="myothercvsserver" username="testuser2" password="mypassword2"/>
     </credentials>

## Usage

### Command-line usage

     scmget [command] [command options]

Commands and options:

    xml      Perform multiple checkouts specified in an XML file
      Usage: xml [options]
        Options:
        * --file
             The XML file to use as input
    
    git      Checkout tagged a tagged project from a Git repository
      Usage: git [options]
        Options:
        * --tag
             The tag to checkout
        * --targetDir
             The target absolute or relative filesystem path
        * --url
             The Git repository URL
    
    cvs      Checkout a tagged module from a CVS repository
      Usage: cvs [options]
        Options:
        * --cvsroot
             The root of the CVS repository
        * --host
             The hostname or IP address of the CVS server
        * --module
             The CVS module to checkout
        * --tag
             The tag to checkout
        * --targetDir
             The target absolute or relative filesystem path

### Sample XML configuration file

     <configuration>
       
       <git url="https://testuser@gitserver/git/MyAwesomeProject.git" 
            target-dir="/home/jdoe/builds/myawesomeproj"
            tag="v0.2"
       />
       
       <cvs host="mycvsserver"
            cvsroot="/var/lib/CVSROOT"
            module="myawesomelib"
            target-dir="//home/jdoe/builds/deplib_1"
            tag="TAG_TEST_1"
       />
     </configuration>

### Why would I need it???

You can use it to checkout the source code for a project and associated source 
dependencies e.g. to perform a release.

## Build instructions
Clone the git repository, change into the project's directory and execute:

    ./gradlew distZip

or on Windows:

    gradlew.bat distZip

A ZIP archive containing the executable JAR file, all dependencies and launcher 
scripts should be created under `build/distributions` .

## Dependencies and used libraries

* [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
* [JGit (and its dependencies)](https://eclipse.org/jgit/)
* [JCommander](http://jcommander.org/)
* [Netbeans CVS client](https://netbeans.org/projects/versioncontrol/downloads/download/org-netbeans-lib-cvsclient.jar)

## License

Copyright 2016 Georgios Migdos

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



