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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
@Parameters(separators = "=", commandDescription = "Perform multiple checkouts specified in an XML file")
public class XmlCommand {

    @Parameter(names = "--file", required = true, description = "The XML file to use as input")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }
    
    
}
