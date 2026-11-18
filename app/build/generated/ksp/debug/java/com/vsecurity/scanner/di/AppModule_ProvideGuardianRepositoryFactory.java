package com.vsecurity.scanner.di;

import com.vsecurity.scanner.data.local.AppSensorStatsDao;
import com.vsecurity.scanner.data.local.DailySummaryDao;
import com.vsecurity.scanner.data.local.PrivacyAlertDao;
import com.vsecurity.scanner.data.local.SensorAccessLogDao;
import com.vsecurity.scanner.data.repository.GuardianRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideGuardianRepositoryFactory implements Factory<GuardianRepository> {
  private final Provider<SensorAccessLogDao> sensorAccessLogDaoProvider;

  private final Provider<PrivacyAlertDao> privacyAlertDaoProvider;

  private final Provider<AppSensorStatsDao> appSensorStatsDaoProvider;

  private final Provider<DailySummaryDao> dailySummaryDaoProvider;

  private AppModule_ProvideGuardianRepositoryFactory(
      Provider<SensorAccessLogDao> sensorAccessLogDaoProvider,
      Provider<PrivacyAlertDao> privacyAlertDaoProvider,
      Provider<AppSensorStatsDao> appSensorStatsDaoProvider,
      Provider<DailySummaryDao> dailySummaryDaoProvider) {
    this.sensorAccessLogDaoProvider = sensorAccessLogDaoProvider;
    this.privacyAlertDaoProvider = privacyAlertDaoProvider;
    this.appSensorStatsDaoProvider = appSensorStatsDaoProvider;
    this.dailySummaryDaoProvider = dailySummaryDaoProvider;
  }

  @Override
  public GuardianRepository get() {
    return provideGuardianRepository(sensorAccessLogDaoProvider.get(), privacyAlertDaoProvider.get(), appSensorStatsDaoProvider.get(), dailySummaryDaoProvider.get());
  }

  public static AppModule_ProvideGuardianRepositoryFactory create(
      Provider<SensorAccessLogDao> sensorAccessLogDaoProvider,
      Provider<PrivacyAlertDao> privacyAlertDaoProvider,
      Provider<AppSensorStatsDao> appSensorStatsDaoProvider,
      Provider<DailySummaryDao> dailySummaryDaoProvider) {
    return new AppModule_ProvideGuardianRepositoryFactory(sensorAccessLogDaoProvider, privacyAlertDaoProvider, appSensorStatsDaoProvider, dailySummaryDaoProvider);
  }

  public static GuardianRepository provideGuardianRepository(SensorAccessLogDao sensorAccessLogDao,
      PrivacyAlertDao privacyAlertDao, AppSensorStatsDao appSensorStatsDao,
      DailySummaryDao dailySummaryDao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGuardianRepository(sensorAccessLogDao, privacyAlertDao, appSensorStatsDao, dailySummaryDao));
  }
}
