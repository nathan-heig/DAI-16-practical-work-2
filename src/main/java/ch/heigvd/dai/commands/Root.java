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
        
        @CommandLine.Option(
                names = {"-p", "--port"},
                description = "The name of the file in which the secret key used for encryption is stored",
                required = false,
                defaultValue = "1234")
        private int port;

        

        public int getPort(){
                return port;
        }
}
