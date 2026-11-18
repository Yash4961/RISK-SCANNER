package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.ScannedAppDao;
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
public final class AppModule_ProvideScannedAppDaoFactory implements Factory<ScannedAppDao> {
  private final Provider<VScannerDatabase> databaseProvider;

  private AppModule_ProvideScannedAppDaoFactory(Provider<VScannerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ScannedAppDao get() {
    return provideScannedAppDao(databaseProvider.get());
  }

  public static AppModule_ProvideScannedAppDaoFactory create(
      Provider<VScannerDatabase> databaseProvider) {
    return new AppModule_ProvideScannedAppDaoFactory(databaseProvider);
  }

  public static ScannedAppDao provideScannedAppDao(VScannerDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideScannedAppDao(database));
  }
}
