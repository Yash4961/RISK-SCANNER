package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.SensorAccessLogDao;
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
public final class AppModule_ProvideSensorAccessLogDaoFactory implements Factory<SensorAccessLogDao> {
  private final Provider<VScannerDatabase> databaseProvider;

  private AppModule_ProvideSensorAccessLogDaoFactory(Provider<VScannerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SensorAccessLogDao get() {
    return provideSensorAccessLogDao(databaseProvider.get());
  }

  public static AppModule_ProvideSensorAccessLogDaoFactory create(
      Provider<VScannerDatabase> databaseProvider) {
    return new AppModule_ProvideSensorAccessLogDaoFactory(databaseProvider);
  }

  public static SensorAccessLogDao provideSensorAccessLogDao(VScannerDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSensorAccessLogDao(database));
  }
}
