package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.DeviceInfo;
import org.libimobiledevice.ios.driver.binding.IMobileDeviceFactory;
import org.libimobiledevice.ios.driver.binding.IOSDevice;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class LockDownValueTest {

//  private IMobileDeviceFactory factory = IMobileDeviceFactory.INSTANCE;
//
//  @Test(groups = "smoke")
//  public void deviceCanSetLocale() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    device.setLockDownValue("com.apple.international", "Locale", "en_GB");
//    device.setLockDownValue("com.apple.international", "Locale", "en_US");
//    device.setLockDownValue("com.apple.international", "Locale", "fr_CA");
//
//    device.disconnect();
//  }
//
//
//  @Test(groups = "smoke")
//  public void deviceCanSetLanguage() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    device.setLockDownValue("com.apple.international", "Language", "fr");
//    device.setLockDownValue("com.apple.international", "Language", "en");
//
//    device.disconnect();
//  }
//
//
//  @Test(groups = "smoke")
//  public void deviceCanGetLocale() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    String res = device.getLockDownValue("com.apple.international", "Locale");
//    System.out.println("res :" + res);
//    device.disconnect();
//  }
//
//  @Test//(invocationCount = 5,threadPoolSize = 5)
//  public void deviceCanGetInfo() {
//    Assert.assertEquals("",new DeviceInfo(main).getDeviceName());
//  }
}
