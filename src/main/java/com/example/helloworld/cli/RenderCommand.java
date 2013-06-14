package com.example.helloworld.cli;

import com.codahale.dropwizard.cli.ConfiguredCommand;
import com.codahale.dropwizard.setup.Bootstrap;
import com.example.helloworld.HelloWorldConfiguration;
import com.example.helloworld.domain.Template;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

@Slf4j
public class RenderCommand extends ConfiguredCommand<HelloWorldConfiguration> {

    public RenderCommand() {
        super("render", "Render the template data to console");
    }

    @Override
    public void configure(final Subparser subparser) {
        super.configure(subparser);
        subparser.addArgument("-i", "--include-default")
                 .action(Arguments.storeTrue())
                 .dest("include-default")
                 .help("Also render the template with the default name");
        subparser.addArgument("names").nargs("*");
    }

    @Override
    protected void run(final Bootstrap<HelloWorldConfiguration> bootstrap,
                       final Namespace namespace,
                       final HelloWorldConfiguration configuration) throws Exception {

        final Template template = configuration.createTemplate();

        if (namespace.getBoolean("include-default")) {
            log.info("DEFAULT => {}", template.render(Optional.<String>absent()));
        }

        for (String name : namespace.<String>getList("names")) {
            for (int i = 0; i < 1000; i++) {
                log.info("{} => {}", name, template.render(Optional.of(name)));
                Thread.sleep(1000);
            }
        }
    }
}
