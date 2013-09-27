package org.libimobiledevice.ios.driver.test;

import org.libimobiledevice.ios.driver.binding.IMobileDeviceFactory;
import org.libimobiledevice.ios.driver.binding.IOSDevice;
import org.libimobiledevice.ios.driver.binding.exceptions.LibImobileException;
import org.libimobiledevice.ios.driver.binding.instruments.Counter;
import org.libimobiledevice.ios.driver.binding.sdk.IDeviceSDK;
import org.libimobiledevice.ios.driver.binding.sdk.InstrumentsService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.libimobiledevice.ios.driver.test.ConnectedDevices.*;


public class InstrumentsTest {


  @Test(groups = "smoke")
  public void deviceCanRunInstruments() throws InterruptedException, LibImobileException {
    IDeviceSDK d = new IDeviceSDK(main);

    InstrumentsService instruments = new InstrumentsService(d, "com.ebay.iphone",null);

    Counter c = Counter.INSTANCE;


    for(int i=0;i<10;i++){
    Thread.sleep(200);
      c.start();
      instruments.executeScriptNonManaged("UIALogger.logMessage('Hello World!');");
    }

    instruments.stopApp();
  }
//
//
//  String
//      s =
//      "var s = {}; s.timestamp=UIATarget.localTarget().name();"
//      + "s.cpt = 0;"
//      + "MyObject = function(){\n"
//      + "this.myVariable = 'Hello';\n"
//      + "};\n"
//      + "MyObject.prototype.method = function(){\n"
//      + " UIALogger.logMessage('method called');\n"
//      + " };\n"
//      + "var pipo = new MyObject();\n"
//      + "UIATarget.localTarget().frontMostApp().setPreferencesValueForKey( s, 'ios-driver');"
//      + "UIATarget.localTarget().frontMostApp().setPreferencesValueForKey( pipo, 'object');";
//
  String
      s3 =
      "while (true){\n"
      + "UIATarget.localTarget().delay(0.1);\n"
      + "var cmd  = UIATarget.localTarget().frontMostApp().preferencesValueForKey('cmd');\n"
      + "UIALogger.logMessage('cmd '+cmd);\n"
      + "};";

  String s6="UIATarget.localTarget().frontMostApp().setPreferencesValueForKey( '/session', 'cmd');\n";
  String s7="UIATarget.localTarget().frontMostApp().setPreferencesValueForKey( '/session2', 'cmd');\n";
  String s8="UIATarget.localTarget().frontMostApp().setPreferencesValueForKey( '/session3', 'cmd');\n";

  String s4 = "UIATarget.localTarget().delay(5);\n"
              + "UIATarget.glob = 'Hell';\n"
              + "UIALogger.logMessage('after wait'+UIATarget.glob);";
  String s5 = "UIALogger.logMessage('direct'+UIATarget.glob);";


  String
      s2 =
      "var s = UIATarget.localTarget().frontMostApp().preferencesValueForKey('ios-driver');\n"
      + "for (i=0;i<1;i++){\n"
      + "UIALogger.logMessage('device :'+s.timestamp + '('+(s.cpt+i)+')');\n"
      + "}\n"
      + "s.cpt= s.cpt+1;\n"
      + "UIATarget.localTarget().frontMostApp().setPreferencesValueForKey( s, 'ios-driver');";
//
//  @Test(groups = "smoke")
//  public void deviceCannotShareObjects() throws InterruptedException {
//    final IOSDevice device = factory.get(main);
//    device.connect();
//    //device.startApp("com.yourcompany.InternationalMountains");
//    ScriptMessageHandler h = new DefaultScriptMessageHandler();
//    InstrumentsService instruments = new InstrumentsService(device, h);
//    instruments.startApp("com.yourcompany.UICatalog");
//    instruments.executeScriptNonManaged(s);
//    instruments.executeScriptNonManaged(s3);
//    instruments.stopApp();
//    device.disconnect();
//  }
//
//
  @Test(groups = "smoke")
  public void parallel() throws InterruptedException, LibImobileException {
    IDeviceSDK d = new IDeviceSDK(main);
    InstrumentsService service = new InstrumentsService(d,"com.yourcompany.UICatalog",null);

    service.executeScriptNonManaged(s3);
    Thread.sleep(1000);
    service.executeScriptNonManaged(s6);
    Thread.sleep(250);
    service.executeScriptNonManaged(s7);
    Thread.sleep(250);
    service.executeScriptNonManaged(s8);
    Thread.sleep(10000);

    System.out.println("DONE\n\n\n");
    service.stopApp();
  }
//
//
//  @Test(groups = "smoke")
//  public void deviceCanRunInstrumentsNonManaged() throws InterruptedException {
//    final IOSDevice device = factory.get(main);
//    device.connect();
//    //device.startApp("com.yourcompany.InternationalMountains");
//    ScriptMessageHandler h = new DefaultScriptMessageHandler();
//    InstrumentsService instruments = new InstrumentsService(device, h);
//    instruments.startApp("com.yourcompany.UICatalog");
//    long start = System.currentTimeMillis();
//    long all = 0;
//    int cpt = 0;
//    instruments.executeScriptNonManaged(s);
//    for (int i = 0; i < 50; i++) {
//      start = System.currentTimeMillis();
//      instruments.executeScriptNonManaged(s2);
//      long total = (System.currentTimeMillis() - start);
//      all += total;
//      cpt++;
//      //System.out.println("total :" + (System.currentTimeMillis() - start));
//
//    }
//    System.out.println("avg : " + (all / cpt));
//    //Thread.sleep(5000);
//    instruments.stopApp();
//    device.disconnect();
//  }
//
//
//  @Test(groups = "smoke")
//  public void startStopSafari() {
//    IOSDevice device = factory.get(main);
//    device.connect();
//    ScriptMessageHandler h = new DefaultScriptMessageHandler();
//    InstrumentsService instruments = new InstrumentsService(device, h);
//    instruments.startApp("com.apple.mobilesafari");
//
//    instruments.stopApp();
//    device.disconnect();
//  }
//
//
//  @DataProvider(name = "devices", parallel = true)
//  public Object[][] createData1() {
//    return new Object[][]{
//        {main},
//        //{device2},
//        //{device3},
//    };
//  }
//
//
//  @Test(dataProvider = "devices", invocationCount = 1)
//  public void instrumentsParallel(String uuid) throws InterruptedException {
//    final IOSDevice device = factory.get(uuid);
//    device.connect();
//    //device.startApp("com.yourcompany.InternationalMountains");
//    long start = System.currentTimeMillis();
//
//    ScriptMessageHandler h = new DefaultScriptMessageHandler();
//    InstrumentsService instruments = new InstrumentsService(device, h);
//    instruments.startApp("com.yourcompany.UICatalog");
//
//    //System.out.println("1->" + (System.currentTimeMillis() - start));
//    instruments.executeScriptNonManaged(s);
//    for (int i = 0; i < 1000; i++) {
//      start = System.currentTimeMillis();
//      //Thread.sleep(5000);
//      instruments.executeScriptNonManaged(s2);
//      //System.out.println("2->" + (System.currentTimeMillis() - start));
//    }
//    instruments.stopApp();
//    device.disconnect();
//  }
//
//
  @DataProvider(name = "createDataSafari", parallel = true)
  public Object[][] createDataSafari() {
    return new Object[][]{
       // {main},
        {device2},
      };
  }



  @Test(dataProvider = "createDataSafari", invocationCount = 1)
  public void safariParallel(String uuid) throws InterruptedException {

    IDeviceSDK d;

    synchronized (this) {
      d = new IDeviceSDK(uuid);
    }

    InstrumentsService instruments = new InstrumentsService(d, "com.apple.mobilesafari",null);

    Counter c = Counter.INSTANCE;


    for(int i=0;i<10;i++){
      Thread.sleep(200);
      c.start();
      instruments.executeScriptNonManaged("UIALogger.logMessage('Hello World!');");
    }

    instruments.stopApp();
  }
}
