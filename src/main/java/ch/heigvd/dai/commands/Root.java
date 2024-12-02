package ch.heigvd.dai.commands;

import picocli.CommandLine;

@CommandLine.Command(
        description = "A program use for messaging",
        version = "1.0.0",
        subcommands = {
                Server.class,
                Client.class
        },
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)


public class Root {
        private int port = 1234;

        public int getPort(){
                return port;
        }
}
