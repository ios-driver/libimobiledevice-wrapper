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

import com.sun.jna.ptr.PointerByReference;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.*;

import org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsSDKException;
import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.testng.annotations.Test;

public class ApplicationLauncher {

  @Test
  public void canLaunch()
      throws LibImobileException, SDKException, InstrumentsSDKException, InterruptedException {
    String bundleId= "com.apple.mobilesafari";
    IOSDevice device = DeviceService.get(main);
    InstrumentsClient instrumentsClient = new InstrumentsClient(device);

    AutomationClient automationClient = instrumentsClient.getAutomationClient();
    ProcessControlClient processControlClient = instrumentsClient.getProcessControlClient();


    int pid = processControlClient.getPid(bundleId);
    System.out.println(pid);

    automationClient.configureLaunchEnvironment(new PointerByReference());

    pid = processControlClient.launchSuspendedProcess(bundleId, null, null);
    pid = processControlClient.getPid(bundleId);
    System.out.println(pid);
    processControlClient.resume(pid);


  }
}
