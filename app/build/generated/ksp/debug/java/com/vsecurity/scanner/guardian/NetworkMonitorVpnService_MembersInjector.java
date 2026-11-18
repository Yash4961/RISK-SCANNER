package com.vsecurity.scanner.guardian;

import com.vsecurity.scanner.data.repository.GuardianRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class NetworkMonitorVpnService_MembersInjector implements MembersInjector<NetworkMonitorVpnService> {
  private final Provider<GuardianRepository> repositoryProvider;

  private NetworkMonitorVpnService_MembersInjector(
      Provider<GuardianRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public void injectMembers(NetworkMonitorVpnService instance) {
    injectRepository(instance, repositoryProvider.get());
  }

  public static MembersInjector<NetworkMonitorVpnService> create(
      Provider<GuardianRepository> repositoryProvider) {
    return new NetworkMonitorVpnService_MembersInjector(repositoryProvider);
  }

  @InjectedFieldSignature("com.vsecurity.scanner.guardian.NetworkMonitorVpnService.repository")
  public static void injectRepository(NetworkMonitorVpnService instance,
      GuardianRepository repository) {
    instance.repository = repository;
  }
}
