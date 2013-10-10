package org.libimobiledevice.ios.driver.binding.services;

import org.libimobiledevice.ios.driver.binding.exceptions.InstrumentsSDKException;

public class ProcessControlService {

  private final InstrumentsClient instrumentsClient;
  private final ProcessControlClient processControlClient;

  ProcessControlService(InstrumentsClient instrumentsClient) throws InstrumentsSDKException {
    this.instrumentsClient = instrumentsClient;
    processControlClient = instrumentsClient.getProcessControlClient();
  }

  public final void releaseProcess(int pid) throws InstrumentsSDKException {
    processControlClient.stopObserving(pid);
    processControlClient.kill(pid);
  }

  public final int getPid(String bundleId) throws InstrumentsSDKException {
    return processControlClient.getPid(bundleId);
  }

  public final void kill(int pid) throws InstrumentsSDKException {
    if (pid > 0) {
      processControlClient.kill(pid);
    }
  }

  public final void kill(String bundleId) throws InstrumentsSDKException {
    int pid = getPid(bundleId);
    kill(pid);
  }

  public final int launch(String bundleId) throws InstrumentsSDKException {
    int pid = getPid(bundleId);
    kill(pid);

    pid = processControlClient.launchSuspendedProcess(bundleId, null, null);
    processControlClient.startObserving(pid);
    processControlClient.resume(pid);
    return pid;
  }

  public final void free() {
    processControlClient.free();
  }
}
