package ch.heigvd.dai.commands;

import picocli.CommandLine;

@CommandLine.Command(
        description = "A program use for encryption and decryption of files",
        version = "1.0.0",
        subcommands = {
                Encrypt.class,
                Decrypt.class,
                CreateKey.class
        },
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)
public class Root {

        public enum AvailableAlgorithms {
                AES(new int[]{128, 192, 256}, false),
                DES(new int[]{56}, true),
                DESede(new int[]{112, 168}, true);

                private final int[] keySizes;
                private final boolean depreciated;

                private AvailableAlgorithms(int[] keySizes, boolean depreciated) {
                        this.keySizes = keySizes;
                        this.depreciated = depreciated;
                }

                public int[] getKeySizes() {return keySizes;}
                public boolean isDepreciated() {return depreciated;}
        }
    
    @CommandLine.Parameters(index = "0", description = "The name of the file to encrypt or decrypt.")
    private String filename;

    @CommandLine.Option(
            names = {"-a", "--algorithm"},
            description = "The algorithm to use (possible values:  ${COMPLETION-CANDIDATES}).",
            required = true)
    private AvailableAlgorithms algorithm;


    // Getters
    public String getFilename() {return filename;}
    public AvailableAlgorithms getAlgorithm() {return algorithm;}

}
