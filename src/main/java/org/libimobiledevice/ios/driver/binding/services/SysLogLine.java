/*
 * Copyright 2012-2013 eBay Software Foundation and ios-driver committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.libimobiledevice.ios.driver.binding.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SysLogLine {

  private Date date;
  private String process;
  private int pid;
  private String level;
  private String message;
  private String original;

  SysLogLine(String line) throws LogParsingException {
    original = line;
    // Date
    try {
      String d = Calendar.getInstance().get(Calendar.YEAR) + " " + line.substring(0, 15);
      SimpleDateFormat parser = new SimpleDateFormat("yyyy MMM d HH:mm:ss");
      date = parser.parse(d);

      // process and its pid
      String rest = line.substring(15);
      int openingBracketPid = rest.indexOf("[");
      int closingBracketPid = rest.indexOf("]");
      // removing the 2 spaces
      process = rest.substring(2, openingBracketPid);
      pid = Integer.parseInt(rest.substring(openingBracketPid + 1, closingBracketPid));

      rest = rest.substring(closingBracketPid + 2);

      // level
      int index = rest.indexOf('>');
      level = rest.substring(1, index);

      // removing the leading ':'
      rest = rest.substring(index + 2);

      // and trim leading space
      message = rest.trim();
    } catch (Exception e) {
      throw new LogParsingException("Cannot parse line (is it a full line ?)"+original);
    }
  }

  static class LogParsingException extends Exception {

    public LogParsingException(String message) {
      super(message);
    }
  }

  @Override
  public String toString() {
    return original;
  }

  public Date getDate() {
    return date;
  }

  public String getProcess() {
    return process;
  }

  public int getPid() {
    return pid;
  }

  public String getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }
}
