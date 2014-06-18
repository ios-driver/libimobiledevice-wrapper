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

import org.apache.commons.io.IOUtils;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.ScreenshotService;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

/**
 * Created by freynaud on 08/04/2014.
 */
public class ScreenshotTest {

  @Test(expectedExceptions = SDKException.class)
  public void notDevDetected() throws SDKException, IOException {
    IOSDevice d = DeviceService.get(main);
    ScreenshotService service = new ScreenshotService(d);
  }

  @Test(groups = "smoke", invocationCount = 10)
  public void screenshot() throws SDKException, IOException {
    IOSDevice d = DeviceService.get(main);
    ScreenshotService service = new ScreenshotService(d);
    byte[] bytes = service.takeScreenshot();
    d.free();

    FileOutputStream output = new FileOutputStream(new File("target-file.tiff"));
    IOUtils.write(bytes, output);
    IOUtils.closeQuietly(output);
  }
}
