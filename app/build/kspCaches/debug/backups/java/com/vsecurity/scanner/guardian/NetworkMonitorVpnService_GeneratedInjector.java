package com.vsecurity.scanner.guardian;

import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.internal.GeneratedEntryPoint;
import javax.annotation.processing.Generated;

@OriginatingElement(
    topLevelClass = NetworkMonitorVpnService.class
)
@GeneratedEntryPoint
@InstallIn(ServiceComponent.class)
@Generated("dagger.hilt.android.processor.internal.androidentrypoint.InjectorEntryPointGenerator")
public interface NetworkMonitorVpnService_GeneratedInjector {
  void injectNetworkMonitorVpnService(NetworkMonitorVpnService networkMonitorVpnService);
}
