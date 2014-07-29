/*
 * Copyright 2012-2014 eBay Software Foundation and ios-driver committers
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


import org.libimobiledevice.ios.driver.binding.model.ProvisioningProfileInfo;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.ProvisioningService;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class ProvisioningServiceTest {

  String
      prof =
      "/Users/freynaud/UICatalog/Payload/UICatalog.app/embedded.mobileprovision";

  @Test
  public void canGetProfiles() throws Exception {
    IOSDevice d = DeviceService.get(main);
    ProvisioningService service = new ProvisioningService(d);
    List<ProvisioningProfileInfo> profiles = service.getProfiles();
    for (ProvisioningProfileInfo profile : profiles) {
      System.out.println(profile);
    }
  }

  @Test
  public void canGetProfile() throws Exception {
    ProvisioningProfileInfo p = ProvisioningService.getProfile(new File(prof));
    System.out.println(p);
  }
}