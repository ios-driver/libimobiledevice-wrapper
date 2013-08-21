package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.binding.sdk.InstallerService;
import org.libimobiledevice.ios.driver.binding.sdk.SysLogService;
import org.testng.annotations.Test;

import java.io.File;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class InstallTest {

  //  final File base = new File("/Users/freynaud/Documents/workspace/ios-driver/applications/");
//  final File intl = new File(base, "com.yourcompany.InternationalMountains.ipa");
//  final File uicatalogVerifFailed = new File(base, "com.yourcompany.UICatalog.ipa");
//  final File uicatalog = new File("/Users/freynaud/out/com.yourcompany.UICatalog.ipa");
//
//  private IMobileDeviceFactory factory = IMobileDeviceFactory.INSTANCE;
//
//  @Test(groups = "smoke")
//  public void a() {
//    IMobileDeviceService.INSTANCE.test();
//  }
//
  @Test(groups = "smoke")
  public void deviceCanInstall() throws InterruptedException, LibImobileException {
    IDeviceSDK d = new IDeviceSDK(main);
    InstallerService service = new InstallerService(d);
    SysLogService s2 = new SysLogService(d);
    s2.start();
    service.install(new File("/Users/freynaud/out/com.yourcompany.UICatalog.ipa"));
//    service.install(new File("/Users/freynaud/Documents/tmp/ebay_iphone_enterprise_3.1.0a1_build12.ipa"));
//  device.install(new File("/Users/freynaud/out/com.yourcompany.UICatalog.ipa"));
    //device.install(new File("/Users/freynaud/imobile/workspace/eBay.ipa"));
    //device.install(new File("/Users/freynaud/Documents/tmp/ebay_iphone_enterprise_3.1.0a1_build12.ipa"));
    //device.install(new File("/Users/freynaud/Documents/tmp/Payload.ipa"));
//    device.disconnect();
  }


  @Test
  public void canListApplications() throws LibImobileException {
    IDeviceSDK d = new IDeviceSDK(main);
    InstallerService service = new InstallerService(d);
    service.listApplications();
  }
//
//  @Test
//  public void deviceCanDetectErrorsInstall() throws InterruptedException {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    device.install(uicatalogVerifFailed);
//    device.disconnect();
//  }
//
//
//  @Test(dependsOnMethods = "deviceCanInstall")
//  public void deviceCanUninstall() {
//    IOSDevice device = factory.get(device2);
//    device.connect();
//    device.uninstall("com.yourcompany.InternationalMountains");
//    device.disconnect();
//  }
//
//  @Test
//  public void test() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    device
//        .install(new File("/Users/freynaud/Downloads/ebay_iphone_enterprise_3.0.0rc1_build38.ipa"));
//    device.disconnect();
//  }
//
//  @DataProvider(name = "devices", parallel = true)
//  public Object[][] createData1() {
//    return new Object[][]{
//        {main},
//        {device2},
//    };
//  }
//
//
//  @Test(dataProvider = "devices")
//  public void deviceCanInstallParallel(String uuid) {
//    IOSDevice device = factory.get(uuid);
//    device.connect();
//    device.install(uicatalog);
//    device.disconnect();
//  }
//
//  @Test
//  public void canListApps() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    device
//        .install(new File("/Users/freynaud/Downloads/ebay_iphone_enterprise_3.0.0rc1_build38.ipa"));
//    device.disconnect();
//  }
//
//  @Test
//  public void canArchiveApp() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    device.archive("com.yourcompany.UICatalog");
//    //device.archive("com.yourcompany.InternationalMountains");
//    device.disconnect();
//  }
//
//  @Test
//  public void emptyApplicationCache() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    //device.emptyApplicationCache("com.ebay.iphone");
//    device.emptyApplicationCache("com.yourcompany.UICatalog");
//    device.disconnect();
//  }

}
