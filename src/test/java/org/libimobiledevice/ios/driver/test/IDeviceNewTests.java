package org.libimobiledevice.ios.driver.test;

import org.junit.Assert;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class IDeviceNewTests {


  @Test(groups = "smoke")
  public void device() throws SDKException {
    IOSDevice d = DeviceService.get(main);
    d.free();

  }

  @Test
  public void reportNotConnectedDevice() throws SDKException {
    DeviceService.get("idontexistaf579e27d166349dc8a1989503ba5b4f");
  }

  @Test
  public void deviceIsSingleton() throws SDKException {
    IOSDevice device = DeviceService.get(main);
    IOSDevice device2 = DeviceService.get(main);
    Assert.assertTrue(device == device2);
  }

}
