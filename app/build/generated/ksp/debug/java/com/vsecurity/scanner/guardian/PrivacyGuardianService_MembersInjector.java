package com.vsecurity.scanner.guardian;

import com.vsecurity.scanner.data.preferences.PreferencesManager;
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
public final class PrivacyGuardianService_MembersInjector implements MembersInjector<PrivacyGuardianService> {
  private final Provider<GuardianRepository> repositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private PrivacyGuardianService_MembersInjector(Provider<GuardianRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public void injectMembers(PrivacyGuardianService instance) {
    injectRepository(instance, repositoryProvider.get());
    injectPreferencesManager(instance, preferencesManagerProvider.get());
  }

  public static MembersInjector<PrivacyGuardianService> create(
      Provider<GuardianRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new PrivacyGuardianService_MembersInjector(repositoryProvider, preferencesManagerProvider);
  }

  @InjectedFieldSignature("com.vsecurity.scanner.guardian.PrivacyGuardianService.repository")
  public static void injectRepository(PrivacyGuardianService instance,
      GuardianRepository repository) {
    instance.repository = repository;
  }

  @InjectedFieldSignature("com.vsecurity.scanner.guardian.PrivacyGuardianService.preferencesManager")
  public static void injectPreferencesManager(PrivacyGuardianService instance,
      PreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }
}
