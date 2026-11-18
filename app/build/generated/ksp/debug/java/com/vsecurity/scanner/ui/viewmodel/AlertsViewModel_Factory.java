package com.vsecurity.scanner.ui.viewmodel;

import com.vsecurity.scanner.data.repository.GuardianRepository;
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
public final class AlertsViewModel_Factory implements Factory<AlertsViewModel> {
  private final Provider<GuardianRepository> guardianRepositoryProvider;

  private AlertsViewModel_Factory(Provider<GuardianRepository> guardianRepositoryProvider) {
    this.guardianRepositoryProvider = guardianRepositoryProvider;
  }

  @Override
  public AlertsViewModel get() {
    return newInstance(guardianRepositoryProvider.get());
  }

  public static AlertsViewModel_Factory create(
      Provider<GuardianRepository> guardianRepositoryProvider) {
    return new AlertsViewModel_Factory(guardianRepositoryProvider);
  }

  public static AlertsViewModel newInstance(GuardianRepository guardianRepository) {
    return new AlertsViewModel(guardianRepository);
  }
}
