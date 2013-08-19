package org.libimobiledevice.ios.driver.test;

import org.junit.Assert;
import org.libimobiledevice.ios.driver.binding.IMobileDeviceFactory;
import org.libimobiledevice.ios.driver.binding.IOSDevice;
import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.sdk.IDevice;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class IDeviceNewTests {


  private IMobileDeviceFactory factory = IMobileDeviceFactory.INSTANCE;

  @Test(groups = "smoke")
  public void device() throws LibImobileException, InterruptedException {
    IDeviceSDK d = new IDeviceSDK(main);
    d.release();


    System.out.println(d.isDeveloperMode());
  }

  @Test(groups = "smoke")
  public void deviceCanConnect() throws LibImobileException, InterruptedException {
    IOSDevice device = IMobileDeviceFactory.get(main);
    device.disconnect();

    Thread.sleep(5000);
  }

  @Test
  public void deviceCanConnectAndReportState() throws LibImobileException {
    IOSDevice device = factory.get(main);
    Assert.assertTrue(device.isConnected());
    device.disconnect();
    Assert.assertFalse(device.isConnected());

  }


  @Test
  public void reportNotConnectedDevice() throws LibImobileException {
    factory.get("idontexistaf579e27d166349dc8a1989503ba5b4f");
  }

  @Test
  public void canDisconnectIfNotConnected() throws LibImobileException {
    IOSDevice device = IMobileDeviceFactory.get(main);
    device.disconnect();
  }

  @Test
  public void canDisconnectTwice() throws LibImobileException {
    IOSDevice device = factory.get(main);

    device.disconnect();
    device.disconnect();
  }

  @Test
  public void canReconnect() throws LibImobileException {
    IOSDevice device;
    for (int i = 0; i < 10; i++) {
      device = factory.get(main);
      device.disconnect();
    }
  }

  @Test
  public void deviceIsSingleton() throws LibImobileException {
    IOSDevice device = factory.get(main);
    IOSDevice device2 = factory.get(main);
    Assert.assertTrue(device == device2);
  }

}
