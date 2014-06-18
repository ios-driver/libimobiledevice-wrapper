package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.testng.annotations.Test;

public class DeviceDetectorTest {


  @Test
  public void ok() throws InterruptedException, SDKException {

    DeviceService service = DeviceService.INSTANCE;
    service.startDetection(null);
    Thread.sleep(60000);

  }


}
