package com.vsecurity.scanner.ui.viewmodel;

import com.vsecurity.scanner.data.preferences.PreferencesManager;
import com.vsecurity.scanner.data.repository.GuardianRepository;
import com.vsecurity.scanner.data.repository.ScannerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ScannerRepository> scannerRepositoryProvider;

  private final Provider<GuardianRepository> guardianRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private DashboardViewModel_Factory(Provider<ScannerRepository> scannerRepositoryProvider,
      Provider<GuardianRepository> guardianRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.scannerRepositoryProvider = scannerRepositoryProvider;
    this.guardianRepositoryProvider = guardianRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(scannerRepositoryProvider.get(), guardianRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<ScannerRepository> scannerRepositoryProvider,
      Provider<GuardianRepository> guardianRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new DashboardViewModel_Factory(scannerRepositoryProvider, guardianRepositoryProvider, preferencesManagerProvider);
  }

  public static DashboardViewModel newInstance(ScannerRepository scannerRepository,
      GuardianRepository guardianRepository, PreferencesManager preferencesManager) {
    return new DashboardViewModel(scannerRepository, guardianRepository, preferencesManager);
  }
}
