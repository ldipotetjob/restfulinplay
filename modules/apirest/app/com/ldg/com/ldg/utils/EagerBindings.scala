package com.ldg.utils

import com.google.inject.AbstractModule
import services.{DataServices, TDataServices}

/**
  * @see https://playframework.com/documentation/2.6.x/ScalaDependencyInjection#eager-bindings
  */
class EagerBindings extends AbstractModule {
  def configure() = {

  bind(classOf[TDataServices]).to(classOf[DataServices])
    .asEagerSingleton()
}
}
