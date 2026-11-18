package com.vsecurity.scanner.ui.viewmodel;

import android.content.Context;
import com.vsecurity.scanner.data.preferences.PreferencesManager;
import com.vsecurity.scanner.data.repository.GuardianRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class GuardianViewModel_Factory implements Factory<GuardianViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<GuardianRepository> guardianRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  private GuardianViewModel_Factory(Provider<Context> contextProvider,
      Provider<GuardianRepository> guardianRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.contextProvider = contextProvider;
    this.guardianRepositoryProvider = guardianRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public GuardianViewModel get() {
    return newInstance(contextProvider.get(), guardianRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static GuardianViewModel_Factory create(Provider<Context> contextProvider,
      Provider<GuardianRepository> guardianRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new GuardianViewModel_Factory(contextProvider, guardianRepositoryProvider, preferencesManagerProvider);
  }

  public static GuardianViewModel newInstance(Context context,
      GuardianRepository guardianRepository, PreferencesManager preferencesManager) {
    return new GuardianViewModel(context, guardianRepository, preferencesManager);
  }
}
