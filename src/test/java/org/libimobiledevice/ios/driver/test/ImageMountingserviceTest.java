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


import org.libimobiledevice.ios.driver.binding.raw.JNAInit;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.ImageMountingService;
import org.libimobiledevice.ios.driver.binding.services.InformationService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class ImageMountingserviceTest {

  private
  File f = new File("/Applications/Xcode5.1.1.app/Contents/Developer/Platforms/iPhoneOS.platform/DeviceSupport/7.1 (11D167)/DeveloperDiskImage.dmg");

  @Test
  public void canMount() throws Exception {

    JNAInit.init();
    IOSDevice d = DeviceService.get(main);
    InformationService s = new InformationService(d);
    System.out.println(s.isDevModeEnabled());
    Assert.assertFalse(s.isDevModeEnabled());

    ImageMountingService service = new ImageMountingService(DeviceService.get(main));
    service.mount(f);

    Thread.sleep(2000);
    service.free();

    System.out.println(s.isDevModeEnabled());
//    Assert.assertTrue(s.isDevModeEnabled());
    s.free();
  }
}
