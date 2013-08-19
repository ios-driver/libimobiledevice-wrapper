package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.DeviceDetectionCallback;
import org.libimobiledevice.ios.driver.binding.IMobileDeviceFactory;
import org.testng.annotations.Test;

public class DeviceDetectorTest {


  private IMobileDeviceFactory factory = IMobileDeviceFactory.INSTANCE;
  private static final DeviceDetectionCallback cb = new DeviceDetectionCallback() {
    @Override
    public void onAdded(String uuid) {
      System.out.println("added " + uuid);
    }

    @Override
    public void onRemoved(String uuid) {
      System.out.println("removed : "+uuid);
    }
  };


  @Test
  public void ok() throws InterruptedException {

    factory.setDeviceDetectionCallback(cb);
    factory.startDetection();
    Thread.sleep(60000);

  }


}
