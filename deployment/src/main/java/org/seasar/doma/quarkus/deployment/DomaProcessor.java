package org.seasar.doma.quarkus.deployment;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

import io.quarkus.agroal.deployment.JdbcDataSourceBuildItem;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.BeanContainerListenerBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.HotDeploymentWatchedFileBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jboss.jandex.DotName;
import org.seasar.doma.DaoImplementation;
import org.seasar.doma.quarkus.runtime.DomaProducer;
import org.seasar.doma.quarkus.runtime.DomaRecorder;
import org.seasar.doma.quarkus.runtime.JtaRequiresNewController;
import org.seasar.doma.quarkus.runtime.ScriptExecutor;
import org.seasar.doma.quarkus.runtime.UnsupportedTransactionManager;

class DomaProcessor {

  private static final String FEATURE = "doma";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  AdditionalBeanBuildItem additionalBeans() {
    return new AdditionalBeanBuildItem(
        DomaProducer.class, JtaRequiresNewController.class, UnsupportedTransactionManager.class);
  }

  @BuildStep
  BeanDefiningAnnotationBuildItem beanDefiningAnnotation() {
    return new BeanDefiningAnnotationBuildItem(
        DotName.createSimple(DaoImplementation.class.getName()));
  }

  @BuildStep
  DomaSettingsBuildItem domaSettings(
      DomaBuildTimeConfig buildTimeConfig,
      List<JdbcDataSourceBuildItem> dataSources,
      ApplicationArchivesBuildItem applicationArchives,
      LaunchModeBuildItem launchMode) {
    var factory =
        new DomaSettingsFactory(buildTimeConfig, dataSources, applicationArchives, launchMode);
    return new DomaSettingsBuildItem(factory.create());
  }

  @BuildStep
  Optional<HotDeploymentWatchedFileBuildItem> hotDeploymentWatchedFile(
      DomaSettingsBuildItem settings) {
    var sqlLoadScript = settings.getSettings().sqlLoadScript;
    if (sqlLoadScript == null) {
      return Optional.empty();
    }
    return Optional.of(new HotDeploymentWatchedFileBuildItem(sqlLoadScript));
  }

  @BuildStep
  NativeImageResourceBuildItem nativeImageResources(DomaSettingsBuildItem settings) {
    List<String> resources = new ArrayList<>();
    var sqlLoadScript = settings.getSettings().sqlLoadScript;
    if (sqlLoadScript != null) {
      resources.add(sqlLoadScript);
    }
    var scanner = new DomaResourceScanner();
    var scannedResources = scanner.scan();
    resources.addAll(scannedResources);
    return new NativeImageResourceBuildItem(resources);
  }

  @BuildStep
  ReflectiveClassBuildItem reflectiveClasses(BeanArchiveIndexBuildItem beanArchiveIndex) {
    List<String> classes = new ArrayList<>();
    classes.add(ScriptExecutor.class.getName());
    var indexView = beanArchiveIndex.getIndex();
    var scanner = new DomaClassScanner(indexView);
    var scannedClasses = scanner.scan();
    classes.addAll(scannedClasses);
    return new ReflectiveClassBuildItem(true, true, classes.toArray(new String[0]));
  }

  @BuildStep
  @Record(STATIC_INIT)
  BeanContainerListenerBuildItem beanContainerListener(
      DomaRecorder recorder, DomaSettingsBuildItem settings, LaunchModeBuildItem launchMode) {
    return new BeanContainerListenerBuildItem(
        recorder.configure(settings.getSettings(), launchMode.getLaunchMode()));
  }
}
