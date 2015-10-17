package com.github.ambry.rest;

import com.codahale.metrics.MetricRegistry;
import com.github.ambry.config.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Netty specific implementation of {@link NioServerFactory}.
 * <p/>
 * Sets up all the supporting cast required for the operation of {@link NettyServer} and returns a new instance on
 * {@link NettyServerFactory#getNioServer()}.
 */
public class NettyServerFactory implements NioServerFactory {

  private final NettyConfig nettyConfig;
  private final NettyMetrics nettyMetrics;
  private final RequestResponseHandlerController requestResponseHandlerController;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public NettyServerFactory(VerifiableProperties verifiableProperties, MetricRegistry metricRegistry,
      RequestResponseHandlerController requestResponseHandlerController)
      throws InstantiationException {
    if (verifiableProperties != null && metricRegistry != null && requestResponseHandlerController != null) {
      this.nettyConfig = new NettyConfig(verifiableProperties);
      this.nettyMetrics = new NettyMetrics(metricRegistry);
      this.requestResponseHandlerController = requestResponseHandlerController;
    } else {
      StringBuilder errorMessage =
          new StringBuilder("Null arg(s) received during instantiation of NettyServerFactory -");
      if (verifiableProperties == null) {
        errorMessage.append(" [VerifiableProperties] ");
      }
      if (metricRegistry == null) {
        errorMessage.append(" [MetricRegistry] ");
      }
      if (requestResponseHandlerController == null) {
        errorMessage.append(" [RequestResponseHandlerController] ");
      }
      throw new InstantiationException(errorMessage.toString());
    }
    logger.trace("Instantiated NettyServerFactory");
  }

  /**
   * Returns a new instance of {@link NettyServer}.
   * @return a new instance of {@link NettyServer}.
   */
  public NioServer getNioServer() {
    return new NettyServer(nettyConfig, nettyMetrics, requestResponseHandlerController);
  }
}
