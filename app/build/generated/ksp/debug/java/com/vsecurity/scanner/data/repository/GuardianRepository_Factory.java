package com.vsecurity.scanner.data.repository;

import com.vsecurity.scanner.data.local.AppSensorStatsDao;
import com.vsecurity.scanner.data.local.DailySummaryDao;
import com.vsecurity.scanner.data.local.PrivacyAlertDao;
import com.vsecurity.scanner.data.local.SensorAccessLogDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GuardianRepository_Factory implements Factory<GuardianRepository> {
  private final Provider<SensorAccessLogDao> sensorAccessLogDaoProvider;

  private final Provider<PrivacyAlertDao> privacyAlertDaoProvider;

  private final Provider<AppSensorStatsDao> appSensorStatsDaoProvider;

  private final Provider<DailySummaryDao> dailySummaryDaoProvider;

  private GuardianRepository_Factory(Provider<SensorAccessLogDao> sensorAccessLogDaoProvider,
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
    return newInstance(sensorAccessLogDaoProvider.get(), privacyAlertDaoProvider.get(), appSensorStatsDaoProvider.get(), dailySummaryDaoProvider.get());
  }

  public static GuardianRepository_Factory create(
      Provider<SensorAccessLogDao> sensorAccessLogDaoProvider,
      Provider<PrivacyAlertDao> privacyAlertDaoProvider,
      Provider<AppSensorStatsDao> appSensorStatsDaoProvider,
      Provider<DailySummaryDao> dailySummaryDaoProvider) {
    return new GuardianRepository_Factory(sensorAccessLogDaoProvider, privacyAlertDaoProvider, appSensorStatsDaoProvider, dailySummaryDaoProvider);
  }

  public static GuardianRepository newInstance(SensorAccessLogDao sensorAccessLogDao,
      PrivacyAlertDao privacyAlertDao, AppSensorStatsDao appSensorStatsDao,
      DailySummaryDao dailySummaryDao) {
    return new GuardianRepository(sensorAccessLogDao, privacyAlertDao, appSensorStatsDao, dailySummaryDao);
  }
}
