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

package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.SysLogListener;
import org.libimobiledevice.ios.driver.binding.services.SysLogService;
import org.testng.annotations.Test;

public class SysLogListenersTest {

  @Test
  public void smoke() throws LibImobileException, SDKException, InterruptedException {
    IOSDevice d = DeviceService.get(ConnectedDevices.main);
    SysLogService log = d.getSysLogService();

    SysLogListener l = new SysLogListener() {
      @Override
      public void onCharacter(char c) {
        System.out.print(c);
      }
    };
    log.addListener(l);
    Thread.sleep(1000);

    log.addListener(new SysLogListener() {
      @Override
      public void onCharacter(char c) {
        System.err.print(c);
      }
    });

    Thread.sleep(1000);

    log.remove(l);

    Thread.sleep(1000);

  }
}
