package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.sdk.AppContainerService;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.fviscomi;

/**
 * Created with IntelliJ IDEA. User: fviscomi Date: 9/30/13 Time: 4:04 PM To change this template
 * use File | Settings | File Templates.
 */
public class ApplicationContainerServiceTest {

  @Test
  public void emptyApplicationCache() {
    IDeviceSDK d = new IDeviceSDK(fviscomi);
    AppContainerService appContainerService = new AppContainerService(d);
    appContainerService.clean("com.ebay.iphone");
    appContainerService.free();
  }
}
