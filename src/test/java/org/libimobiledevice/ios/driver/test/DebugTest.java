package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DebugService;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class DebugTest {


  @Test(groups = "smoke")
  public void deviceCanDebugLaunch() throws InterruptedException, LibImobileException,
                                            SDKException {
    DebugService service = new DebugService(DeviceService.get(main));
    service.launch("com.apple.mobilesafari");
    //service.launch("com.yourcompany.UICatalog");

    service.free();
  }
}
