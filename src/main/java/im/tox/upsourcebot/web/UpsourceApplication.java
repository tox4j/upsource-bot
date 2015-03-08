package im.tox.upsourcebot.web;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UpsourceApplication extends Application<UpsourceConfiguration> {

  public static void main(String[] args) throws Exception {
    new UpsourceApplication().run(args);
  }

  @Override
  public void run(UpsourceConfiguration configuration, Environment environment) throws Exception {

  }

  @Override
  public String getName() {
    return "upsource-bot";
  }

}
