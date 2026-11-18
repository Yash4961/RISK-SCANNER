package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.PrivacyAlertDao;
import com.vsecurity.scanner.data.local.VScannerDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvidePrivacyAlertDaoFactory implements Factory<PrivacyAlertDao> {
  private final Provider<VScannerDatabase> databaseProvider;

  private AppModule_ProvidePrivacyAlertDaoFactory(Provider<VScannerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PrivacyAlertDao get() {
    return providePrivacyAlertDao(databaseProvider.get());
  }

  public static AppModule_ProvidePrivacyAlertDaoFactory create(
      Provider<VScannerDatabase> databaseProvider) {
    return new AppModule_ProvidePrivacyAlertDaoFactory(databaseProvider);
  }

  public static PrivacyAlertDao providePrivacyAlertDao(VScannerDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePrivacyAlertDao(database));
  }
}
