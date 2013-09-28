package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.sdk.DebugService;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.binding.sdk.InstallerService;
import org.libimobiledevice.ios.driver.binding.sdk.SysLogService;
import org.testng.annotations.Test;

import java.io.File;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.device2;
import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class DebugTest {


  @Test(groups = "smoke")
  public void deviceCanDebugLaunch() throws InterruptedException, LibImobileException {
    IDeviceSDK d = new IDeviceSDK(device2);
    DebugService service = new DebugService(d);
    service.launch("com.apple.mobilesafari");
    //service.launch("com.yourcompany.UICatalog");

    service.free();
  }
}
