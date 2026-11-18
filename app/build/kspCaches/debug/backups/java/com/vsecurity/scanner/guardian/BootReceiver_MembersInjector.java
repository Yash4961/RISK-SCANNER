package com.vsecurity.scanner.guardian;

import com.vsecurity.scanner.data.preferences.PreferencesManager;
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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private BootReceiver_MembersInjector(Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectPreferencesManager(instance, preferencesManagerProvider.get());
  }

  public static MembersInjector<BootReceiver> create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new BootReceiver_MembersInjector(preferencesManagerProvider);
  }

  @InjectedFieldSignature("com.vsecurity.scanner.guardian.BootReceiver.preferencesManager")
  public static void injectPreferencesManager(BootReceiver instance,
      PreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }
}
