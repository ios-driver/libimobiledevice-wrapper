package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.libimobiledevice.ios.driver.binding.ApplicationInfo;
import org.libimobiledevice.ios.driver.binding.services.DeviceService;
import org.libimobiledevice.ios.driver.binding.services.IOSDevice;
import org.libimobiledevice.ios.driver.binding.services.InstallCallback;
import org.libimobiledevice.ios.driver.binding.services.InstallerService;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.main;

public class InstallTest {

  final File base = new File("/Users/freynaud/Downloads/ebay_iphone_enterprise_3.2.0a1_build6.ipa");

  //  final File base = new File("/Users/freynaud/Documents/workspace/ios-driver/applications/");
//  final File intl = new File(base, "com.yourcompany.InternationalMountains.ipa");
//  final File uicatalogVerifFailed = new File(base, "com.yourcompany.UICatalog.ipa");
//  final File uicatalog = new File("/Users/freynaud/out/com.yourcompany.UICatalog.ipa");
//
//  private LibImobileDeviceWrapperFactory factory = LibImobileDeviceWrapperFactory.INSTANCE;
//
//  @Test(groups = "smoke")
//  public void a() {
//    IMobileDeviceService.INSTANCE.test();
//  }
//
  @Test(groups = "smoke")
  public void deviceCanInstall() throws LibImobileException, SDKException {
    IOSDevice d = DeviceService.get(main);
    InstallerService service = new InstallerService(d);
    //SysLogService s2 = d.getSysLogService();
    //s2.start();

    final InstallCallback cb = new InstallCallback() {
      @Override
      protected void onUpdate(String operation, int percent, String message) {
        sout(operation, percent, message);
      }
    };
    File ui = new File("/Users/freynaud/out/com.yourcompany.UICatalog.ipa");
    File eBay = new File("/Users/freynaud/tmp/com.ebay.iphone.ipa");

    service.install(eBay, cb);

    service.free();
//    service.install(new File("/Users/freynaud/Downloads/ebay_iphone_enterprise_3.2.0a1_build6.ipa"));
    //service.install(new File("/Users/freynaud/com.ebay.iphone_3.2_ios7.ipa"));

//    service.install(new File("/Users/freynaud/Documents/tmp/ebay_iphone_enterprise_3.1.0a1_build12.ipa"));
//  device.install(new File("/Users/freynaud/out/com.yourcompany.UICatalog.ipa"));
    //device.install(new File("/Users/freynaud/imobile/workspace/eBay.ipa"));
    //device.install(new File("/Users/freynaud/Documents/tmp/ebay_iphone_enterprise_3.1.0a1_build12.ipa"));
    //device.install(new File("/Users/freynaud/Documents/tmp/Payload.ipa"));

//    device.disconnect();
  }

  @Test
  public void canListApplications() throws LibImobileException, SDKException {
    IOSDevice d = DeviceService.get(main);
    InstallerService service = new InstallerService(d);
    List<ApplicationInfo> l = service.listApplications();
    for (ApplicationInfo app : l){
      System.out.println(app.getApplicationId());
    }
    service.free();
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
  @Test(dependsOnMethods = "deviceCanInstall")
  public void deviceCanUninstall() throws SDKException, LibImobileException {
    IOSDevice d = DeviceService.get(main);
    InstallerService service = new InstallerService(d);
    //service.uninstall("com.ebay.iphone");
    service.uninstall("com.yourcompany.UICatalog");
    service.free();
  }
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
