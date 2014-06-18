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

import com.sun.jna.Pointer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.libimobiledevice.ios.driver.binding.raw.ImobiledeviceSdkLibrary.sdk_idevice_syslog_service_read_cb_t;

public class SyslogMessageListeners implements sdk_idevice_syslog_service_read_cb_t {

  private final List<SysLogListener> all = new CopyOnWriteArrayList<SysLogListener>();
  private StringBuffer buff = new StringBuffer();

  @Override
  public void apply(byte b, Pointer user_data) {
    char c = (char) b;
    if (c == '\n') {
      String line = buff.toString();
      buff = new StringBuffer();
      for (SysLogListener h : all) {
        SysLogLine log = null;
        try {
          log = new SysLogLine(line);
          h.onLog(log);
        } catch (SysLogLine.LogParsingException e) {
          System.err.println(e.getMessage());
        }
      }
    } else {
      buff.append(c);
    }
  }

  public void add(SysLogListener listener) {
    all.add(listener);
  }

  public void remove(SysLogListener listener) {
    all.remove(listener);
  }

  public int size() {
    return all.size();

  }
}
